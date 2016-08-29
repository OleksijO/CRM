package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Stage;
import com.becomejavasenior.jdbc.SpringDaoTests;
import com.becomejavasenior.jdbc.entity.StageDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class StageDAOTest extends SpringDaoTests{
    @Autowired
    private StageDAO stageDAO;
    private static final String DEFAULT_NAME = "Default Name";
    private int stageTestId;

    @Before
    public void setUp() {
        stageTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (stageTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM stage_deals WHERE id = " + Integer.toString(stageTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Stage clean up failed!", e);
            }
            stageTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Stage stageTest = new Stage();
        Assert.assertTrue("Stage ID before creation must be '0'", stageTest.getId() == 0);

        try {
            stageTestId = stageDAO.insert(stageTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty stage ID must be '0'", stageTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty stage ID must be '0'", stageTestId == 0);
        }

        stageTest.setName(DEFAULT_NAME);
        stageTestId = stageDAO.insert(stageTest);

        Assert.assertNotNull("Stage creation failed", stageTest);
        Assert.assertTrue("Stage ID after creation must be not '0'", stageTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Stage read by PK failed", stageDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";

        Stage stageTest = new Stage();
        stageTest.setName(DEFAULT_NAME);
        stageTestId = stageDAO.insert(stageTest);
        Assert.assertNotNull("Stage before update must not be null", stageTest);

        stageTest.setName(updatedName);

        stageDAO.update(stageTest);

        Stage updatedStage = stageDAO.getById(stageTestId);
        Assert.assertNotNull("Stage after update is null", updatedStage);
        Assert.assertEquals("Stage name update failed", updatedName, updatedStage.getName());
    }

    @Test
    public void testDelete() {
        Stage stageTest = new Stage();
        stageTest.setName(DEFAULT_NAME);
        stageTestId = stageDAO.insert(stageTest);

        List stageList = stageDAO.getAll();
        int oldListSize = stageList.size();
        Assert.assertTrue("Stage list must not be empty", oldListSize > 0);

        stageDAO.delete(stageTestId);
        stageList = stageDAO.getAll();
        Assert.assertEquals("Stage delete test failed", 1, oldListSize - stageList.size());
        Assert.assertNull("Stage delete test failed", stageDAO.getById(stageTestId));
    }

    @Test
    public void testGetAll() {
        List stageList = stageDAO.getAll();
        Assert.assertNotNull("Stage list must not be null", stageList);
        Assert.assertTrue("Stage list must not be empty", stageList.size() > 0);
    }
}