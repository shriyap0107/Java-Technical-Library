package com.library.repository;

import com.library.model.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory repository for Resource objects.
 * Provides efficient lookup by id and supports filtering by various fields.
 */
public class ResourceRepository implements Repository<Resource, String> {

    // Primary store: id → Resource
    private final Map<String, Resource> store = new LinkedHashMap<>();

    @Override
    public void save(Resource resource) {
        Objects.requireNonNull(resource, "Resource cannot be null.");
        store.put(resource.getId(), resource);
    }

    @Override
    public Optional<Resource> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Resource resource) {
        if (!store.containsKey(resource.getId())) {
            throw new NoSuchElementException("Resource not found: " + resource.getId());
        }
        store.put(resource.getId(), resource);
    }

    @Override
    public void delete(String id) {
        if (store.remove(id) == null) {
            throw new NoSuchElementException("Resource not found: " + id);
        }
    }

    @Override
    public boolean exists(String id) {
        return store.containsKey(id);
    }

    @Override
    public int count() {
        return store.size();
    }

    // ─── Domain-specific queries ─────────────────────────────────────────────

    public List<Resource> findByCategory(String category) {
        return store.values().stream()
                .filter(r -> r.getCategory() != null &&
                             r.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Resource> findByStatus(Resource.Status status) {
        return store.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Resource> findByType(Resource.ResourceType type) {
        return store.values().stream()
                .filter(r -> r.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Resource> findByTag(String tag) {
        String normalised = tag.toLowerCase().trim();
        return store.values().stream()
                .filter(r -> r.getTags().contains(normalised))
                .collect(Collectors.toList());
    }

    /**
     * Case-insensitive full-text search across title, author, notes, and tags.
     */
    public List<Resource> search(String keyword) {
        String kw = keyword.toLowerCase().trim();
        return store.values().stream()
                .filter(r -> contains(r.getTitle(), kw)
                          || contains(r.getAuthor(), kw)
                          || contains(r.getNotes(), kw)
                          || r.getTags().stream().anyMatch(t -> t.contains(kw))
                          || contains(r.getCategory(), kw))
                .collect(Collectors.toList());
    }

    /** Sort all resources by rating descending */
    public List<Resource> findTopRated() {
        return store.values().stream()
                .filter(r -> r.getRating() > 0)
                .sorted(Comparator.comparingInt(Resource::getRating).reversed())
                .collect(Collectors.toList());
    }

    /** Returns category → count mapping */
    public Map<String, Long> getCategoryStats() {
        return store.values().stream()
                .filter(r -> r.getCategory() != null)
                .collect(Collectors.groupingBy(Resource::getCategory, Collectors.counting()));
    }

    private boolean contains(String field, String keyword) {
        return field != null && field.toLowerCase().contains(keyword);
    }
}