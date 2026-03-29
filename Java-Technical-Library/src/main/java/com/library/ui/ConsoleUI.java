package com.library.ui;

import com.library.model.Category;
import com.library.model.Resource;
import com.library.model.Resource.ResourceType;
import com.library.model.Resource.Status;
import com.library.service.DataPersistenceService;
import com.library.service.LibraryService;
import com.library.util.Printer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console-based user interface for the Technical Library Manager.
 * Handles all user interactions through a text menu system.
 */
public class ConsoleUI {

    private final LibraryService libraryService;
    private final DataPersistenceService persistenceService;
    private final Scanner scanner;

    public ConsoleUI(LibraryService libraryService,
                     DataPersistenceService persistenceService) {
        this.libraryService = libraryService;
        this.persistenceService = persistenceService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        Printer.banner();
        loadData();
        mainMenu();
    }

    // ─── Main menu ────────────────────────────────────────────────────────────

    private void mainMenu() {
        boolean running = true;
        while (running) {
            Printer.mainMenu();
            String choice = prompt("Choice").trim();
            switch (choice) {
                case "1" -> addResourceFlow();
                case "2" -> viewAllResources();
                case "3" -> searchFlow();
                case "4" -> filterMenu();
                case "5" -> resourceActionsMenu();
                case "6" -> categoryMenu();
                case "7" -> showStatistics();
                case "8" -> saveData();
                case "9" -> { saveData(); running = false; }
                default  -> Printer.warn("Invalid choice. Please enter 1–9.");
            }
        }
        Printer.goodbye();
    }

    // ─── Add resource ─────────────────────────────────────────────────────────

    private void addResourceFlow() {
        Printer.section("ADD NEW RESOURCE");

        String title = requireInput("Title");

        String author = prompt("Author (leave blank if unknown)");
        if (author.isBlank()) author = "Unknown";

        ResourceType type = pickType();
        String category  = pickCategory();
        String url       = prompt("URL / DOI / File path (optional)");
        String tagsInput = prompt("Tags (comma-separated, optional)");

        Resource resource = new Resource(title, author, type, category, url);
        if (!tagsInput.isBlank()) {
            for (String tag : tagsInput.split(",")) {
                resource.addTag(tag.trim());
            }
        }

        try {
            libraryService.addResource(resource);
            Printer.success("Resource added! ID: " + resource.getId());
        } catch (IllegalArgumentException e) {
            Printer.warn("Could not add resource: " + e.getMessage());
        }
    }

    // ─── View all ─────────────────────────────────────────────────────────────

    private void viewAllResources() {
        List<Resource> all = libraryService.getAllResources();
        if (all.isEmpty()) {
            Printer.warn("Library is empty. Add some resources first.");
            return;
        }
        Printer.section("ALL RESOURCES (" + all.size() + ")");
        Printer.resourceTable(all);
    }

    // ─── Search ───────────────────────────────────────────────────────────────

    private void searchFlow() {
        Printer.section("SEARCH");
        String keyword = requireInput("Enter keyword");
        List<Resource> results = libraryService.search(keyword);
        Printer.section("Results for \"" + keyword + "\" (" + results.size() + " found)");
        Printer.resourceTable(results);
    }

    // ─── Filter menu ─────────────────────────────────────────────────────────

    private void filterMenu() {
        Printer.section("FILTER");
        System.out.println("  1. By Category");
        System.out.println("  2. By Status");
        System.out.println("  3. By Type");
        System.out.println("  4. By Tag");
        System.out.println("  5. Top Rated");
        System.out.println("  0. Back");

        String choice = prompt("Choice");
        List<Resource> results;

        switch (choice.trim()) {
            case "1" -> {
                String cat = requireInput("Category name");
                results = libraryService.filterByCategory(cat);
                Printer.resourceTable(results);
            }
            case "2" -> {
                Status status = pickStatus();
                results = libraryService.filterByStatus(status);
                Printer.resourceTable(results);
            }
            case "3" -> {
                ResourceType type = pickType();
                results = libraryService.filterByType(type);
                Printer.resourceTable(results);
            }
            case "4" -> {
                String tag = requireInput("Tag");
                results = libraryService.filterByTag(tag);
                Printer.resourceTable(results);
            }
            case "5" -> {
                results = libraryService.getTopRated();
                Printer.resourceTable(results);
            }
            case "0" -> { /* back */ }
            default  -> Printer.warn("Invalid choice.");
        }
    }

    // ─── Resource actions ─────────────────────────────────────────────────────

    private void resourceActionsMenu() {
        Printer.section("RESOURCE ACTIONS");
        viewAllResources();
        String id = prompt("Enter resource ID (or 0 to cancel)").trim();
        if (id.equals("0") || id.isBlank()) return;

        libraryService.getResource(id).ifPresentOrElse(resource -> {
            System.out.println();
            Printer.resourceDetail(resource);
            System.out.println();
            System.out.println("  1. Mark as Read / Rate");
            System.out.println("  2. Change Status");
            System.out.println("  3. Add Note");
            System.out.println("  4. Delete");
            System.out.println("  0. Back");

            String choice = prompt("Action");
            switch (choice.trim()) {
                case "1" -> {
                    int rating = readInt("Rating (1–5, 0 to skip)", 0, 5);
                    libraryService.markAsRead(id, rating);
                    Printer.success("Marked as read!");
                }
                case "2" -> {
                    Status status = pickStatus();
                    libraryService.updateStatus(id, status);
                    Printer.success("Status updated.");
                }
                case "3" -> {
                    String note = requireInput("Note");
                    libraryService.addNote(id, note);
                    Printer.success("Note saved.");
                }
                case "4" -> {
                    if (confirm("Are you sure you want to delete this resource?")) {
                        libraryService.deleteResource(id);
                        Printer.success("Deleted.");
                    }
                }
                case "0" -> { /* back */ }
                default  -> Printer.warn("Invalid choice.");
            }
        }, () -> Printer.warn("Resource not found."));
    }

    // ─── Category menu ────────────────────────────────────────────────────────

    private void categoryMenu() {
        Printer.section("CATEGORIES");
        System.out.println("  1. View All Categories");
        System.out.println("  2. Add Category");
        System.out.println("  3. Delete Category");
        System.out.println("  0. Back");

        String choice = prompt("Choice").trim();
        switch (choice) {
            case "1" -> {
                List<Category> cats = libraryService.getAllCategories();
                Printer.section("CATEGORIES (" + cats.size() + ")");
                cats.forEach(c -> System.out.println("  " + c));
            }
            case "2" -> {
                String name = requireInput("Category name");
                String desc = prompt("Description (optional)");
                try {
                    libraryService.addCategory(new Category(name, desc));
                    Printer.success("Category '" + name + "' created.");
                } catch (IllegalArgumentException e) {
                    Printer.warn(e.getMessage());
                }
            }
            case "3" -> {
                String name = requireInput("Category name to delete");
                if (confirm("Delete category '" + name + "'?")) {
                    libraryService.deleteCategory(name);
                    Printer.success("Category deleted.");
                }
            }
            case "0" -> { /* back */ }
            default  -> Printer.warn("Invalid choice.");
        }
    }

    // ─── Statistics ───────────────────────────────────────────────────────────

    private void showStatistics() {
        Printer.section("LIBRARY STATISTICS");
        Map<String, Object> stats = libraryService.getStatistics();
        stats.forEach((k, v) -> {
            if (v instanceof Map<?,?> map) {
                System.out.println("  " + k + ":");
                map.forEach((ck, cv) ->
                        System.out.printf("      %-25s %s%n", ck + ":", cv));
            } else {
                System.out.printf("  %-20s %s%n", k + ":", v);
            }
        });
    }

    // ─── Persistence helpers ──────────────────────────────────────────────────

    private void loadData() {
        try {
            persistenceService.loadFromFile();
        } catch (IOException e) {
            Printer.warn("Could not load saved data: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            persistenceService.saveToFile();
        } catch (IOException e) {
            Printer.warn("Could not save data: " + e.getMessage());
        }
    }

    // ─── Input helpers ────────────────────────────────────────────────────────

    private String prompt(String message) {
        System.out.print("  > " + message + ": ");
        return scanner.nextLine();
    }

    private String requireInput(String message) {
        String input;
        do {
            input = prompt(message).trim();
            if (input.isBlank()) Printer.warn("This field cannot be empty.");
        } while (input.isBlank());
        return input;
    }

    private boolean confirm(String message) {
        System.out.print("  > " + message + " (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("yes") || answer.equals("y");
    }

    private int readInt(String message, int min, int max) {
        while (true) {
            String input = prompt(message).trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) return val;
                Printer.warn("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                Printer.warn("Invalid number.");
            }
        }
    }

    private ResourceType pickType() {
        ResourceType[] types = ResourceType.values();
        System.out.println("  Resource Types:");
        for (int i = 0; i < types.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, types[i]);
        }
        int idx = readInt("Select type (1–" + types.length + ")", 1, types.length);
        return types[idx - 1];
    }

    private Status pickStatus() {
        Status[] statuses = Status.values();
        System.out.println("  Statuses:");
        for (int i = 0; i < statuses.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, statuses[i]);
        }
        int idx = readInt("Select status (1–" + statuses.length + ")", 1, statuses.length);
        return statuses[idx - 1];
    }

    private String pickCategory() {
        List<Category> cats = libraryService.getAllCategories();
        if (!cats.isEmpty()) {
            System.out.println("  Existing categories:");
            for (int i = 0; i < cats.size(); i++) {
                System.out.printf("    %d. %s%n", i + 1, cats.get(i).getName());
            }
            System.out.println("    0. Enter new category");
            String choice = prompt("Select or enter 0 for new").trim();
            try {
                int idx = Integer.parseInt(choice);
                if (idx > 0 && idx <= cats.size()) {
                    return cats.get(idx - 1).getName();
                }
            } catch (NumberFormatException ignored) {}
        }
        return requireInput("Category name");
    }
}