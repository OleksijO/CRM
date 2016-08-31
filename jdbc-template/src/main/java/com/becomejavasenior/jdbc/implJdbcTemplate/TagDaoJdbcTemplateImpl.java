package com.becomejavasenior.jdbc.implJdbcTemplate;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Tag;
import com.becomejavasenior.jdbc.entity.TagDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

//@Repository("tagDao")
public class TagDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplate<Tag> implements TagDAO {

    private static final String INSERT_SQL = "INSERT INTO tag (name, deleted) VALUES (?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE tag SET name = ?, deleted = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT id, name FROM tag WHERE (deleted ISNULL OR NOT deleted)";

    private static final String INSERT_FOR_CONTACT_COMPANY_SQL = "INSERT INTO contact_company_tag " +
            "(tag_id, contact_id, company_id, deleted) VALUES (?, ?, ?, FALSE)";

    private static final String TABLE_NAME = "tag";

    private final String className = getClass().getSimpleName().concat(": ");

    private static final RowMapper<Tag> ROW_MAPPER_TAG = (resultSet, i) -> {
        Tag tag = new Tag();
        tag.setId(resultSet.getInt(FIELD_ID));
        tag.setName(resultSet.getString(FIELD_NAME));
        tag.setDelete(false);
        return tag;
    };

    @Override
    public int insert(Tag tag) {

        if (tag.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + tag.getId());
        }

        PreparedStatementCreator preparedStatementCreator= connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            statement.setString(1, tag.getName());
            return statement;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        int id=(int) keyHolder.getKey();
        tag.setId(id);
        return id;
    }

    @Override
    public void update(Tag tag) {

        if (tag.getId() == 0) {
            throw new DatabaseException("tag must be created before update");
        }

        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setString(1, tag.getName());
            preparedStatement.setBoolean(2, tag.isDelete());
            preparedStatement.setInt(3, tag.getId());
            preparedStatement.executeUpdate();
        };
        jdbcTemplate.update(UPDATE_SQL,preparedStatementSetter);
    }

    @Override
    public void delete(int id) {
        delete(id, TABLE_NAME);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SELECT_SQL, ROW_MAPPER_TAG);
    }

    @Override
    public Tag getById(int id) {

        Tag tag = null;
        try {
            tag = jdbcTemplate.queryForObject(SELECT_SQL + " AND id = ?", ROW_MAPPER_TAG, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
            // to return null if there is no object in DB with specified id. For tests.
        }
        return tag;
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
            int finalFieldId = fieldId;
            int finalId = id;
            int finalObjectId = objectId;
            PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
                preparedStatement.setInt(1, finalId);
                preparedStatement.setObject(2, finalFieldId == 2 ? finalObjectId : null, Types.INTEGER); //contact_id
                preparedStatement.setObject(3, finalFieldId == 3 ? finalObjectId : null, Types.INTEGER); //company_id
                preparedStatement.executeUpdate();
            };
            jdbcTemplate.update(INSERT_FOR_CONTACT_COMPANY_SQL,preparedStatementSetter);
        } else {
            throw new DatabaseException("Can't set relation, parent object is not persist or undefined");
        }
        return id;
    }
}

