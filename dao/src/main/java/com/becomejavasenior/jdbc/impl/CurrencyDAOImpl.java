package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Currency;
import com.becomejavasenior.jdbc.entity.CurrencyDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl extends AbstractDAO<Currency> implements CurrencyDAO {

    private static final String INSERT_SQL = "INSERT INTO currency (id, name, active, deleted) " +
            "VALUES ((SELECT CASE WHEN max(id) ISNULL THEN 1 ELSE max(id) + 1 END FROM currency), ?, ?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE currency SET name = ?, active = ?, deleted = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT id, name, active FROM currency WHERE (deleted ISNULL OR NOT deleted)";

    private static final String FIELD_ACTIVE = "active";

    private static final String TABLE_NAME = "currency";
    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public int insert(Currency currency) {

        if (currency.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + currency.getId());
        }

        int id;
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, currency.getName());
            insertStatement.setBoolean(2, currency.isActive());

            if (1 == insertStatement.executeUpdate() && insertStatement.getGeneratedKeys().next()) {
                id = insertStatement.getGeneratedKeys().getInt(FIELD_ID);
                currency.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get currency id from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(Currency currency) {

        if (currency.getId() == 0) {
            throw new DatabaseException("contact must be created before update");
        }
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, currency.getName());
            statement.setBoolean(2, currency.isActive());
            statement.setBoolean(3, currency.isDelete());
            statement.setInt(4, currency.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PREPARING_UPDATE + TABLE_NAME, e);
        }
    }

    @Override
    public void delete(int id) {
        delete(id, TABLE_NAME);
    }

    @Override
    public List<Currency> getAll() {

        try (Connection connection = PostgresDAOFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public Currency getById(int id) {

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            List<Currency> currencyList = parseResultSet(statement.executeQuery());
            return currencyList == null || currencyList.isEmpty() ? null : currencyList.get(0);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<Currency> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Currency> currencyList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt(FIELD_ID));
                currency.setName(resultSet.getString(FIELD_NAME));
                currency.setActive(resultSet.getBoolean(FIELD_ACTIVE));
                currency.setDelete(false);
                currencyList.add(currency);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return currencyList;
    }
}
