package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.User;
import com.becomejavasenior.entity.VisitHistory;
import com.becomejavasenior.jdbc.UserDAO;
import com.becomejavasenior.jdbc.VisitHistoryDAO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class VisitHistoryDAOTest extends SpringDaoTests {
    @Autowired
    private VisitHistoryDAO visitHistoryDAO;
    @Autowired
    private UserDAO userDAO;
    private static final Timestamp DEFAULT_DATE_TIME = new Timestamp(new Date().getTime());
    private User defaultUser;
    private int visitHistoryTestId;

    @PostConstruct
    public void init() {
        defaultUser = userDAO.getById(1);
    }

    @Before
    public void setUp() {
        visitHistoryTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (visitHistoryTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM visit_history WHERE id = " + Integer.toString(visitHistoryTestId));
            } catch (SQLException e) {
                throw new SQLException("Test VisitHistory clean up failed!", e);
            }
            visitHistoryTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        VisitHistory visitHistoryTest = new VisitHistory();
        Assert.assertTrue("VisitHistory ID before creation must be '0'", visitHistoryTest.getId() == 0);

        try {
            visitHistoryTestId = visitHistoryDAO.insert(visitHistoryTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty visitHistory ID must be '0'", visitHistoryTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty visitHistory ID must be '0'", visitHistoryTestId == 0);
        }

        visitHistoryTest.setUser(defaultUser);
        visitHistoryTest.setDateCreate(DEFAULT_DATE_TIME);
        visitHistoryTestId = visitHistoryDAO.insert(visitHistoryTest);

        Assert.assertNotNull("VisitHistory creation failed", visitHistoryTest);
        Assert.assertTrue("VisitHistory ID after creation must be not '0'", visitHistoryTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("VisitHistory read by PK failed", visitHistoryDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        User updatedUser = userDAO.getById(2);
        Timestamp updatedDateTime = new Timestamp(1L << 41);
        String updatedIpAddress = "111.222.025.222";
        String updatedBrowser = "Updated Browser ID String";

        VisitHistory visitHistoryTest = new VisitHistory();
        visitHistoryTest.setUser(defaultUser);
        visitHistoryTest.setDateCreate(DEFAULT_DATE_TIME);
        visitHistoryTestId = visitHistoryDAO.insert(visitHistoryTest);
        Assert.assertNotNull("VisitHistory before update must not be null", visitHistoryTest);

        visitHistoryTest.setUser(updatedUser);
        visitHistoryTest.setDateCreate(updatedDateTime);
        visitHistoryTest.setIpAddress(updatedIpAddress);
        visitHistoryTest.setBrowser(updatedBrowser);
        visitHistoryDAO.update(visitHistoryTest);

        VisitHistory updatedVisitHistory = visitHistoryDAO.getById(visitHistoryTestId);
        Assert.assertNotNull("VisitHistory after update is null", updatedVisitHistory);
        Assert.assertEquals("VisitHistory user update failed", updatedUser.getId(), updatedVisitHistory.getUser().getId());
        Assert.assertEquals("VisitHistory Date update failed", updatedDateTime, updatedVisitHistory.getDateCreate());

        Assert.assertEquals("VisitHistory ip address update failed", updatedIpAddress, updatedVisitHistory.getIpAddress());
        Assert.assertEquals("VisitHistory browser id update failed", updatedBrowser, updatedVisitHistory.getBrowser());
    }

    @Test
    public void testDelete() {
        VisitHistory visitHistoryTest = new VisitHistory();
        visitHistoryTest.setUser(defaultUser);
        visitHistoryTest.setDateCreate(DEFAULT_DATE_TIME);
        visitHistoryTestId = visitHistoryDAO.insert(visitHistoryTest);

        List visitHistoryList = visitHistoryDAO.getAll();
        int oldListSize = visitHistoryList.size();
        Assert.assertTrue("VisitHistory list must not be empty", oldListSize > 0);

        visitHistoryDAO.delete(visitHistoryTestId);
        visitHistoryList = visitHistoryDAO.getAll();
        Assert.assertEquals("VisitHistory delete test failed", 1, oldListSize - visitHistoryList.size());
        Assert.assertNull("VisitHistory delete test failed", visitHistoryDAO.getById(visitHistoryTestId));
    }

    @Test
    public void testGetAll() {
        List<VisitHistory> visitHistoryList = visitHistoryDAO.getAll();
        Assert.assertNotNull("VisitHistory list must not be null", visitHistoryList);
        Assert.assertTrue("VisitHistory list must not be empty", visitHistoryList.size() > 0);
    }
}
