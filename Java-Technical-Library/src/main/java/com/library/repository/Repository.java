package com.library.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface defining standard CRUD operations.
 * Using generics keeps every concrete repository consistent.
 *
 * @param <T>  entity type
 * @param <ID> identifier type
 */
public interface Repository<T, ID> {

    void save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void update(T entity);

    void delete(ID id);

    boolean exists(ID id);

    int count();
}