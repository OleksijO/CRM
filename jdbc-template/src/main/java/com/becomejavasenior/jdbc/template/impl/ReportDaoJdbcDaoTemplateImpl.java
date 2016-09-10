package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.ReportDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//@Repository("ReportDao")
public class ReportDaoJdbcDaoTemplateImpl extends JdbcDaoSupport implements ReportDAO {

    private static final String INSERT_SQL = "INSERT INTO report (date, hour_amount, company_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE report SET date = ?, hour_amount = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT \n" +
            "  report.id, \n" +
            "  report.date, \n" +
            "  hour_amount, \n" +
            "  company.id AS company_id, \n" +
            "  company.name AS company_name, \n" +
            "  users.id AS users_id, \n" +
            "  users.name AS users_name\n" +
            "  \n" +
            "FROM report\n" +
            "LEFT JOIN company ON company.id=report.company_id\n" +
            "LEFT JOIN users ON company.responsible_users_id=users.id";

    private static final String FIELD_DATE = "date";
    private static final String FIELD_AMOUNT = "hour_amount";
    private static final String FIELD_COMPANY_ID = "company_id";
    private static final String FIELD_CONPANY_NAME = "company_name";
    private static final String FIELD_CONPANY_RESPONSIBLE_USER_ID = "users_id";
    private static final String FIELD_CONPANY_RESPONSIBLE_NAME = "users_name";
    private static final String TABLE_NAME = "report";

    private final String className = getClass().getSimpleName().concat(": ");

    private static final RowMapper<Report> ROW_MAPPER_REPORT = (resultSet, i) -> {
        User responsibleUser = new User();
        responsibleUser.setId(resultSet.getInt(FIELD_CONPANY_RESPONSIBLE_USER_ID));
        responsibleUser.setName(resultSet.getString(FIELD_CONPANY_RESPONSIBLE_NAME));
        Company company = new Company();
        company.setId(resultSet.getInt(FIELD_COMPANY_ID));
        company.setName(resultSet.getString(FIELD_CONPANY_NAME));
        company.setResponsibleUser(responsibleUser);
        Report report = new Report();
        report.setId(resultSet.getInt(AbstractDaoJdbcTemplateImpl.FIELD_ID));
        report.setDate(new Date(resultSet.getTimestamp(FIELD_DATE).getTime()));
        report.setHourAmount(resultSet.getBigDecimal(FIELD_AMOUNT));
        report.setCompany(company);
        return report;
    };

    @Override
    public int insert(Report report) {

        if (report.getId() != 0) {
            throw new DatabaseException(className + AbstractDaoJdbcTemplateImpl.ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + AbstractDaoJdbcTemplateImpl.ERROR_GIVEN_ID + report.getId());
        }
        Date reportDate = report.getDate();
        if (0 != (reportDate.getTime() - DateUtils.truncate(reportDate, Calendar.HOUR).getTime())) {
            throw new DatabaseException(className+" Report date must be truncated to hour of the begin of report period!");
        }
        if (reportDate.equals(DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR))) {
            throw new DatabaseException(className+" Report period begin date must not be in present hour!");
        }

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            statement.setTimestamp(1, toTimestamp(report.getDate()));
            statement.setBigDecimal(2, report.getHourAmount());
            statement.setInt(3, report.getCompany().getId());
            return statement;
        };
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(preparedStatementCreator, keyHolder);
        int id = (int) keyHolder.getKey();
        report.setId(id);
        return id;
    }

    @Override
    public void update(Report report) {

        if (report.getId() == 0) {
            throw new DatabaseException("report must be created before update (id = 0)");
        }

        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimestamp(DateUtils.truncate(report.getDate(), Calendar.HOUR)));
            preparedStatement.setBigDecimal(2, report.getHourAmount());
            preparedStatement.setInt(3, report.getId());
        };
        getJdbcTemplate().update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public void delete(int id) {
        getJdbcTemplate().update("DELETE FROM " + TABLE_NAME + " WHERE id = " + id);
    }

    @Override
    public List<Report> getAll() {
        return getJdbcTemplate().query(SELECT_SQL, ROW_MAPPER_REPORT);
    }

    @Override
    public Report getById(int id) {
        try {
            return getReportById(id);
        } catch (EmptyResultDataAccessException ignored) {
            // to return null if there is no object in DB with specified id. For tests.
            return null;
        }
    }

    @Override
    public Report getReportById(int id) {
        return getJdbcTemplate().queryForObject(SELECT_SQL + " WHERE report.id = ?", ROW_MAPPER_REPORT, id);
    }

    @Override
    public List<Report> getByCompanyAndPeriod(Company company, Date start, Date end) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimestamp(start));
            preparedStatement.setTimestamp(2, toTimestamp(end));
            preparedStatement.setInt(3, company.getId());
        };
        return getJdbcTemplate().query(SELECT_SQL + " WHERE date >= ? AND date < ? AND company.id = ?",
                preparedStatementSetter,
                ROW_MAPPER_REPORT);
    }

    @Override
    public List<Report> getByPeriod(Date start, Date end) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimestamp(start));
            preparedStatement.setTimestamp(2, toTimestamp(end));
        };
        return getJdbcTemplate().query(SELECT_SQL + " WHERE date >= ? AND date < ?",
                preparedStatementSetter,
                ROW_MAPPER_REPORT);
    }

    @Override
    public List<Report> getByCompany(Company company) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setInt(1, company.getId());

        };
        return getJdbcTemplate().query(SELECT_SQL + " WHERE company.id = ?",
                preparedStatementSetter,
                ROW_MAPPER_REPORT);
    }


    private Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }


}

