package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.jdbc.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
abstract class AbstractDaoJdbcTemplateImpl<T> implements GenericDAO<T> {

    static final String ERROR_ID_MUST_BE_FROM_DBMS = "id must be obtained from DB, cannot create record in ";
    static final String ERROR_GIVEN_ID = ", given id value is: ";

    static final String FIELD_ID = "id";
    static final String FIELD_NAME = "name";
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected AbstractDaoJdbcTemplateImpl() {
    }

    @Override
    abstract public int insert(T o);

    @Override
    abstract public void update(T t);

    @Override
    abstract public List<T> getAll();

    @Override
    abstract public T getById(int id);

    public void delete(int id, String tableName) {
        jdbcTemplate.update("UPDATE " + tableName + " SET deleted = TRUE WHERE id = " + id);
    }
}
