package com.library.repository;

import com.library.model.Category;

import java.util.*;

/**
 * In-memory repository for Category objects, keyed by category name.
 */
public class CategoryRepository implements Repository<Category, String> {

    private final Map<String, Category> store = new LinkedHashMap<>();

    @Override
    public void save(Category category) {
        Objects.requireNonNull(category, "Category cannot be null.");
        store.put(category.getName().toLowerCase(), category);
    }

    @Override
    public Optional<Category> findById(String name) {
        return Optional.ofNullable(store.get(name.toLowerCase()));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Category category) {
        String key = category.getName().toLowerCase();
        if (!store.containsKey(key)) {
            throw new NoSuchElementException("Category not found: " + category.getName());
        }
        store.put(key, category);
    }

    @Override
    public void delete(String name) {
        if (store.remove(name.toLowerCase()) == null) {
            throw new NoSuchElementException("Category not found: " + name);
        }
    }

    @Override
    public boolean exists(String name) {
        return store.containsKey(name.toLowerCase());
    }

    @Override
    public int count() {
        return store.size();
    }
}