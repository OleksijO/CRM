package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Audit;
import com.becomejavasenior.jdbc.AuditDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository("auditDao")
@Transactional          //Task 24 Transactional / Propagation
public class AuditDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplateImpl<Audit> implements AuditDAO {
    private static final String INSERT_SQL = "INSERT INTO audit (target_id ,message, is_error, date_create)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE audit SET target_id = ?, message = ?, is_error = ?, date_create = ?" +
            " WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM audit";

    private final String className = getClass().getSimpleName().concat(": ");

    private static final String TABLE_NAME = "audit";
    private static final String FIELD_TAGET_ID = "target_id";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_IS_ERROR = "is_error";
    private static final String FIELD_DATE_CREATE = "date_create";

    private static final RowMapper<Audit> ROW_MAPPER_AUDIT = (resultSet, i) -> {
        Audit audit = new Audit();
        audit.setId(resultSet.getInt(FIELD_ID));
        audit.setDate(new Date(resultSet.getTimestamp(FIELD_DATE_CREATE).getTime()));
        audit.setError(resultSet.getBoolean(FIELD_IS_ERROR));
        audit.setMessage(resultSet.getString(FIELD_MESSAGE));
        audit.setTargetId(resultSet.getInt(FIELD_TAGET_ID));
        return audit;
    };

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM " + TABLE_NAME + " WHERE id = " + id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int insert(Audit audit) {
        if (audit.getId() != 0) {
            throw new DatabaseException(className + AbstractDaoJdbcTemplateImpl.ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + AbstractDaoJdbcTemplateImpl.ERROR_GIVEN_ID + audit.getId());
        }
        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            statement.setInt(1, audit.getTargetId());
            statement.setString(2, audit.getMessage());
            statement.setBoolean(3, audit.isError());
            statement.setTimestamp(4, new Timestamp(audit.getDate().getTime()));
            return statement;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        int id = (int) keyHolder.getKey();
        audit.setId(id);
        return id;
    }

    @Override
    public void update(Audit audit) {
        if (audit.getId() == 0) {
            throw new DatabaseException("audit must be created before update (id = 0)");
        }

        PreparedStatementSetter preparedStatementSetter = statement -> {
            statement.setInt(1, audit.getTargetId());
            statement.setString(2, audit.getMessage());
            statement.setBoolean(3, audit.isError());
            statement.setTimestamp(4, new Timestamp(audit.getDate().getTime()));
            statement.setInt(5, audit.getId());
        };
        jdbcTemplate.update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public List<Audit> getAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, ROW_MAPPER_AUDIT);
    }

    @Override
    public Audit getById(int id) {
        try {
           return jdbcTemplate.queryForObject(SELECT_ALL_SQL + " WHERE id = ?", ROW_MAPPER_AUDIT, id);
        } catch (EmptyResultDataAccessException ignored) {
            // for backward compatibility
            return null;
        }
    }
}
