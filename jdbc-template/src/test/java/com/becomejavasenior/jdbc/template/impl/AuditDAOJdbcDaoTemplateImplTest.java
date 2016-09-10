package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Audit;
import com.becomejavasenior.jdbc.entity.AuditDAO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditDAOJdbcDaoTemplateImplTest extends AbstractSpringDaoJdbcTemplateImplTest {

    private static final String DEFAULT_NAME = "Default Name";
    private static final Date DEFAULT_DATE = new Timestamp(new Date().getTime());
    @Autowired
    private AuditDAO auditDAO;


    @Test
    public void testGetAll() {
        Assert.assertEquals("Must be 4 audits: ", 4, auditDAO.getAll().size());
    }


    @Test
    public void testCreate() throws SQLException, ParseException {
        Audit auditTest = createTestAudit();
        auditTest.setId(0);
        int idTest = auditDAO.insert(auditTest);
        Audit auditFromDB = auditDAO.getById(idTest);
        try {
            //restore DB
            auditDAO.delete(idTest);
            // DB restored
        } catch (Exception e) {
            // Probably DB not restored
            System.out.println("Error whhile deleting test audit from DB.\n" +
                    ">>>> Next error ignored by test!");
            e.printStackTrace();
        }
        Assert.assertNotNull("Audit must be not null", auditTest);
        Assert.assertNotNull("Audit must have ID", idTest);
        Assert.assertTrue("Audit ID after creation must be not '0'", idTest > 0);
        Assert.assertEquals(auditTest, auditFromDB);
    }

    @Test
    public void testDelete() throws ParseException {
        Audit audit = createTestAudit();
        audit.setId(0);
        int id = auditDAO.insert(audit);
        Assert.assertEquals("Database in non consistent mode. Must by 5 audits", 5, auditDAO.getAll().size());
        auditDAO.delete(id);
        Assert.assertEquals("Audit has not been deleted", 4, auditDAO.getAll().size());
    }

    @Test
    public void testUpdate() throws ParseException {
        Audit auditTest = createTestAudit();
        auditTest.setId(0);
        int testId = auditDAO.insert(auditTest);
        Assert.assertEquals("Preparing for test failed.", auditTest, auditDAO.getById(testId));
        auditTest.setMessage("test message");
        auditDAO.update(auditTest);
        Assert.assertEquals("Audit has not been updated", "test message", auditDAO.getById(testId).getMessage());
        auditDAO.delete(testId);
    }


    @Test
    public void testGetByPK() throws ParseException {
        Audit auditTest = createTestAudit();
        assertEquals(auditTest, auditDAO.getById(1));
        Assert.assertNull("Audit doesn't exist, Must bu null", auditDAO.getById(10));
    }

    private void assertEquals(Audit auditTest, Audit auditFromDB) {
        Assert.assertEquals("Audit read by PK not equals to test one", auditTest.getId(), auditFromDB.getId());
        Assert.assertEquals("Audit read by PK not equals to test one", auditTest.getDate(), auditFromDB.getDate());
        Assert.assertEquals("Audit read by PK not equals to test one", auditTest.getMessage(), auditFromDB.getMessage());
        Assert.assertEquals("Audit read by PK not equals to test one", auditTest.getTargetId(), auditFromDB.getTargetId());
        Assert.assertEquals("Audit read by PK not equals to test one", auditTest.isError(), auditFromDB.isError());
    }

    private Audit createTestAudit() throws ParseException {
        Audit auditTest = new Audit();
        auditTest.setId(1);
        auditTest.setTargetId(1);
        auditTest.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 10:00:00"));
        auditTest.setMessage("SomeClass some method called");
        auditTest.setError(false);
        return auditTest;
    }
}
