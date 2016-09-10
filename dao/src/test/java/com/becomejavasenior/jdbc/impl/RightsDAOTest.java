package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Rights;
import com.becomejavasenior.entity.SubjectType;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.RightsDAO;
import com.becomejavasenior.jdbc.UserDAO;
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

public class RightsDAOTest extends SpringDaoTests{
    @Autowired
    private RightsDAO rightsDAO;
    @Autowired
    private UserDAO userDAO;
    private User defaultUser;
    private SubjectType defaultSubjectType = SubjectType.DEAL;
    private int rightsTestId;

    @PostConstruct
    public void init() {
        defaultUser = userDAO.getById(1);
    }

    @Before
    public void setUp() {
        rightsTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (rightsTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM rights WHERE id = " + Integer.toString(rightsTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Rights clean up failed!", e);
            }
            rightsTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Rights rightsTest = new Rights();
        Assert.assertTrue("Rights ID before creation must be '0'", rightsTest.getId() == 0);

        try {
            rightsTestId = rightsDAO.insert(rightsTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty rights ID must be '0'", rightsTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty rights ID must be '0'", rightsTestId == 0);
        }

        rightsTest.setUser(defaultUser);
        rightsTest.setSubjectType(defaultSubjectType);
        rightsTestId = rightsDAO.insert(rightsTest);

        Assert.assertNotNull("Rights creation failed", rightsTest);
        Assert.assertTrue("Rights ID after creation must be not '0'", rightsTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Rights read by PK failed", rightsDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        User updatedUser = userDAO.getById(2);
        SubjectType updatedSubjectType = SubjectType.COMPANY;

        Rights rightsTest = new Rights();
        rightsTest.setUser(defaultUser);
        rightsTest.setSubjectType(defaultSubjectType);
        rightsTestId = rightsDAO.insert(rightsTest);
        Assert.assertNotNull("Rights before update must not be null", rightsTest);

        rightsTest.setUser(updatedUser);
        rightsTest.setSubjectType(updatedSubjectType);
        rightsTest.setChange(true);
        rightsTest.setCreate(true);
        rightsTest.setDelete(true);
        rightsTest.setExport(true);
        rightsTest.setRead(true);
        rightsDAO.update(rightsTest);

        Rights updatedRights = rightsDAO.getById(rightsTestId);
        Assert.assertNotNull("Rights after update is null", updatedRights);
        Assert.assertEquals("Rights user update failed", updatedUser.getId(), updatedRights.getUser().getId());
        Assert.assertEquals("Rights subject type update failed", updatedSubjectType, updatedRights.getSubjectType());

        Assert.assertEquals("Rights for change object update failed", true, updatedRights.isChange());
        Assert.assertEquals("Rights for create object update failed", true, updatedRights.isCreate());
        Assert.assertEquals("Rights for delete object update failed", true, updatedRights.isDelete());
        Assert.assertEquals("Rights for export object update failed", true, updatedRights.isExport());
        Assert.assertEquals("Rights for read object update failed", true, updatedRights.isRead());
    }

    @Test
    public void testDelete() {
        Rights rightsTest = new Rights();
        rightsTest.setUser(defaultUser);
        rightsTest.setSubjectType(defaultSubjectType);
        rightsTestId = rightsDAO.insert(rightsTest);

        List rightsList = rightsDAO.getAll();
        int oldListSize = rightsList.size();
        Assert.assertTrue("Rights list must not be empty", oldListSize > 0);

        rightsDAO.delete(rightsTestId);
        rightsList = rightsDAO.getAll();
        Assert.assertEquals("Rights delete test failed", 1, oldListSize - rightsList.size());
        Assert.assertNull("Rights delete test failed", rightsDAO.getById(rightsTestId));
    }

    @Test
    public void testGetAll() {
        List<Rights> rightsList = rightsDAO.getAll();
        Assert.assertNotNull("Rights list must not be null", rightsList);
        Assert.assertTrue("Rights list must not be empty", rightsList.size() > 0);
    }

    @Test
    public void testGetByUserId() {
        List<Rights> rightsList = rightsDAO.getRightsByUserId(2);
        Assert.assertNotNull("Rights list must not be null", rightsList);
        Assert.assertTrue("Rights list must not be empty", rightsList.size() > 0);
        for (Rights rights : rightsList) {
            Assert.assertEquals("Rights list contains record for another user", 2, rights.getUser().getId());
        }
    }
}
