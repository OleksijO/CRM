package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.jdbc.GenericDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

//import java.util.logging.Level;
//import java.util.logging.Logger;
@Component
abstract class AbstractDAO<T> implements GenericDAO<T> {

    static final String ERROR_PARSE_RESULT_SET = "error while parsing result set for ";
    static final String ERROR_PREPARING_INSERT = "error while preparing INSERT statement for ";
    static final String ERROR_PREPARING_UPDATE = "error while preparing UPDATE statement for ";
    static final String ERROR_ID_MUST_BE_FROM_DBMS = "id must be obtained from DB, cannot create record in ";
    static final String ERROR_GIVEN_ID = ", given id value is: ";
    static final String ERROR_SELECT_ALL = "error while reading all records";
    static final String ERROR_SELECT_1 = "error while reading record by key";

    static final String FIELD_ID = "id";
    static final String FIELD_NAME = "name";
    @Autowired
    private DataSource dataSource;

    @Override
    abstract public int insert(T o);

    @Override
    abstract public void update(T t);

    @Override
    abstract public List<T> getAll();

    @Override
    abstract public T getById(int id);

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void delete(int id, String tableName /*, Logger logger*/) {

        final String DELETE_SQL = "UPDATE " + tableName + " SET deleted = TRUE WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.executeUpdate();

            //logger.log(Level.INFO, "DELETE ENTITY WITH ID = " + id + " FROM TABLE " + tableName);

        } catch (SQLException ex) {
            //logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DatabaseException(ex);
        }
    }


}
