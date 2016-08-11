package com.becomejavasenior.service;

import java.util.List;

public interface GenericService<T> {
    int insert(T t);

    void update(T t);

    List<T> getAll();

    T getById(int id);

    void delete(int id);
}
