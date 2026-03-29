package com.library.service;

import com.library.model.Category;
import com.library.model.Resource;
import com.library.repository.CategoryRepository;
import com.library.repository.ResourceRepository;

import java.time.LocalDate;
import java.util.*;

/**
 * Service layer that sits between the UI and the repositories.
 * All business rules and validations live here.
 */
public class LibraryService {

    private final ResourceRepository resourceRepo;
    private final CategoryRepository categoryRepo;

    public LibraryService(ResourceRepository resourceRepo,
                          CategoryRepository categoryRepo) {
        this.resourceRepo = resourceRepo;
        this.categoryRepo = categoryRepo;
    }

    // ─── Resource operations ─────────────────────────────────────────────────

    public void addResource(Resource resource) {
        validateResource(resource);
        // Auto-create category if it doesn't exist
        if (resource.getCategory() != null && !resource.getCategory().isBlank()) {
            if (!categoryRepo.exists(resource.getCategory())) {
                categoryRepo.save(new Category(resource.getCategory(), "Auto-created"));
            }
            categoryRepo.findById(resource.getCategory())
                        .ifPresent(c -> c.addResource(resource.getId()));
        }
        resourceRepo.save(resource);
    }

    public Optional<Resource> getResource(String id) {
        return resourceRepo.findById(id);
    }

    public List<Resource> getAllResources() {
        return resourceRepo.findAll();
    }

    public void updateResource(Resource resource) {
        validateResource(resource);
        resourceRepo.update(resource);
    }

    public void deleteResource(String id) {
        Resource r = resourceRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        // Remove from category index
        if (r.getCategory() != null) {
            categoryRepo.findById(r.getCategory())
                        .ifPresent(c -> c.removeResource(id));
        }
        resourceRepo.delete(id);
    }

    /** Mark resource as read, set dateRead to today, and optionally rate it. */
    public void markAsRead(String id, int rating) {
        Resource r = resourceRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        r.setStatus(Resource.Status.COMPLETED);
        r.setDateRead(LocalDate.now());
        if (rating >= 1 && rating <= 5) {
            r.setRating(rating);
        }
        resourceRepo.update(r);
    }

    /** Change the reading status of a resource. */
    public void updateStatus(String id, Resource.Status status) {
        Resource r = resourceRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        r.setStatus(status);
        resourceRepo.update(r);
    }

    /** Add a personal note to a resource (appends, doesn't overwrite). */
    public void addNote(String id, String note) {
        Resource r = resourceRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
        String existing = r.getNotes() == null ? "" : r.getNotes() + "\n";
        r.setNotes(existing + note.trim());
        resourceRepo.update(r);
    }

    // ─── Search & filter ─────────────────────────────────────────────────────

    public List<Resource> search(String keyword) {
        return resourceRepo.search(keyword);
    }

    public List<Resource> filterByCategory(String category) {
        return resourceRepo.findByCategory(category);
    }

    public List<Resource> filterByStatus(Resource.Status status) {
        return resourceRepo.findByStatus(status);
    }

    public List<Resource> filterByType(Resource.ResourceType type) {
        return resourceRepo.findByType(type);
    }

    public List<Resource> filterByTag(String tag) {
        return resourceRepo.findByTag(tag);
    }

    public List<Resource> getTopRated() {
        return resourceRepo.findTopRated();
    }

    // ─── Category operations ──────────────────────────────────────────────────

    public void addCategory(Category category) {
        if (categoryRepo.exists(category.getName())) {
            throw new IllegalArgumentException(
                    "Category already exists: " + category.getName());
        }
        categoryRepo.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public void deleteCategory(String name) {
        categoryRepo.delete(name);
    }

    // ─── Statistics ───────────────────────────────────────────────────────────

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<Resource> all = resourceRepo.findAll();

        stats.put("Total Resources", all.size());
        stats.put("Unread", all.stream().filter(r -> r.getStatus() == Resource.Status.UNREAD).count());
        stats.put("Reading", all.stream().filter(r -> r.getStatus() == Resource.Status.READING).count());
        stats.put("Completed", all.stream().filter(r -> r.getStatus() == Resource.Status.COMPLETED).count());
        stats.put("Archived", all.stream().filter(r -> r.getStatus() == Resource.Status.ARCHIVED).count());
        stats.put("Categories", categoryRepo.count());
        stats.put("By Category", resourceRepo.getCategoryStats());

        OptionalDouble avg = all.stream()
                .filter(r -> r.getRating() > 0)
                .mapToInt(Resource::getRating)
                .average();
        stats.put("Average Rating", avg.isPresent()
                ? String.format("%.1f / 5.0", avg.getAsDouble()) : "N/A");

        return stats;
    }

    // ─── Validation ───────────────────────────────────────────────────────────

    private void validateResource(Resource r) {
        if (r.getTitle() == null || r.getTitle().isBlank()) {
            throw new IllegalArgumentException("Resource title cannot be empty.");
        }
        if (r.getType() == null) {
            throw new IllegalArgumentException("Resource type must be specified.");
        }
    }
}