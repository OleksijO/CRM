package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.Currency;
import com.becomejavasenior.jdbc.ConnectionPool;
import com.becomejavasenior.jdbc.entity.CurrencyDAO;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CurrencyDAOTest {

    private CurrencyDAO currencyDAO;
    private static final String DEFAULT_NAME = "Default Name";
    private int currencyTestId;

    public CurrencyDAOTest() {
        PostgresDAOFactory factory = new PostgresDAOFactory();
        currencyDAO = factory.getCurrencyDAO();
    }

    @Before
    public void setUp() {
        currencyTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (currencyTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM currency WHERE id = " + Integer.toString(currencyTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Currency clean up failed!", e);
            }
            currencyTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Currency currencyTest = new Currency();
        Assert.assertTrue("Currency ID before creation must be '0'", currencyTest.getId() == 0);

        try {
            currencyTestId = currencyDAO.insert(currencyTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty currency ID must be '0'", currencyTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty currency ID must be '0'", currencyTestId == 0);
        }

        currencyTest.setName(DEFAULT_NAME);
        currencyTest.setActive(false);
        currencyTestId = currencyDAO.insert(currencyTest);

        Assert.assertNotNull("Currency creation failed", currencyTest);
        Assert.assertTrue("Currency ID after creation must be not '0'", currencyTestId > 0);
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Currency read by PK failed", currencyDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";

        Currency currencyTest = new Currency();
        currencyTest.setName(DEFAULT_NAME);
        currencyTest.setActive(false);
        currencyTestId = currencyDAO.insert(currencyTest);
        Assert.assertNotNull("Currency before update must not be null", currencyTest);

        currencyTest.setName(updatedName);
        currencyTest.setActive(true);

        currencyDAO.update(currencyTest);

        Currency updatedCurrency = currencyDAO.getById(currencyTestId);
        Assert.assertNotNull("Currency after update is null", updatedCurrency);
        Assert.assertEquals("Currency name update failed", updatedName, updatedCurrency.getName());
        Assert.assertEquals("Currency code update failed", true, updatedCurrency.isActive());
    }

    @Test
    public void testDelete() {
        Currency currencyTest = new Currency();
        currencyTest.setName(DEFAULT_NAME);
        currencyTest.setActive(false);
        currencyTestId = currencyDAO.insert(currencyTest);

        List currencyList = currencyDAO.getAll();
        int oldListSize = currencyList.size();
        Assert.assertTrue("Currency list must not be empty", oldListSize > 0);

        currencyDAO.delete(currencyTestId);
        currencyList = currencyDAO.getAll();
        Assert.assertEquals("Currency delete test failed", 1, oldListSize - currencyList.size());
        Assert.assertNull("Currency delete test failed", currencyDAO.getById(currencyTestId));
    }

    @Test
    public void testGetAll() {
        List currencyList = currencyDAO.getAll();
        Assert.assertNotNull("Currency list must not be null", currencyList);
        Assert.assertTrue("Currency list must not be empty", currencyList.size() > 0);
    }
}