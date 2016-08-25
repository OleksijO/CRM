package com.becomejavasenior.jdbc.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.FileDAO;
import com.becomejavasenior.jdbc.factory.PostgresDAOFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class FileDAOTest {

    private static final String DEFAULT_FILENAME = "DefaultFilename.ext";
    private static final Date DEFAULT_DATE = new Timestamp(new Date().getTime());
    private final PostgresDAOFactory factory;
    private FileDAO fileDAO;
    private User userForFileTest;
    private int fileTestId;


    public FileDAOTest() {
        factory = new PostgresDAOFactory();
        userForFileTest = factory.getUserDAO().getById(1);
        fileDAO = factory.getFileDAO();
    }

    @Before
    public void setUp() {
        fileTestId = 0;
    }

    @After
    public void tearDown() throws SQLException {
        if (fileTestId > 0) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("DELETE FROM attached_file WHERE id = " + Integer.toString(fileTestId));
            } catch (SQLException e) {
                throw new SQLException("Test File clean up failed!", e);
            }
            fileTestId = 0;
        }
    }

    @Test
    public void testCreate() {
        File fileTest = new File();
        Assert.assertTrue("File ID before creation must be '0'", fileTest.getId() == 0);

        try {
            fileTestId = fileDAO.insert(fileTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty file ID must be '0'", fileTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty file ID must be '0'", fileTestId == 0);
        }

        fileTest.setFileName(DEFAULT_FILENAME);
        fileTest.setDateCreate(DEFAULT_DATE);
        fileTest.setCreator(userForFileTest);
        fileTestId = fileDAO.insert(fileTest);

        Assert.assertNotNull("File creation failed", fileTest);
        Assert.assertTrue("File ID after creation must be not '0'", fileTestId > 0);
        Assert.assertNotNull("File date of creation must be not null", fileTest.getDateCreate());
        Assert.assertNotNull("File creator must be not null", fileTest.getCreator());
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("File read by PK failed", fileDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "UpdatedFilename.ext";
        Timestamp updatedCreateDate = new Timestamp(1L << 41);
        User userForTestUpdate = factory.getUserDAO().getById(2);
        Company companyForTestUpdate = factory.getCompanyDAO().getById(2);
        Contact contactForTestUpdate = factory.getContactDAO().getById(2);
        Deal dealForTestUpdate = factory.getDealDAO().getById(2);

        File fileTest = new File();
        fileTest.setFileName(DEFAULT_FILENAME);
        fileTest.setDateCreate(DEFAULT_DATE);
        fileTest.setCreator(userForFileTest);
        fileTestId = fileDAO.insert(fileTest);
        Assert.assertNotNull("File before update must not be null", fileTest);

        fileTest.setFileName(updatedName);
        fileTest.setDateCreate(updatedCreateDate);
        fileTest.setCreator(userForTestUpdate);
        fileTest.setCompany(companyForTestUpdate);
        fileTest.setContact(contactForTestUpdate);
        fileTest.setDeal(dealForTestUpdate);
        fileTest.setFileSize(updatedName.length());
        fileTest.setUrlFile(updatedName);
        fileTest.setFile(updatedName.getBytes());

        fileDAO.update(fileTest);

        File updatedFile = fileDAO.getById(fileTestId);
        Assert.assertNotNull("File after update is null", updatedFile);
        Assert.assertEquals("Filename update failed", updatedName, updatedFile.getFileName());
        Assert.assertEquals("Date of file creation update failed", updatedCreateDate, updatedFile.getDateCreate());
        Assert.assertEquals("File creator update failed", userForTestUpdate.getId(), updatedFile.getCreator().getId());
        Assert.assertEquals("File link to Company update failed", companyForTestUpdate.getId(), updatedFile.getCompany().getId());
        Assert.assertEquals("File link to Contact update failed", contactForTestUpdate.getId(), updatedFile.getContact().getId());
        Assert.assertEquals("File link to Deal update failed", dealForTestUpdate.getId(), updatedFile.getDeal().getId());
        Assert.assertEquals("File size update failed", updatedName.length(), updatedFile.getFileSize());
        Assert.assertEquals("File URL update failed", updatedName, updatedFile.getUrlFile());
        Assert.assertArrayEquals("File (bytes) update failed", updatedName.getBytes(), updatedFile.getFile());
    }

    @Test
    public void testDelete() {
        File fileTest = new File();
        fileTest.setFileName(DEFAULT_FILENAME);
        fileTest.setDateCreate(DEFAULT_DATE);
        fileTest.setCreator(userForFileTest);
        fileTestId = fileDAO.insert(fileTest);

        List fileList = fileDAO.getAll();
        int oldListSize = fileList.size();
        Assert.assertTrue("File list must not be empty", oldListSize > 0);

        fileDAO.delete(fileTestId);
        fileList = fileDAO.getAll();
        Assert.assertEquals("File delete test failed", 1, oldListSize - fileList.size());
        Assert.assertNull("File delete test failed", fileDAO.getById(fileTestId));
    }

    @Test
    public void testGetAll() {
        List fileList = fileDAO.getAll();
        Assert.assertNotNull("File list must not be null", fileList);
        Assert.assertTrue("File list must not be empty", fileList.size() > 0);
    }
}
