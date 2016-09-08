package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.jdbc.entity.DashboardDAO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class DashboardDAOJdbcTemplateImplTest extends SpringDaoJdbcTemplateImplTests {
    @Autowired
    private DashboardDAO dashboardDAO;

    @Test
    public void testGetNumberOfDeals() throws Exception {
        Assert.assertEquals(3,dashboardDAO.getNumberOfDeals());
    }

    @Test
    public void testGetTotalBudgetAmount() throws Exception {
        Assert.assertEquals(new BigDecimal (30330.25),dashboardDAO.getTotalBudgetAmount());
    }

    @Test
    public void testGetNumberOfSuccessfulDeals() throws Exception {
        Assert.assertEquals(0,dashboardDAO.getNumberOfSuccessfulDeals());
    }

    @Test
    public void testGetNumberOfFailedDeals() throws Exception {
        Assert.assertEquals(0,dashboardDAO.getNumberOfFailedDeals());
    }

    @Test
    public void testGetNumberOfTaskLessDeals() throws Exception {
        Assert.assertEquals(1,dashboardDAO.getNumberOfTaskLessDeals());
    }

    @Test
    public void testNumberOfDealsWithTasks() throws Exception {
        Assert.assertEquals(2,dashboardDAO.getNumberOfDealsWithTasks());
    }

    @Test
    public void testNumberOfCurrentTasks() throws Exception {
        Assert.assertEquals(2,dashboardDAO.getNumberOfCurrentTasks());
    }

    @Test
    public void testGetNumberOfCompleatedTasks() throws Exception {
        Assert.assertEquals(1,dashboardDAO.getNumberOfCompleatedTasks());
    }

    @Test
    public void testGetNumberOfTermOutTasks() throws Exception {
        Assert.assertEquals(0,dashboardDAO.getNumberOfTermOutTasks());
    }

    @Test
    public void testGetTotalNumberOfContacts() throws Exception {
        Assert.assertEquals(3,dashboardDAO.getTotalNumberOfContacts());
    }

    @Test
    public void testGetTotalNumberOfCompanies() throws Exception {
        Assert.assertEquals(2,dashboardDAO.getTotalNumberOfCompanies());
    }

}
