package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.jdbc.LanguageDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class LanguageDAOTest extends SpringDaoTests{
    @Autowired
    private LanguageDAO languageDAO;
    private static final String DEFAULT_NAME = "Default Name";
    private static final String DEFAULT_CODE = "DN";
    private int languageTestId;

    @Before
    public void setUp() {
        languageTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (languageTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM language WHERE id = " + Integer.toString(languageTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Language clean up failed!", e);
            }
            languageTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Language languageTest = new Language();
        Assert.assertTrue("Language ID before creation must be '0'", languageTest.getId() == 0);

        try {
            languageTestId = languageDAO.insert(languageTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty language ID must be '0'", languageTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty language ID must be '0'", languageTestId == 0);
        }

        languageTest.setName(DEFAULT_NAME);
        languageTest.setLanguageCode(DEFAULT_CODE);
        languageTestId = languageDAO.insert(languageTest);

        Assert.assertNotNull("Language creation failed", languageTest);
        Assert.assertTrue("Language ID after creation must be not '0'", languageTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Language read by PK failed", languageDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";
        String updatedCode = "UN";

        Language languageTest = new Language();
        languageTest.setName(DEFAULT_NAME);
        languageTest.setLanguageCode(DEFAULT_CODE);
        languageTestId = languageDAO.insert(languageTest);
        Assert.assertNotNull("Language before update must not be null", languageTest);

        languageTest.setName(updatedName);
        languageTest.setLanguageCode(updatedCode);

        languageDAO.update(languageTest);

        Language updatedLanguage = languageDAO.getById(languageTestId);
        Assert.assertNotNull("Language after update is null", updatedLanguage);
        Assert.assertEquals("Language name update failed", updatedName, updatedLanguage.getName());
        Assert.assertEquals("Language code update failed", updatedCode, updatedLanguage.getLanguageCode());
    }

    @Test
    public void testDelete() {
        Language languageTest = new Language();
        languageTest.setName(DEFAULT_NAME);
        languageTest.setLanguageCode(DEFAULT_CODE);
        languageTestId = languageDAO.insert(languageTest);

        List languageList = languageDAO.getAll();
        int oldListSize = languageList.size();
        Assert.assertTrue("Language list must not be empty", oldListSize > 0);

        languageDAO.delete(languageTestId);
        languageList = languageDAO.getAll();
        Assert.assertEquals("Language delete test failed", 1, oldListSize - languageList.size());
        Assert.assertNull("Language delete test failed", languageDAO.getById(languageTestId));
    }

    @Test
    public void testGetAll() {
        List languageList = languageDAO.getAll();
        Assert.assertNotNull("Language list must not be null", languageList);
        Assert.assertTrue("Language list must not be empty", languageList.size() > 0);
    }
}