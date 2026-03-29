package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a technical resource (research paper, article, book, tutorial, etc.)
 * stored in the library.
 */
public class Resource {

    public enum ResourceType {
        RESEARCH_PAPER,
        BOOK,
        ARTICLE,
        TUTORIAL,
        VIDEO,
        DOCUMENTATION,
        OTHER
    }

    public enum Status {
        UNREAD,
        READING,
        COMPLETED,
        ARCHIVED
    }

    private String id;
    private String title;
    private String author;
    private ResourceType type;
    private String category;
    private List<String> tags;
    private String url;
    private String notes;
    private Status status;
    private int rating;           // 1–5, 0 means unrated
    private LocalDate dateAdded;
    private LocalDate dateRead;

    // Default constructor
    public Resource() {
        this.id = UUID.randomUUID().toString();
        this.tags = new ArrayList<>();
        this.status = Status.UNREAD;
        this.rating = 0;
        this.dateAdded = LocalDate.now();
    }

    // Full constructor
    public Resource(String title, String author, ResourceType type,
                    String category, String url) {
        this();
        this.title = title;
        this.author = author;
        this.type = type;
        this.category = category;
        this.url = url;
    }

    // ─── Getters ────────────────────────────────────────────────────────────────

    public String getId()           { return id; }
    public String getTitle()        { return title; }
    public String getAuthor()       { return author; }
    public ResourceType getType()   { return type; }
    public String getCategory()     { return category; }
    public List<String> getTags()   { return tags; }
    public String getUrl()          { return url; }
    public String getNotes()        { return notes; }
    public Status getStatus()       { return status; }
    public int getRating()          { return rating; }
    public LocalDate getDateAdded() { return dateAdded; }
    public LocalDate getDateRead()  { return dateRead; }

    // ─── Setters ────────────────────────────────────────────────────────────────

    public void setId(String id)               { this.id = id; }
    public void setTitle(String title)         { this.title = title; }
    public void setAuthor(String author)       { this.author = author; }
    public void setType(ResourceType type)     { this.type = type; }
    public void setCategory(String category)   { this.category = category; }
    public void setTags(List<String> tags)     { this.tags = tags; }
    public void setUrl(String url)             { this.url = url; }
    public void setNotes(String notes)         { this.notes = notes; }
    public void setStatus(Status status)       { this.status = status; }
    public void setDateAdded(LocalDate date)   { this.dateAdded = date; }
    public void setDateRead(LocalDate date)    { this.dateRead = date; }

    public void setRating(int rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        this.rating = rating;
    }

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank() && !tags.contains(tag.toLowerCase().trim())) {
            tags.add(tag.toLowerCase().trim());
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag.toLowerCase().trim());
    }

    /** Convenience: star string for display (e.g., "★★★☆☆") */
    public String getRatingStars() {
        if (rating == 0) return "Not rated";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            sb.append(i <= rating ? "★" : "☆");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s (%s) | %s | %s",
                type, title, author, category, status, getRatingStars());
    }
}