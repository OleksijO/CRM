package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.jdbc.entity.AuditDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public class LogTransactionTest extends SpringDaoJdbcTemplateImplTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    AuditDAO auditDAO;

    @Test
    public void testSuccessLogOperation() {
        int testAuditsNumber = auditDAO.getAll().size();
        userDAO.getUserById(1);
        Assert.assertEquals("Must be 1 more records", 1, auditDAO.getAll().size() - testAuditsNumber);
    }

    @Test
    @Transactional
    @Commit
    public void testLogOperationWithThrowing() {
        int testAuditsNumber = auditDAO.getAll().size();

        userDAO.getUserById(11246); /// throwable caught by try-catch and returned null.

        Assert.assertEquals("Must be 1 more records", 1, auditDAO.getAll().size() - testAuditsNumber);
    }
}
