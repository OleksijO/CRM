package com.becomejavasenior.jdbc.template.impl;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.becomejavasenior.jdbc.template.impl.SpringDaoJdbcTemplateImplTests.APPLICATION_CONTEXT_FILE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(APPLICATION_CONTEXT_FILE)

abstract public class SpringDaoJdbcTemplateImplTests {
    public static final String APPLICATION_CONTEXT_FILE = "classpath:dao_test_jdbc_template.xml";

    @Autowired
    private DataSource dataSource;

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @BeforeClass
    public static void initTestDatabase() throws IOException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_FILE);
        DataSource dataSource = (DataSource) ctx.getBean("testDataSource");

        if (ctx == null) throw new RuntimeException("Application context is not initialized");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null) throw new RuntimeException(
                "Test database is not initialized. Check connection or run database_test.sql");
        System.out.println("-------- Locale: "+Locale.getDefault());
        List<Resource> scripts = new ArrayList<>();
        scripts.add(ctx.getResource("classpath:schema.sql"));
        scripts.add(ctx.getResource("classpath:data.sql"));
        scripts.add(ctx.getResource("classpath:patch-01_2016-04-03.sql"));
        scripts.add(ctx.getResource("classpath:patch-02_update_stage_deal.sql"));
        for (Resource script : scripts) {
            System.out.println(script.getFilename() + " " + script.exists() + " " + script.contentLength());
            ScriptUtils.executeSqlScript(connection, script);
        }
        System.out.println();
        System.out.println("===========================================");
        System.out.println("Database has been reset to test conditions.");
        System.out.println("===========================================");
        System.out.println();
    }
}
