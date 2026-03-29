package com.library;

import com.library.model.Resource;
import com.library.model.Resource.ResourceType;
import com.library.model.Resource.Status;
import com.library.repository.ResourceRepository;

import java.util.List;
import java.util.Optional;

/**
 * Manual test class for ResourceRepository.
 *
 * Run this class directly to verify repository operations without any
 * external test framework — keeping dependencies to zero as per course guidelines.
 *
 * Expected output: all assertions print PASS.
 */
public class ResourceRepositoryTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== ResourceRepository Tests ===\n");

        testSaveAndFindById();
        testFindAll();
        testUpdate();
        testDelete();
        testSearch();
        testFilterByCategory();
        testFilterByStatus();
        testFilterByTag();
        testTopRated();
        testCount();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    // ─── Tests ────────────────────────────────────────────────────────────────

    static void testSaveAndFindById() {
        ResourceRepository repo = new ResourceRepository();
        Resource r = new Resource("Test Paper", "Author A", ResourceType.RESEARCH_PAPER, "ML", "http://test.com");
        repo.save(r);

        Optional<Resource> found = repo.findById(r.getId());
        assertEqual("testSaveAndFindById: should find saved resource", true, found.isPresent());
        assertEqual("testSaveAndFindById: title matches", "Test Paper", found.get().getTitle());
    }

    static void testFindAll() {
        ResourceRepository repo = new ResourceRepository();
        repo.save(new Resource("A", "Auth1", ResourceType.BOOK, "CS", null));
        repo.save(new Resource("B", "Auth2", ResourceType.ARTICLE, "ML", null));

        List<Resource> all = repo.findAll();
        assertEqual("testFindAll: should return 2 resources", 2, all.size());
    }

    static void testUpdate() {
        ResourceRepository repo = new ResourceRepository();
        Resource r = new Resource("Old Title", "Author", ResourceType.TUTORIAL, "Web", null);
        repo.save(r);
        r.setTitle("New Title");
        repo.update(r);

        assertEqual("testUpdate: title should be updated",
                "New Title", repo.findById(r.getId()).get().getTitle());
    }

    static void testDelete() {
        ResourceRepository repo = new ResourceRepository();
        Resource r = new Resource("DeleteMe", "Author", ResourceType.BOOK, "Java", null);
        repo.save(r);
        repo.delete(r.getId());

        assertEqual("testDelete: resource should not exist after deletion",
                false, repo.exists(r.getId()));
    }

    static void testSearch() {
        ResourceRepository repo = new ResourceRepository();
        Resource r = new Resource("Attention Is All You Need", "Vaswani", ResourceType.RESEARCH_PAPER, "NLP", null);
        r.addTag("transformers");
        repo.save(r);

        List<Resource> byTitle  = repo.search("attention");
        List<Resource> byTag    = repo.search("transformers");
        List<Resource> byAuthor = repo.search("vaswani");
        List<Resource> noMatch  = repo.search("zzzzzz");

        assertEqual("testSearch: by title",  1, byTitle.size());
        assertEqual("testSearch: by tag",    1, byTag.size());
        assertEqual("testSearch: by author", 1, byAuthor.size());
        assertEqual("testSearch: no match",  0, noMatch.size());
    }

    static void testFilterByCategory() {
        ResourceRepository repo = new ResourceRepository();
        repo.save(new Resource("P1", "A", ResourceType.BOOK, "Java", null));
        repo.save(new Resource("P2", "B", ResourceType.BOOK, "Java", null));
        repo.save(new Resource("P3", "C", ResourceType.BOOK, "Python", null));

        List<Resource> javaRes = repo.findByCategory("Java");
        assertEqual("testFilterByCategory: Java has 2 resources", 2, javaRes.size());
    }

    static void testFilterByStatus() {
        ResourceRepository repo = new ResourceRepository();
        Resource r1 = new Resource("R1", "A", ResourceType.BOOK, "Cat", null);
        Resource r2 = new Resource("R2", "B", ResourceType.BOOK, "Cat", null);
        r1.setStatus(Status.COMPLETED);
        r2.setStatus(Status.UNREAD);
        repo.save(r1);
        repo.save(r2);

        List<Resource> completed = repo.findByStatus(Status.COMPLETED);
        assertEqual("testFilterByStatus: 1 completed", 1, completed.size());
    }

    static void testFilterByTag() {
        ResourceRepository repo = new ResourceRepository();
        Resource r = new Resource("Deep Learning", "LeCun", ResourceType.BOOK, "ML", null);
        r.addTag("neural-networks");
        repo.save(r);

        List<Resource> tagged = repo.findByTag("neural-networks");
        assertEqual("testFilterByTag: should find 1 tagged resource", 1, tagged.size());
    }

    static void testTopRated() {
        ResourceRepository repo = new ResourceRepository();
        Resource r1 = new Resource("A", "X", ResourceType.BOOK, "Cat", null);
        Resource r2 = new Resource("B", "Y", ResourceType.BOOK, "Cat", null);
        Resource r3 = new Resource("C", "Z", ResourceType.BOOK, "Cat", null);
        r1.setRating(3);
        r2.setRating(5);
        r3.setRating(4);
        repo.save(r1); repo.save(r2); repo.save(r3);

        List<Resource> top = repo.findTopRated();
        assertEqual("testTopRated: first should have rating 5", 5, top.get(0).getRating());
    }

    static void testCount() {
        ResourceRepository repo = new ResourceRepository();
        assertEqual("testCount: empty repo", 0, repo.count());
        repo.save(new Resource("X", "A", ResourceType.ARTICLE, "C", null));
        assertEqual("testCount: after 1 save", 1, repo.count());
    }

    // ─── Assertion helpers ────────────────────────────────────────────────────

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