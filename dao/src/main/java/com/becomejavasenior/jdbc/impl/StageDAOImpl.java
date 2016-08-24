package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Stage;
import com.becomejavasenior.jdbc.entity.StageDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.apache.commons.dbcp2.Utils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("stageDao")
public class StageDAOImpl extends AbstractDAO<Stage> implements StageDAO {

    private static final String INSERT_SQL = "INSERT INTO stage_deals (id, name, deleted) " +
            "VALUES ((SELECT CASE WHEN max(id) ISNULL THEN 1 ELSE max(id) + 1 END FROM stage_deals), ?, FALSE)";
    private static final String UPDATE_SQL = "UPDATE stage_deals SET name = ?, deleted = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT id, name FROM stage_deals WHERE (deleted ISNULL OR NOT deleted)";

    private static final String TABLE_NAME = "stage_deals";

    private final String className = getClass().getSimpleName().concat(": ");

    @Override
    public int insert(Stage stage) {

        if (stage.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + stage.getId());
        }

        int id = selectStageByName(stage.getName());
        if (id > 0) {
            stage.setId(id);
            return id;
        }

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, stage.getName());

            if (1 == insertStatement.executeUpdate() && insertStatement.getGeneratedKeys().next()) {
                id = insertStatement.getGeneratedKeys().getInt(FIELD_ID);
                stage.setId(id);
            } else {
                throw new DatabaseException(className + "Can't get stage id from database");
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PREPARING_INSERT + TABLE_NAME, e);
        }
        return id;
    }

    @Override
    public void update(Stage stage) {

        if (stage.getId() == 0) {
            throw new DatabaseException("stage must be created before update");
        }
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, stage.getName());
            statement.setBoolean(2, stage.isDelete());
            statement.setInt(3, stage.getId());
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
    public List<Stage> getAll() {

        try (Connection connection = PostgresDAOFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_SQL)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_SELECT_ALL, e);
        }
    }

    @Override
    public Stage getById(int id) {

        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND id = ?")) {

            statement.setInt(1, id);
            List<Stage> stageList = parseResultSet(statement.executeQuery());
            return stageList == null || stageList.isEmpty() ? null : stageList.get(0);

        } catch (SQLException e) {
            throw new DatabaseException(className + ERROR_SELECT_1, e);
        }
    }

    private List<Stage> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Stage> stageList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Stage stage = new Stage();
                stage.setId(resultSet.getInt(FIELD_ID));
                stage.setName(resultSet.getString(FIELD_NAME));
                stage.setDelete(false);
                stageList.add(stage);
            }
        } catch (Exception e) {
            throw new DatabaseException(className + ERROR_PARSE_RESULT_SET + TABLE_NAME, e);
        }
        return stageList;
    }

    private int selectStageByName(String stageName) {

        if (stageName == null || stageName.isEmpty()) {
            throw new DatabaseException("deal stage name can't be empty");
        }

        int id = 0;
        ResultSet resultSet = null;
        try (Connection connection = PostgresDAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_SQL + " AND name = ?")) {

            statement.setString(1, stageName);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(FIELD_ID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("can't check if stage already exist", e);
        } finally {
            if (resultSet != null) {
                Utils.closeQuietly(resultSet);
            }
        }
        return id;
    }
}
