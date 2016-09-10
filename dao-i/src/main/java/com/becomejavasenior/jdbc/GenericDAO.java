package com.becomejavasenior.jdbc;

import java.util.List;

public interface GenericDAO<T> {

    int insert(T t);

    void update(T t);

    List<T> getAll();

    /**
     * Returns NULL if there is now entity with specified Id
     */
    T getById(int id);

    void delete(int id);
}
