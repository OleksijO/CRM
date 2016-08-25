package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Rights;
import com.becomejavasenior.entity.SubjectType;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.RightsDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("rightsDao")
public class RightsDAOImpl extends AbstractDAO<Rights> implements RightsDAO {

    private static final String INSERT_SQL = "INSERT INTO rights (user_id, subject_type, subject_type_create," +
            " subject_type_read, subject_type_delete, subject_type_change, subject_type_export, deleted)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE rights SET user_id = ?, subject_type = ?," +
            " subject_type_create = ?, subject_type_read = ?, subject_type_delete = ?, subject_type_change = ?," +
            " subject_type_export = ?, deleted = ?\nWHERE id = ?;";
    private static final String SELECT_SQL = "SELECT id, user_id, subject_type, subject_type_create," +
            " subject_type_read, subject_type_delete, subject_type_change, subject_type_export\n" +
            "FROM rights WHERE NOT deleted";

    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_SUBJECT_TYPE = "subject_type";
    private static final String FIELD_SUBJECT_TYPE_CREATE = "subject_type_create";
    private static final String FIELD_SUBJECT_TYPE_READ = "subject_type_read";
    private static final String FIELD_SUBJECT_TYPE_DELETE = "subject_type_delete";
    private static final String FIELD_SUBJECT_TYPE_CHANGE = "subject_type_change";
    private static final String FIELD_SUBJECT_TYPE_EXPORT = "subject_type_export";

    private static final String TABLE_NAME = "rights";

    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public int insert(Rights rights) {

        if (rights.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + rights.getId());
        }

        int id;
        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setObject(1, rights.getUser() == null ? null : rights.getUser().getId(), Types.INTEGER);
            insertStatement.setObject(2, rights.getSubjectType() == null ? null : rights.getSubjectType().getId(), Types.INTEGER);
            insertStatement.setBoolean(3, rights.isCreate());
            insertStatement.setBoolean(4, rights.isRead());
            insertStatement.setBoolean(5, rights.isDelete());
            insertStatement.setBoolean(6, rights.isChange());
            insertStatement.setBoolean(7, rights.isExport());

            if (1 == insertStatement.executeUpdate() && insertStatement.getGeneratedKeys().next()) {
                id = insertStatement.getGeneratedKeys().getInt(FIELD_ID);
                rights.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get rights id from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(Rights rights) {

        if (rights.getId() == 0) {
            throw new DatabaseException("rights must be created before update");
        }
        try (Connection connection = getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL)) {

            updateStatement.setInt(1, rights.getUser().getId());
            updateStatement.setInt(2, rights.getSubjectType().getId());
            updateStatement.setBoolean(3, rights.isCreate());
            updateStatement.setBoolean(4, rights.isRead());
            updateStatement.setBoolean(5, rights.isDelete());
            updateStatement.setBoolean(6, rights.isChange());
            updateStatement.setBoolean(7, rights.isExport());
            updateStatement.setBoolean(8, rights.isDeleted());
            updateStatement.setInt(9, rights.getId());
            updateStatement.executeUpdate();

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PREPARING_UPDATE + TABLE_NAME, e);
        }
    }

    @Override
    public void delete(int id) {
        delete(id, TABLE_NAME);
    }

    @Override
    public List<Rights> getAll() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public Rights getById(int id) {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            List<Rights> rightsList = parseResultSet(statement.executeQuery());
            return rightsList == null || rightsList.isEmpty() ? null : rightsList.get(0);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<Rights> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Rights> rightsList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Rights rights = new Rights();
                rights.setId(resultSet.getInt(FIELD_ID));
                User user = new User();
                user.setId(resultSet.getInt(FIELD_USER_ID));
                rights.setUser(user);
                rights.setSubjectType(SubjectType.getById(resultSet.getInt(FIELD_SUBJECT_TYPE)));
                rights.setCreate(resultSet.getBoolean(FIELD_SUBJECT_TYPE_CREATE));
                rights.setRead(resultSet.getBoolean(FIELD_SUBJECT_TYPE_READ));
                rights.setDelete(resultSet.getBoolean(FIELD_SUBJECT_TYPE_DELETE));
                rights.setChange(resultSet.getBoolean(FIELD_SUBJECT_TYPE_CHANGE));
                rights.setExport(resultSet.getBoolean(FIELD_SUBJECT_TYPE_EXPORT));
                rights.setDeleted(false);
                rightsList.add(rights);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return rightsList;
    }

    @Override
    public List<Rights> getRightsByUserId(int userId) {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND user_id = ?")) {

            statement.setInt(1, userId);
            return parseResultSet(statement.executeQuery());

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL + " for user id = " + userId, e);
        }
    }
}
