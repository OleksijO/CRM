package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Language;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.LanguageDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDAOTest extends SpringDaoTests{

    private static final String DEFAULT_NAME = "Default Name";
    private static final String DEFAULT_EMAIL = "default@email.org";
    private static final String DEFAULT_PASSWORD = "DefaultPassword123";
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LanguageDAO languageDAO;
    private Language defaultLanguage;
    private int userTestId;

    @PostConstruct
    public void init() {
        defaultLanguage = languageDAO.getById(1);
    }

    @Before
    public void setUp() {
        userTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (userTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM users WHERE id = " + Integer.toString(userTestId));
            } catch (SQLException e) {
                throw new SQLException("Test User clean up failed!", e);
            }
            userTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        User userTest = new User();
        Assert.assertTrue("User ID before creation must be '0'", userTest.getId() == 0);

        try {
            userTestId = userDAO.insert(userTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty user ID must be '0'", userTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty user ID must be '0'", userTestId == 0);
        }

        userTest.setName(DEFAULT_NAME);
        userTest.setEmail(DEFAULT_EMAIL);
        userTest.setPassword(DEFAULT_PASSWORD);
        userTest.setLanguage(defaultLanguage);
        userTestId = userDAO.insert(userTest);

        Assert.assertNotNull("User creation failed", userTest);
        Assert.assertTrue("User ID after creation must be not '0'", userTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("User read by PK failed", userDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";
        String updatedEmail = "updated@email.org";
        String updatedPassword = "UpdatedPassWord_13";
        Language updatedLanguage = languageDAO.getById(2);
        String updatedPhone = "UpdatedPhone 345-67-89";
        String updatedMobilePhone = "UpdatedMobilePhone 567-89-01";
        String updatedNote = "This is short story\nabout\tUSER";
        String updatedUrl = "http://updated.user.URL";

        User userTest = new User();
        userTest.setName(DEFAULT_NAME);
        userTest.setEmail(DEFAULT_EMAIL);
        userTest.setPassword(DEFAULT_PASSWORD);
        userTest.setLanguage(defaultLanguage);
        userTestId = userDAO.insert(userTest);

        Assert.assertNotNull("User before update must not be null", userTest);
        userTest.setName(updatedName);
        userTest.setEmail(updatedEmail);
        userTest.setPassword(updatedPassword);
        userTest.setLanguage(updatedLanguage);
        userTest.setPhone(updatedPhone);
        userTest.setMobilePhone(updatedMobilePhone);
        userTest.setNote(updatedNote);
        userTest.setUrl(updatedUrl);
        userTest.setImage(updatedUrl.getBytes());
        userDAO.update(userTest);

        User updatedUser = userDAO.getById(userTestId);
        Assert.assertNotNull("User after update is null", updatedUser);
        Assert.assertEquals("User name update failed", updatedName, updatedUser.getName());
        Assert.assertEquals("User email update failed", updatedEmail, updatedUser.getEmail());
        Assert.assertEquals("User password update failed", updatedPassword, updatedUser.getPassword());
        Assert.assertEquals("User language update failed", updatedLanguage.getId(), updatedUser.getLanguage().getId());
        Assert.assertEquals("User phone update failed", updatedPhone, updatedUser.getPhone());
        Assert.assertEquals("User mobile phone update failed", updatedMobilePhone, updatedUser.getMobilePhone());
        Assert.assertEquals("User note update failed", updatedNote, updatedUser.getNote());
        Assert.assertEquals("User URL address update failed", updatedUrl, updatedUser.getUrl());
        Assert.assertArrayEquals("User image update failed", updatedUrl.getBytes(), updatedUser.getImage());
    }

    @Test
    public void testDelete() {
        User userTest = new User();
        userTest.setName(DEFAULT_NAME);
        userTest.setEmail(DEFAULT_EMAIL);
        userTest.setPassword(DEFAULT_PASSWORD);
        userTest.setLanguage(defaultLanguage);
        userTestId = userDAO.insert(userTest);

        List userList = userDAO.getAll();
        int oldListSize = userList.size();
        Assert.assertTrue("User list must not be empty", oldListSize > 0);

        userDAO.delete(userTestId);
        userList = userDAO.getAll();
        Assert.assertEquals("User delete test failed", 1, oldListSize - userList.size());
        Assert.assertNull("User delete test failed", userDAO.getById(userTestId));
    }

    @Test
    public void testGetAll() {
        List userList = userDAO.getAll();
        Assert.assertNotNull("User list must not be null", userList);
        Assert.assertTrue("User list must not be empty", userList.size() > 0);
    }
}
