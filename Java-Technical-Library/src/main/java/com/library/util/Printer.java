package com.library.util;

import com.library.model.Resource;

import java.util.List;

/**
 * Utility class for consistent, formatted terminal output.
 * Centralising all print logic here keeps UI code clean.
 */
public class Printer {

    // ANSI colour codes
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String GREEN  = "\u001B[32m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String BLUE   = "\u001B[34m";
    private static final String DIM    = "\u001B[2m";

    public static void banner() {
        System.out.println();
        System.out.println(BOLD + CYAN +
            "  ╔══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN +
            "  ║       TECHNICAL LIBRARY & RESOURCE MANAGER          ║" + RESET);
        System.out.println(BOLD + CYAN +
            "  ║          Organize · Track · Rate · Review            ║" + RESET);
        System.out.println(BOLD + CYAN +
            "  ╚══════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public static void mainMenu() {
        System.out.println();
        System.out.println(BOLD + "  ── MAIN MENU ──────────────────────────────" + RESET);
        System.out.println("  1. Add Resource");
        System.out.println("  2. View All Resources");
        System.out.println("  3. Search");
        System.out.println("  4. Filter Resources");
        System.out.println("  5. Resource Actions  (read / note / delete)");
        System.out.println("  6. Manage Categories");
        System.out.println("  7. Statistics");
        System.out.println("  8. Save Library");
        System.out.println("  9. Save & Exit");
        System.out.println(DIM + "  ─────────────────────────────────────────" + RESET);
    }

    public static void section(String title) {
        System.out.println();
        System.out.println(BOLD + BLUE + "  ── " + title + " " + "─".repeat(
                Math.max(0, 42 - title.length())) + RESET);
    }

    public static void success(String message) {
        System.out.println(GREEN + "  ✔ " + message + RESET);
    }

    public static void warn(String message) {
        System.out.println(YELLOW + "  ⚠ " + message + RESET);
    }

    public static void error(String message) {
        System.out.println(RED + "  ✖ " + message + RESET);
    }

    public static void goodbye() {
        System.out.println();
        System.out.println(BOLD + CYAN + "  Goodbye! Happy reading. 📚" + RESET);
        System.out.println();
    }

    // ─── Resource display ─────────────────────────────────────────────────────

    /** Print a compact table of resources. */
    public static void resourceTable(List<Resource> resources) {
        if (resources.isEmpty()) {
            warn("No resources found.");
            return;
        }

        String fmt = "  %-8s  %-38s  %-15s  %-10s  %-10s  %s%n";
        System.out.printf(BOLD + fmt + RESET,
                "ID-Short", "Title", "Category", "Type", "Status", "Rating");
        System.out.println(DIM + "  " + "─".repeat(100) + RESET);

        for (Resource r : resources) {
            String shortId = r.getId().substring(0, 8);
            String title   = truncate(r.getTitle(), 38);
            String cat     = truncate(r.getCategory() != null ? r.getCategory() : "-", 15);
            String type    = truncate(r.getType() != null ? r.getType().name() : "-", 10);
            String status  = colourStatus(r.getStatus());
            System.out.printf("  %-8s  %-38s  %-15s  %-10s  %-18s  %s%n",
                    shortId, title, cat, type, status, r.getRatingStars());
        }
        System.out.println();
    }

    /** Print a detailed single-resource view. */
    public static void resourceDetail(Resource r) {
        System.out.println(BOLD + "  ── RESOURCE DETAIL " + "─".repeat(28) + RESET);
        field("ID",        r.getId());
        field("Title",     r.getTitle());
        field("Author",    r.getAuthor());
        field("Type",      r.getType() != null ? r.getType().name() : "-");
        field("Category",  r.getCategory());
        field("Tags",      String.join(", ", r.getTags()));
        field("URL",       r.getUrl());
        field("Status",    r.getStatus().name());
        field("Rating",    r.getRatingStars());
        field("Added",     r.getDateAdded() != null ? r.getDateAdded().toString() : "-");
        field("Read on",   r.getDateRead() != null ? r.getDateRead().toString() : "Not read yet");
        System.out.println();
        if (r.getNotes() != null && !r.getNotes().isBlank()) {
            System.out.println(BOLD + "  Notes:" + RESET);
            for (String line : r.getNotes().split("\n")) {
                System.out.println("    " + line);
            }
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    private static void field(String label, String value) {
        System.out.printf("  %-12s %s%n",
                BOLD + label + ":" + RESET,
                value != null && !value.isBlank() ? value : DIM + "-" + RESET);
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String colourStatus(Resource.Status status) {
        return switch (status) {
            case UNREAD    -> YELLOW + "UNREAD"    + RESET;
            case READING   -> CYAN   + "READING"   + RESET;
            case COMPLETED -> GREEN  + "COMPLETED" + RESET;
            case ARCHIVED  -> DIM    + "ARCHIVED"  + RESET;
        };
    }
}