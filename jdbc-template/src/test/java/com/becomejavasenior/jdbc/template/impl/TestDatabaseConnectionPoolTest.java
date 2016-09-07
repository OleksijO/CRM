package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class TestDatabaseConnectionPoolTest extends SpringDaoJdbcImplTests {

    @Autowired
    DataSource dataSource;

    @Test
    public void openConnectionTest() throws DatabaseException, SQLException {
        Connection connection = getConnection();
        Assert.assertNotNull("connection does not exist", connection);
        Assert.assertFalse("connection must not be closed", connection.isClosed());
        Assert.assertFalse("connection must not be read only", connection.isReadOnly());
        connection.close();
        Assert.assertTrue("finally connection must be closed", connection.isClosed());
    }
}
