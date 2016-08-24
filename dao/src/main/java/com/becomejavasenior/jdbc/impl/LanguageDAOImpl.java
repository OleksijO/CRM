package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.jdbc.entity.LanguageDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("languageDao")
public class LanguageDAOImpl extends AbstractDAO<Language> implements LanguageDAO {

    private static final String INSERT_SQL = "INSERT INTO language (id, name, code, deleted) " +
            "VALUES ((SELECT CASE WHEN max(id) ISNULL THEN 1 ELSE max(id) + 1 END FROM language), ?, ?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE language SET name = ?, code = ?, deleted = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT id, name, code FROM language WHERE (deleted ISNULL OR NOT deleted)";

    private static final String FIELD_CODE = "code";

    private static final String TABLE_NAME = "language";
    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public final int insert(Language language) {

        if (language.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + language.getId());
        }

        int id;
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, language.getName());
            insertStatement.setString(2, language.getLanguageCode());

            if (1 == insertStatement.executeUpdate() && insertStatement.getGeneratedKeys().next()) {
                id = insertStatement.getGeneratedKeys().getInt(FIELD_ID);
                language.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get language id from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(Language language) {

        if (language.getId() == 0) {
            throw new DatabaseException("language must be created before update");
        }
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, language.getName());
            statement.setString(2, language.getLanguageCode());
            statement.setBoolean(3, language.isDelete());
            statement.setInt(4, language.getId());
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
    public List<Language> getAll() {

        try (Connection connection = PostgresDAOFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public Language getById(int id) {

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            List<Language> languageList = parseResultSet(statement.executeQuery());
            return languageList == null || languageList.isEmpty() ? null : languageList.get(0);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<Language> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Language> languageList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Language language = new Language();
                language.setId(resultSet.getInt(FIELD_ID));
                language.setName(resultSet.getString(FIELD_NAME));
                language.setLanguageCode(resultSet.getString(FIELD_CODE));
                language.setDelete(false);
                languageList.add(language);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return languageList;
    }
}
