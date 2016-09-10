package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Tag;
import com.becomejavasenior.jdbc.TagDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Repository("tagDao")
public class TagDAOImpl extends AbstractDAO<Tag> implements TagDAO {

    private static final String INSERT_SQL = "INSERT INTO tag (name, deleted) VALUES (?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE tag SET name = ?, deleted = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT id, name FROM tag WHERE (deleted ISNULL OR NOT deleted)";

    private static final String INSERT_FOR_CONTACT_COMPANY_SQL = "INSERT INTO contact_company_tag " +
            "(tag_id, contact_id, company_id, deleted) VALUES (?, ?, ?, FALSE)";

    private static final String TABLE_NAME = "tag";

    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public int insert(Tag tag) {

        if (tag.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + tag.getId());
        }

        int id;
        try (Connection connection = getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, tag.getName());

            if (1 == insertStatement.executeUpdate() && insertStatement.getGeneratedKeys().next()) {
                id = insertStatement.getGeneratedKeys().getInt(FIELD_ID);
                tag.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get tag id from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(Tag tag) {

        if (tag.getId() == 0) {
            throw new DatabaseException("tag must be created before update (id = 0)");
        }
        try (Connection connection = getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL)) {

            updateStatement.setString(1, tag.getName());
            updateStatement.setBoolean(2, tag.isDelete());
            updateStatement.setInt(3, tag.getId());
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
    public List<Tag> getAll() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public Tag getById(int id) {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            List<Tag> tagList = parseResultSet(statement.executeQuery());
            return tagList == null || tagList.isEmpty() ? null : tagList.get(0);

        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<Tag> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Tag> tagList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setId(resultSet.getInt(FIELD_ID));
                tag.setName(resultSet.getString(FIELD_NAME));
                tag.setDelete(false);
                tagList.add(tag);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return tagList;
    }

    @Override
    public int insertForCompanyContact(Tag tag, Object object) {
        int id;
        int objectId = 0;
        int fieldId = 0;

        if (object instanceof Contact) {
            objectId = ((Contact) object).getId();
            fieldId = 2;
        } else if (object instanceof Company) {
            objectId = ((Company) object).getId();
            fieldId = 3;
        }

        if (objectId > 0) {
            id = tag.getId();
            if (id == 0) {
                //todo: refactor for support transaction (autocommit=off)
                id = insert(tag);
            }
            try (Connection connection = getConnection();
                 PreparedStatement insertStatement = connection.prepareStatement(INSERT_FOR_CONTACT_COMPANY_SQL)) {

                insertStatement.setInt(1, id);
                insertStatement.setObject(2, fieldId == 2 ? objectId : null, Types.INTEGER); //contact_id
                insertStatement.setObject(3, fieldId == 3 ? objectId : null, Types.INTEGER); //company_id
                insertStatement.executeUpdate();

            } catch (SQLException e) {
                throw new DatabaseException(className + ERROR_PREPARING_INSERT + "contact_company_tag", e);
            }
        } else {
            throw new DatabaseException("Can't set relation, parent object is not persist or undefined");
        }
        return id;
    }
}

