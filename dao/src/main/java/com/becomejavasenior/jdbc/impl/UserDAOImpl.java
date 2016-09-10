package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.dbcp2.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//@Repository("userDao")
public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {
    private static final String INSERT_SQL = "INSERT INTO users (name, email, password, is_admin, phone, " +
            "mobile_phone, note, deleted, url, image, language_id) VALUES (?, ?, ?, ?, ?, ?, ?, FALSE, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE users SET name = ?, email = ?, password = ?, is_admin = ?, phone = ?," +
            " mobile_phone = ?, note = ?, deleted = ?, image = ?, url = ?, language_id = ? WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, name, email, password, is_admin, phone, mobile_phone," +
            " note, image, url, language_id FROM users WHERE NOT deleted";

    @Override
    public int insert(User user) {

        if (user.getId() != 0) {
            throw new DatabaseException("user id must be obtained from DB");
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

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

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                user.setId(id);
            } else {
                throw new DatabaseException("Can't get user id from database.");
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
        return id;
    }

    @Override
    public void delete(int id) {
        delete(id, "users");
    }

    @Override
    public void update(User user) {

        if (user.getId() == 0) {
            throw new DatabaseException("user must be created before update (id = 0)");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

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
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public List<User> getAll() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
            return parseResultSet(resultSet);
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        }
    }

    @Override
    public User getById(int id) {

        ResultSet resultSet = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            List<User> userList = parseResultSet(resultSet);
            return userList == null || userList.isEmpty() ? null : userList.get(0);

        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        } finally {
            if (resultSet != null) {
                Utils.closeQuietly(resultSet);
            }
        }
    }

    private List<User> parseResultSet(ResultSet resultSet) throws SQLException {

        List<User> userList = new ArrayList<>();

        while (resultSet.next()) {
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
            userList.add(user);
        }
        return userList;
    }


    @Override
    public User getUserById(int id) {
        //TODO implement method
        throw new RuntimeException("Operation is not supported by this implementation yet. Use another one, please.");

    }
}