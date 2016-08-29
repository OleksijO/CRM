package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.SpringDaoTests;
import com.becomejavasenior.jdbc.entity.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskDAOTest extends SpringDaoTests{

    private static final String DEFAULT_NAME = "Default Task Name";
    private static final Date DEFAULT_DATE = new Timestamp(new Date().getTime());
    private static final String DEFAULT_TASK_TYPE = "Важно";
    private static final String DEFAULT_TASK_STATUS = "В работе";
    private static final TypeOfPeriod DEFAULT_TASK_PERIOD = TypeOfPeriod.TO_DAY;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    @Qualifier("companyDao")
    private CompanyDAO companyDAO;
    @Autowired
    private ContactDAO contactDAO;
    @Autowired
    private DealDAO dealDAO;
    private User userForTaskTest;
    private int taskTestId;

    @PostConstruct
    public void init() {
        userForTaskTest = userDAO.getById(1);
    }

    @Before
    public void setUp() {
        taskTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (taskTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM task WHERE id = " + Integer.toString(taskTestId));
            } catch (SQLException e) {
                throw new SQLException("Test Task clean up failed!", e);
            }
            taskTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        Task taskTest = new Task();
        Assert.assertTrue("Task ID before creation must be '0'", taskTest.getId() == 0);

        try {
            taskTestId = taskDAO.insert(taskTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty task ID must be '0'", taskTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty task ID must be '0'", taskTestId == 0);
        }

        taskTest.setName(DEFAULT_NAME);
        taskTest.setDateCreate(DEFAULT_DATE);
        taskTest.setCreator(userForTaskTest);
        taskTest.setResponsibleUser(userForTaskTest);
        taskTest.setTaskType(DEFAULT_TASK_TYPE);
        taskTest.setStatus(DEFAULT_TASK_STATUS);
        taskTest.setPeriod(DEFAULT_TASK_PERIOD);
        taskTestId = taskDAO.insert(taskTest);

        Assert.assertNotNull("Task creation failed", taskTest);
        Assert.assertTrue("Task ID after creation must be not '0'", taskTestId > 0);
        Assert.assertNotNull("Task date of creation must be not null", taskTest.getDateCreate());
        Assert.assertNotNull("Task creator must be not null", taskTest.getCreator());
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Task read by PK failed", taskDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedTaskName = "Updated Task Name";
        Timestamp updatedCreateDate = new Timestamp(1L << 41);
        User updatedUser = userDAO.getById(2);
        Company updatedCompany = companyDAO.getById(2);
        Contact updatedContact = contactDAO.getById(2);
        Deal updatedDeal = dealDAO.getById(2);
        String updatedTaskType = "Срочно";
        String updatedTaskStatus = "Выполнено";
        TypeOfPeriod updatedPeriod = TypeOfPeriod.NEXT_MONTH;
        java.util.Date updatedDate = new GregorianCalendar(2013, 11, 25, 0, 0).getTime();
        String updatedTime = "23-30";

        Task taskTest = new Task();
        taskTest.setName(DEFAULT_NAME);
        taskTest.setDateCreate(DEFAULT_DATE);
        taskTest.setCreator(userForTaskTest);
        taskTest.setResponsibleUser(userForTaskTest);
        taskTest.setTaskType(DEFAULT_TASK_TYPE);
        taskTest.setStatus(DEFAULT_TASK_STATUS);
        taskTest.setPeriod(DEFAULT_TASK_PERIOD);
        taskTestId = taskDAO.insert(taskTest);
        Assert.assertNotNull("Task before update must not be null", taskTest);

        taskTest.setName(updatedTaskName);
        taskTest.setDateCreate(updatedCreateDate);
        taskTest.setCreator(updatedUser);
        taskTest.setCompany(updatedCompany);
        taskTest.setContact(updatedContact);
        taskTest.setDeal(updatedDeal);

        taskTest.setResponsibleUser(updatedUser);
        taskTest.setTaskType(updatedTaskType);
        taskTest.setStatus(updatedTaskStatus);
        taskTest.setPeriod(updatedPeriod);
        taskTest.setDateTask(updatedDate);
        taskTest.setTimeTask(updatedTime);

        taskDAO.update(taskTest);

        Task updatedTask = taskDAO.getById(taskTestId);
        Assert.assertNotNull("Task after update is null", updatedTask);
        Assert.assertEquals("Task name update failed", updatedTaskName, updatedTask.getName());
        Assert.assertEquals("Date of task creation update failed", updatedCreateDate, updatedTask.getDateCreate());
        Assert.assertEquals("Task creator update failed", updatedUser.getId(), updatedTask.getCreator().getId());
        Assert.assertEquals("Task link to Company update failed", updatedCompany.getId(), updatedTask.getCompany().getId());
        Assert.assertEquals("Task link to Contact update failed", updatedContact.getId(), updatedTask.getContact().getId());
        Assert.assertEquals("Task link to Deal update failed", updatedDeal.getId(), updatedTask.getDeal().getId());

        Assert.assertEquals("Task responsible user update failed", updatedUser.getId(), updatedTask.getResponsibleUser().getId());
        Assert.assertEquals("Task type update failed", updatedTaskType, updatedTask.getTaskType());
        Assert.assertEquals("Task status update failed", updatedTaskStatus, updatedTask.getStatus());
        Assert.assertEquals("Task period update failed", updatedPeriod, updatedTask.getPeriod());
        Assert.assertEquals("Task date update failed", updatedDate, updatedTask.getDateTask());
        Assert.assertEquals("Task time update failed", updatedTime, updatedTask.getTimeTask());
    }

    @Test
    public void testDelete() {
        Task taskTest = new Task();
        taskTest.setName(DEFAULT_NAME);
        taskTest.setDateCreate(DEFAULT_DATE);
        taskTest.setCreator(userForTaskTest);
        taskTest.setResponsibleUser(userForTaskTest);
        taskTest.setTaskType(DEFAULT_TASK_TYPE);
        taskTest.setStatus(DEFAULT_TASK_STATUS);
        taskTest.setPeriod(DEFAULT_TASK_PERIOD);
        taskTestId = taskDAO.insert(taskTest);

        List taskList = taskDAO.getAll();
        int oldListSize = taskList.size();
        Assert.assertTrue("Task list must not be empty", oldListSize > 0);

        taskDAO.delete(taskTestId);
        taskList = taskDAO.getAll();
        Assert.assertEquals("Task delete test failed", 1, oldListSize - taskList.size());
        Assert.assertNull("Task delete test failed", taskDAO.getById(taskTestId));
    }

    @Test
    public void testGetAll() {
        List taskList = taskDAO.getAll();
        Assert.assertNotNull("Task list must not be null", taskList);
        Assert.assertTrue("Task list must not be empty", taskList.size() > 0);
    }
}
