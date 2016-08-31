package com.becomejavasenior.jdbc.entity;

import java.util.List;

public interface GenericDAO<T> {
    int insert(T t);
    void update(T t);
    List<T> getAll();
    T getById(int id);
    void delete(int id);
}
