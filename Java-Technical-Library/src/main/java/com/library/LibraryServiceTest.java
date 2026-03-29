package com.library;

import com.library.model.Resource;
import com.library.model.Resource.ResourceType;
import com.library.model.Resource.Status;
import com.library.repository.CategoryRepository;
import com.library.repository.ResourceRepository;
import com.library.service.LibraryService;

import java.util.List;
import java.util.Map;

/**
 * Manual tests for LibraryService business logic.
 * No external libraries required — all assertions use simple equality checks.
 */
public class LibraryServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== LibraryService Tests ===\n");

        testAddAndGet();
        testDelete();
        testMarkAsRead();
        testUpdateStatus();
        testAddNote();
        testAutoCreateCategory();
        testDuplicateCategory();
        testStatistics();
        testValidationEmptyTitle();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    static LibraryService freshService() {
        return new LibraryService(new ResourceRepository(), new CategoryRepository());
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    static void testAddAndGet() {
        LibraryService s = freshService();
        Resource r = new Resource("Clean Code", "Uncle Bob", ResourceType.BOOK, "Java", null);
        s.addResource(r);

        boolean found = s.getResource(r.getId()).isPresent();
        assertEqual("testAddAndGet: resource should be retrievable", true, found);
    }

    static void testDelete() {
        LibraryService s = freshService();
        Resource r = new Resource("X", "Y", ResourceType.ARTICLE, "Cat", null);
        s.addResource(r);
        s.deleteResource(r.getId());

        boolean gone = s.getResource(r.getId()).isEmpty();
        assertEqual("testDelete: resource should be gone", true, gone);
    }

    static void testMarkAsRead() {
        LibraryService s = freshService();
        Resource r = new Resource("Paper A", "Auth", ResourceType.RESEARCH_PAPER, "ML", null);
        s.addResource(r);
        s.markAsRead(r.getId(), 4);

        Resource updated = s.getResource(r.getId()).get();
        assertEqual("testMarkAsRead: status should be COMPLETED",
                Status.COMPLETED, updated.getStatus());
        assertEqual("testMarkAsRead: rating should be 4", 4, updated.getRating());
    }

    static void testUpdateStatus() {
        LibraryService s = freshService();
        Resource r = new Resource("Paper B", "Auth", ResourceType.RESEARCH_PAPER, "DS", null);
        s.addResource(r);
        s.updateStatus(r.getId(), Status.READING);

        Status status = s.getResource(r.getId()).get().getStatus();
        assertEqual("testUpdateStatus: should be READING", Status.READING, status);
    }

    static void testAddNote() {
        LibraryService s = freshService();
        Resource r = new Resource("Paper C", "Auth", ResourceType.RESEARCH_PAPER, "CS", null);
        s.addResource(r);
        s.addNote(r.getId(), "Very insightful paper.");

        String notes = s.getResource(r.getId()).get().getNotes();
        assertEqual("testAddNote: note should be set", true,
                notes != null && notes.contains("Very insightful"));
    }

    static void testAutoCreateCategory() {
        LibraryService s = freshService();
        Resource r = new Resource("T", "A", ResourceType.BOOK, "NewCat", null);
        s.addResource(r);

        boolean catExists = s.getAllCategories().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase("NewCat"));
        assertEqual("testAutoCreateCategory: category auto-created", true, catExists);
    }

    static void testDuplicateCategory() {
        LibraryService s = freshService();
        com.library.model.Category cat = new com.library.model.Category("Java", "Java resources");
        s.addCategory(cat);

        boolean threw = false;
        try {
            s.addCategory(new com.library.model.Category("Java", "Another"));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assertEqual("testDuplicateCategory: should throw on duplicate", true, threw);
    }

    static void testStatistics() {
        LibraryService s = freshService();
        Resource r1 = new Resource("A", "X", ResourceType.BOOK, "Java", null);
        Resource r2 = new Resource("B", "Y", ResourceType.ARTICLE, "Java", null);
        r1.setStatus(Status.COMPLETED);
        r1.setRating(5);
        s.addResource(r1);
        s.addResource(r2);

        Map<String, Object> stats = s.getStatistics();
        assertEqual("testStatistics: total should be 2", 2L, (long)(int)(Integer) stats.get("Total Resources"));
    }

    static void testValidationEmptyTitle() {
        LibraryService s = freshService();
        boolean threw = false;
        try {
            s.addResource(new Resource("", "Author", ResourceType.BOOK, "Cat", null));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assertEqual("testValidationEmptyTitle: should reject empty title", true, threw);
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    static void assertEqual(String testName, Object expected, Object actual) {
        if (expected.equals(actual)) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName
                    + " | expected: " + expected + " | got: " + actual);
            failed++;
        }
    }
}