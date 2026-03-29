package com.library.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category that groups related resources together.
 * Categories can be created and managed by the user.
 */
public class Category {

    private String name;
    private String description;
    private String color;           // ANSI colour code used in terminal display
    private List<String> resourceIds;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.color = "\u001B[36m"; // default: cyan
        this.resourceIds = new ArrayList<>();
    }

    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public String getColor()             { return color; }
    public List<String> getResourceIds() { return resourceIds; }

    public void setName(String name)             { this.name = name; }
    public void setDescription(String desc)      { this.description = desc; }
    public void setColor(String color)           { this.color = color; }

    public void addResource(String resourceId) {
        if (!resourceIds.contains(resourceId)) {
            resourceIds.add(resourceId);
        }
    }

    public void removeResource(String resourceId) {
        resourceIds.remove(resourceId);
    }

    public int getResourceCount() {
        return resourceIds.size();
    }

    @Override
    public String toString() {
        return String.format("%-20s | %-40s | %d resource(s)",
                name, description, getResourceCount());
    }
}
