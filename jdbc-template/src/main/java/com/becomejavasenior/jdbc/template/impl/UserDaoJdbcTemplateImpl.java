package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Audit;
import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.AuditDAO;
import com.becomejavasenior.jdbc.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository("userDao")
@Transactional          // Task 24 Transaction/Propagation
public class UserDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplateImpl<User> implements UserDAO {
    private static final String INSERT_SQL = "INSERT INTO users (name, email, password, is_admin, phone, " +
            "mobile_phone, note, deleted, url, image, language_id) VALUES (?, ?, ?, ?, ?, ?, ?, FALSE, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET name = ?, email = ?, password = ?, is_admin = ?, phone = ?," +
            " mobile_phone = ?, note = ?, deleted = ?, image = ?, url = ?, language_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name, email, password, is_admin, phone, mobile_phone," +
            " note, image, url, language_id FROM users WHERE NOT deleted";

    private static final RowMapper<User> ROW_MAPPER_USER = (resultSet, i) -> {
        User user = new User();
        user.setId(resultSet.getInt(FIELD_ID));
        user.setName(resultSet.getString(FIELD_NAME));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setAdmin(resultSet.getBoolean("is_admin"));
        user.setPhone(resultSet.getString("phone"));
        user.setMobilePhone(resultSet.getString("mobile_phone"));
        user.setNote(resultSet.getString("note"));
        user.setDelete(false);
        user.setImage(resultSet.getBytes("image"));
        user.setUrl(resultSet.getString("url"));
        if (resultSet.getObject("language_id") != null) {
            Language language = new Language();
            language.setId(resultSet.getInt("language_id"));
            user.setLanguage(language);
        }
        return user;
    };

    @Autowired
    private AuditDAO auditDAO;

    @Override
    public int insert(User user) {

        if (user.getId() != 0) {
            throw new DatabaseException("User id must be obtained from DB. User id="+user.getId());
        }

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBoolean(4, user.isAdmin());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getMobilePhone());
            statement.setString(7, user.getNote());
            statement.setString(8, user.getUrl());
            statement.setBytes(9, user.getImage());
            statement.setInt(10, user.getLanguage().getId());
            return statement;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        int id = (int) keyHolder.getKey();
        user.setId(id);
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "users");
    }

    @Override
    public void update(User user) {

        if (user.getId() == 0) {
            throw new DatabaseException("user must be created before update (id=0).");
        }
        PreparedStatementSetter preparedStatementSetter = statement -> {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBoolean(4, user.isAdmin());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getMobilePhone());
            statement.setString(7, user.getNote());
            statement.setBoolean(8, user.isDelete());
            statement.setBytes(9, user.getImage());
            statement.setString(10, user.getUrl());
            statement.setInt(11, user.getLanguage().getId());
            statement.setInt(12, user.getId());
            statement.executeUpdate();
        };
        jdbcTemplate.update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, ROW_MAPPER_USER);
    }

    @Override
    public User getById(int id) {
        final String methodName = "getById method";
        logTransaction(methodName, id, this.getClass() + " " + methodName + " started.");
        User user;
        try {
            user = jdbcTemplate.queryForObject(SELECT_ALL_SQL + " AND id = ?", ROW_MAPPER_USER, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
            // for backward compatibility.
        }
        return user;
    }

    @Override

    public User getUserById(int id) {
        this.logTransaction("getUserById method", id, " was called");
        return jdbcTemplate.queryForObject(SELECT_ALL_SQL + " AND id = ?", ROW_MAPPER_USER, id);
    }


    public void logTransaction(String methodName, int targetId, String message) {
        auditDAO.insert(new Audit(targetId, this.getClass() + " " + methodName + " " + message, new Date()));
    }

    public void setAuditDAO(AuditDAO auditDAO) {
        this.auditDAO = auditDAO;
    }
}