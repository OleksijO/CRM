package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Tag;
import com.becomejavasenior.jdbc.ConnectionPool;
import com.becomejavasenior.jdbc.entity.TagDAO;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TagDAOTest {

    private TagDAO tagDAO;
    private static final String DEFAULT_NAME = "Default Name";
    private int tagTestId;

    public TagDAOTest() {
        PostgresDAOFactory factory = new PostgresDAOFactory();
        tagDAO = factory.getTagDAO();
    }

    @Before
    public void setUp() {
        tagTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (tagTestId > 0) {
            try (Connection connection = ConnectionPool.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM tag WHERE id = " + Integer.toString(tagTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Tag clean up failed!", e);
            }
            tagTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Tag tagTest = new Tag();
        Assert.assertTrue("Tag ID before creation must be '0'", tagTest.getId() == 0);

        try {
            tagTestId = tagDAO.insert(tagTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty tag ID must be '0'", tagTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty tag ID must be '0'", tagTestId == 0);
        }

        tagTest.setName(DEFAULT_NAME);
        tagTestId = tagDAO.insert(tagTest);

        Assert.assertNotNull("Tag creation failed", tagTest);
        Assert.assertTrue("Tag ID after creation must be not '0'", tagTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Tag read by PK failed", tagDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";

        Tag tagTest = new Tag();
        tagTest.setName(DEFAULT_NAME);
        tagTestId = tagDAO.insert(tagTest);
        Assert.assertNotNull("Tag before update must not be null", tagTest);

        tagTest.setName(updatedName);

        tagDAO.update(tagTest);

        Tag updatedTag = tagDAO.getById(tagTestId);
        Assert.assertNotNull("Tag after update is null", updatedTag);
        Assert.assertEquals("Tag name update failed", updatedName, updatedTag.getName());
    }

    @Test
    public void testDelete() {
        Tag tagTest = new Tag();
        tagTest.setName(DEFAULT_NAME);
        tagTestId = tagDAO.insert(tagTest);

        List tagList = tagDAO.getAll();
        int oldListSize = tagList.size();
        Assert.assertTrue("Tag list must not be empty", oldListSize > 0);

        tagDAO.delete(tagTestId);
        tagList = tagDAO.getAll();
        Assert.assertEquals("Tag delete test failed", 1, oldListSize - tagList.size());
        Assert.assertNull("Tag delete test failed", tagDAO.getById(tagTestId));
    }

    @Test
    public void testGetAll() {
        List tagList = tagDAO.getAll();
        Assert.assertNotNull("Tag list must not be null", tagList);
        Assert.assertTrue("Tag list must not be empty", tagList.size() > 0);
    }
}