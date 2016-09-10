package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.User;
import com.becomejavasenior.entity.VisitHistory;
import com.becomejavasenior.jdbc.VisitHistoryDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("visitHistoryDao")
public class VisitHistoryDAOImpl extends AbstractDAO<VisitHistory> implements VisitHistoryDAO {

    private static final String INSERT_SQL = "INSERT INTO visit_history (users_id, date_create, ip_address," +
            " browser, deleted)\nVALUES (?, ?, ?, ?, false)";
    private static final String UPDATE_SQL = "UPDATE visit_history SET users_id = ?, date_create = ?, ip_address = ?," +
            " browser = ?, deleted = ?\nWHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, users_id, date_create, ip_address, browser\n" +
            "FROM visit_history WHERE NOT deleted";

    private static final String TABLE_NAME = "visit_history";

    private static final String FIELD_USER_ID = "users_id";
    private static final String FIELD_DATE_CREATE = "date_create";
    private static final String FIELD_IP_ADDRESS = "ip_address";
    private static final String FIELD_BROWSER = "browser";

    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public int insert(VisitHistory visitHistory) {

        if (visitHistory.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + visitHistory.getId());
        }
        int id;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, visitHistory.getUser().getId());
            statement.setTimestamp(2, new java.sql.Timestamp(visitHistory.getDateCreate() == null ? System.currentTimeMillis() : visitHistory.getDateCreate().getTime()));
            statement.setString(3, visitHistory.getIpAddress());
            statement.setString(4, visitHistory.getBrowser());

            if (1 == statement.executeUpdate() && statement.getGeneratedKeys().next()) {
                id = statement.getGeneratedKeys().getInt(FIELD_ID);
                visitHistory.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get VisitHistory id from database");
            }
        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(VisitHistory visitHistory) {

        if (visitHistory.getId() == 0) {
            throw new DatabaseException("VisitHistory must be created before update (id = 0)");
        }
        try (Connection connection = getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_SQL)) {

            updateStatement.setInt(1, visitHistory.getUser().getId());
            updateStatement.setTimestamp(2, new java.sql.Timestamp(visitHistory.getDateCreate().getTime()));
            updateStatement.setString(3, visitHistory.getIpAddress());
            updateStatement.setString(4, visitHistory.getBrowser());
            updateStatement.setBoolean(5, visitHistory.isDelete());
            updateStatement.setInt(6, visitHistory.getId());
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
    public List<VisitHistory> getAll() {

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
            return parseResultSet(resultSet);
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public VisitHistory getById(int id) {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + " AND id = ?")) {
            statement.setInt(1, id);
            List<VisitHistory> visitHistoryList = parseResultSet(statement.executeQuery());
            return visitHistoryList == null || visitHistoryList.isEmpty() ? null : visitHistoryList.get(0);
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<VisitHistory> parseResultSet(ResultSet resultSet) throws SQLException {

        List<VisitHistory> visitHistoryList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                VisitHistory visitHistory = new VisitHistory();
                visitHistory.setId(resultSet.getInt(FIELD_ID));
                User user = new User();
                user.setId(resultSet.getInt(FIELD_USER_ID));
                visitHistory.setUser(user);
                visitHistory.setDateCreate(resultSet.getTimestamp(FIELD_DATE_CREATE));
                visitHistory.setIpAddress(resultSet.getString(FIELD_IP_ADDRESS));
                visitHistory.setBrowser(resultSet.getString(FIELD_BROWSER));
                visitHistory.setDelete(false);
                visitHistoryList.add(visitHistory);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return visitHistoryList;
    }
}
