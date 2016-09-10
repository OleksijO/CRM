package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.jdbc.DashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository("dashboardDao")
public class DashboardDaoJdbcTemplate implements DashboardDAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private int getCount(String sql) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return (count == null) ? 0 : count;
    }

    @Override
    public int getNumberOfDeals() {
        return getCount("SELECT COUNT(id) FROM deal;");
    }

    @Override
    public BigDecimal getTotalBudgetAmount() {
        return jdbcTemplate.queryForObject("SELECT SUM(amount) FROM deal", BigDecimal.class);
    }

    @Override
    public int getNumberOfSuccessfulDeals() {
        return getCount(
                "SELECT SUM(id) FROM deal " +
                        "WHERE stage_id = " +
                        "(SELECT id FROM stage_deals WHERE name = 'Успешно реализовано');");
    }

    @Override
    public int getNumberOfFailedDeals() {
        return getCount("SELECT COUNT(id) FROM deal " +
                "WHERE stage_id = " +
                "(SELECT id FROM stage_deals WHERE name = 'Закрыто и нереализовано');");
    }

    @Override
    public int getNumberOfTaskLessDeals() {
        return getCount("SELECT COUNT(deal.id) FROM deal " +
                "WHERE deal.responsible_users_id IN " +
                "(" +
                "SELECT users.id FROM users where users.id NOT IN " +
                "(SELECT task.responsible_users_id FROM task)" +
                ");");
    }

    @Override
    public int getNumberOfDealsWithTasks() {
        return getCount("SELECT COUNT(deal.id) FROM deal " +
                "WHERE deal.responsible_users_id IN " +
                "(" +
                "SELECT users.id FROM users where users.id IN " +
                "(SELECT task.responsible_users_id FROM task)" +
                ");");
    }

    @Override
    public int getNumberOfCurrentTasks() {
        return getCount("SELECT COUNT(task.id) FROM task " +
                "WHERE status_id = " +
                "(SELECT task_status.id FROM task_status WHERE name = 'В работе');");
    }

    @Override
    public int getNumberOfCompleatedTasks() {
        return getCount("SELECT COUNT(task.id) FROM task " +
                "WHERE status_id = " +
                "(SELECT task_status.id FROM task_status WHERE name = 'Выполнено');");
    }

    @Override
    public int getNumberOfTermOutTasks() {
        return getCount("SELECT COUNT(task.id) FROM task " +
                "WHERE status_id = " +
                "(SELECT task_status.id FROM task_status WHERE name = 'Приостановлено');");
    }

    @Override
    public int getTotalNumberOfContacts() {
        return getCount("SELECT COUNT(contact.id) FROM contact;");
    }

    @Override
    public int getTotalNumberOfCompanies() {
        return getCount("SELECT COUNT(company.id) FROM company;");
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

