package com.becomejavasenior.jdbc.impl.initDB;

import com.becomejavasenior.jdbc.impl.SpringDaoTests;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * IN this class method initTestDB() clears test database.
 */
public class TestDataSourceInitializer extends SpringDaoTests implements ApplicationContextAware {
    @Autowired
    private DataSource dataSource;
    private ApplicationContext context;

    // BEFORE RUN MAKE SURE TO MANUALLY RUN database_test.sql !!!
    @Test
    public void initTestDB() throws IOException {
        if (context == null) throw new RuntimeException("Application context is not initialized");
        Connection connection = null;
        try {
            connection = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null) throw new RuntimeException(
                "Test database is not initialized. Check connection or run database_test.sql");
        List<Resource> scripts = new ArrayList<>();
        scripts.add(context.getResource("classpath:schema.sql"));
        scripts.add(context.getResource("classpath:data.sql"));
        scripts.add(context.getResource("classpath:patch-01_2016-04-03.sql"));
        scripts.add(context.getResource("classpath:patch-02_update_stage_deal.sql"));
        for (Resource script : scripts) {
            System.out.println(script.getFilename() + " " + script.exists() + " " + script.contentLength());
            ScriptUtils.executeSqlScript(connection, script);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
