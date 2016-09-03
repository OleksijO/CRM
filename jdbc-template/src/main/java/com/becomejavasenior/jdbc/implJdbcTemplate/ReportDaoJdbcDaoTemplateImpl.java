package com.becomejavasenior.jdbc.implJdbcTemplate;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.ReportDAO;
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

import static com.becomejavasenior.jdbc.implJdbcTemplate.AbstractDaoJdbcTemplate.*;

//@Repository("ReportDao")
public class ReportDaoJdbcDaoTemplateImpl extends JdbcDaoSupport implements ReportDAO {

    private static final String INSERT_SQL = "INSERT INTO report (date, hour_amount, company_id) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE report SET date = ?, hour_amount = ? WHERE id = ?";
    private static final String SELECT_SQL = "SELECT \n" +
            "  report.id, \n" +
            "  date, \n" +
            "  hour_amount, \n" +
            "  company.id AS company_id, \n" +
            "  company.name AS company_name, \n" +
            "  users.id AS users_id, \n" +
            "  users.name AS users_name\n" +
            "  \n" +
            "FROM report\n" +
            "LEFT JOIN company ON company.id=report.company_id\n" +
            "LEFT JOIN users ON company.responsible_users_id=users.id";

    private static final String FIELD_DATE = "date_create";
    private static final String FIELD_AMOUNT = "hour_amount";
    private static final String FIELD_COMPANY_ID = "id";
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
        report.setId(resultSet.getInt(FIELD_ID));
        report.setHourAmount(resultSet.getBigDecimal(FIELD_AMOUNT));
        report.setCompany(company);
        return report;
    };

    private static final RowMapper<Report> ROW_MAPPER_REPORT_BY_ID = (resultSet, i) -> {
        User responsibleUser = new User();
        responsibleUser.setId(resultSet.getInt(FIELD_CONPANY_RESPONSIBLE_USER_ID));
        responsibleUser.setName(resultSet.getString(FIELD_CONPANY_RESPONSIBLE_NAME));
        Company company = new Company();
        company.setId(resultSet.getInt(FIELD_COMPANY_ID));
        company.setName(resultSet.getString(FIELD_CONPANY_NAME));
        company.setResponsibleUser(responsibleUser);
        Report report = new Report();
        report.setId(resultSet.getInt(FIELD_ID));
        report.setDate(resultSet.getDate(FIELD_DATE));
        report.setHourAmount(resultSet.getBigDecimal(FIELD_AMOUNT));
        report.setCompany(company);
        return report;
    };

    @Override
    public int insert(Report report) {

        if (report.getId() != 0) {
            throw new DatabaseException(className + ERROR_ID_MUST_BE_FROM_DBMS + TABLE_NAME + ERROR_GIVEN_ID + report.getId());
        }

        PreparedStatementCreator preparedStatementCreator = connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            statement.setTimestamp(1, toTimeStamp(DateUtils.truncate(report.getDate(), Calendar.HOUR)));
            statement.setBigDecimal(2, report.getHourAmount());
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
            throw new DatabaseException("report must be created before update");
        }

        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimeStamp(DateUtils.truncate(report.getDate(), Calendar.HOUR)));
            preparedStatement.setBigDecimal(2, report.getHourAmount());
            preparedStatement.setInt(3, report.getId());
        };
        getJdbcTemplate().update(UPDATE_SQL, preparedStatementSetter);
    }

    @Override
    public void delete(int id) {
        getJdbcTemplate().update("UPDATE " + TABLE_NAME + " SET deleted = TRUE WHERE id = " + id);
    }

    @Override
    public List<Report> getAll() {
        return getJdbcTemplate().query(SELECT_SQL, ROW_MAPPER_REPORT);
    }

    @Override
    public Report getById(int id) {

        Report report = null;
        try {
            report = getJdbcTemplate().queryForObject(SELECT_SQL + " WHERE report.id = ?", ROW_MAPPER_REPORT, id);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
            // to return null if there is no object in DB with specified id. For tests.
        }
        return report;
    }

    @Override
    public List<Report> getByDateAndCompany(Date date, Company company) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimeStamp(DateUtils.truncate(date, Calendar.HOUR)));
            preparedStatement.setInt(2, company.getId());

        };
        return getJdbcTemplate().query(SELECT_SQL + " WHERE date = ? AND company.id = ?",
                preparedStatementSetter,
                ROW_MAPPER_REPORT);
    }

    @Override
    public List<Report> getByDate(Date date) {
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, toTimeStamp(DateUtils.truncate(date, Calendar.HOUR)));
        };
        return getJdbcTemplate().query(SELECT_SQL + " WHERE date = ?",
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

    @Override
    public List<Report> getDealsAmountForPreviousHour() {
        Date date = new Date(System.currentTimeMillis());
        Date finish = DateUtils.truncate(date, Calendar.HOUR);
        Date start = DateUtils.truncate(new Date(finish.getTime() - 1000 * 1800), Calendar.HOUR);
        PreparedStatementSetter preparedStatementSetter = preparedStatement -> {
            preparedStatement.setTimestamp(1, new Timestamp(start.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(finish.getTime()));
        };
        List<Report> reportList = getJdbcTemplate().query(" SELECT SUM(deal.amount) AS hour_amount,\n" +
                "  company.id,\n" +
                "  company.name AS company_name,\n" +
                "  users.id AS users_id,\n" +
                "  users.name AS users_name\n" +
                "  " +
                "FROM deal\n" +
                "LEFT JOIN company ON company.id=deal.company_id\n" +
                "LEFT JOIN users ON company.responsible_users_id=users.id\n" +
                "WHERE deal.date_create >= ? AND deal.date_create < ?\n" +
                "GROUP BY company.id, company_name, users_id, users_name", preparedStatementSetter, ROW_MAPPER_REPORT);
        for(Report report:reportList){
            report.setDate(new Date(finish.getTime()));

        }
        return reportList;
    }

    private Timestamp toTimeStamp(Date date) {
        return new Timestamp(date.getTime());
    }

}

