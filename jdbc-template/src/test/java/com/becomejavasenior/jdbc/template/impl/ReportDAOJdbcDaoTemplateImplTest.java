package com.becomejavasenior.jdbc.template.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportDAOJdbcDaoTemplateImplTest extends SpringDaoJdbcTemplateImplTests {

    private static final String DEFAULT_NAME = "Default Name";
    private static final Date DEFAULT_DATE = new Timestamp(new Date().getTime());
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DealDAO dealDAO;
    @Autowired
    private ReportDAO reportDAO;

    private User userForDealTest;
    private Company companyForTest;
    private List<Deal> dealList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        companyForTest = companyDAO.getById(1);
        userForDealTest = userDAO.getById(1);
        companyForTest.setResponsibleUser(userForDealTest);

    }

       @Test
    public void testGetAll() {
        Assert.assertEquals("Must be 2 reports: ", 2, reportDAO.getAll().size());
    }

    @Test
    public void testGetByCompany(){
        Company company1=new Company();
        company1.setId(1);
        Company company2=new Company();
        company2.setId(2);
        Assert.assertEquals(1,reportDAO.getByCompany(company1).size());
        Assert.assertEquals(1,reportDAO.getByCompany(company2).size());
    }

    @Test
    public void testGetByCompanyAndPeriod() throws ParseException {
        Company company1=new Company();
        company1.setId(1);
        Date start1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 10:00:00");
        Date start2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 11:00:00");
        Date finish= new Date();
        Assert.assertEquals(1,reportDAO.getByCompanyAndPeriod(company1,start1,finish).size());
        Assert.assertEquals(0,reportDAO.getByCompanyAndPeriod(company1,start2,finish).size());
    }

    @Test
    public void testGetPeriod() throws ParseException {
        Date start1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 10:00:00");
        Date start2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 11:00:00");
        Date finish= new Date();
        Assert.assertEquals(2,reportDAO.getByPeriod(start1,finish).size());
        Assert.assertEquals(1,reportDAO.getByPeriod(start2,finish).size());
        Assert.assertEquals(0,reportDAO.getByPeriod(new Date (finish.getTime()-1000*3600*24),finish).size());
    }

    @Test
    public void testCreate() throws SQLException {
        Report reportTest = new Report();
        Assert.assertTrue("Report ID before creation must be '0'", reportTest.getId() == 0);
        int reportTestId = 0;
        try {
            reportTestId = reportDAO.insert(reportTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty report ID must be '0'", reportTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty report ID must be '0'", reportTestId == 0);
        }

        Date dateTest = new Date(System.currentTimeMillis());
        BigDecimal amountTest = new BigDecimal(Math.pow(10, 17) - 0.1);
        reportTest.setCompany(companyForTest);
        reportTest.setDate(dateTest);
        reportTest.setHourAmount(amountTest);

        int idTest = 0;
        try {
            reportDAO.insert(reportTest);
            Assert.assertTrue("Must be trown Exception that report must be truncated", false);
        } catch (Exception e) {
            Assert.assertTrue(
                    "Must be trown Exception that report must be trancated",
                    e instanceof DatabaseException);
        }
        dateTest = DateUtils.truncate(dateTest, Calendar.HOUR);
        try {
            reportDAO.insert(reportTest);
            Assert.assertTrue("Must be trown Exception that report begin date is this hour, not previos", false);
        } catch (Exception e) {
            Assert.assertTrue(
                    "Must be trown Exception that report begin date is this hour, not previos",
                    e instanceof DatabaseException);
        }
        dateTest = new Date(dateTest.getTime() - 1000 * 3600);
        reportTest.setDate(dateTest);
        idTest = reportDAO.insert(reportTest);
        try {
            //restore DB
            reportDAO.delete(idTest);
            // DB restored
        } catch (Exception e) {
            // Probably DB not restored
            System.out.println("Error whhile deleting test report from DB.\n" +
                    ">>>> Next error ignored by test!");
            e.printStackTrace();
        }
        Assert.assertNotNull("Report must be not null", reportTest);
        Assert.assertNotNull("Report must have ID", idTest);
        Assert.assertTrue("Report ID after creation must be not '0'", idTest > 0);
    }

    @Test
    public void testDelete() throws ParseException {
        Report report=createTestReport();
        report.setId(0);
        int id=reportDAO.insert(report);
        Assert.assertEquals("Database in non consistent mode. Must by 3 reports", 3, reportDAO.getAll().size());
        reportDAO.delete(id);
        Assert.assertEquals("Report has not been deleted", 2, reportDAO.getAll().size());
    }

    @Test
    public void testUpdate() throws ParseException {
        Report reportTest = createTestReport();
        reportTest.setId(0);
        int testId = reportDAO.insert(reportTest);
        Assert.assertEquals("Preparing for test failed.", reportTest, reportDAO.getReportById(testId));
        BigDecimal newAmount = new BigDecimal(12);
        reportTest.setHourAmount(newAmount);
        reportDAO.update(reportTest);
        Assert.assertEquals("Report has not been updated", newAmount, new BigDecimal(reportDAO.getReportById(testId).getHourAmount().doubleValue()));
        reportDAO.delete(testId);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGetByPK() throws ParseException {
        Report reportTest = createTestReport();
        assertEquals(reportTest, reportDAO.getById(1));
        Assert.assertNull("Report doesn't exist, Must bu null", reportDAO.getById(10));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testGetReportByPK() throws ParseException {
        Report reportTest = createTestReport();
        assertEquals(reportTest, reportDAO.getReportById(1));
        reportDAO.getReportById(10);
    }

    private void assertEquals(Report repTest, Report repFromDB) {
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getId(), repFromDB.getId());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getDate(), repFromDB.getDate());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getHourAmount(), repFromDB.getHourAmount());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getCompany().getId(), repFromDB.getCompany().getId());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getCompany().getName(), repFromDB.getCompany().getName());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getCompany().getResponsibleUser().getId(), repFromDB.getCompany().getResponsibleUser().getId());
        Assert.assertEquals("Report read by PK not equals to test one", repTest.getCompany().getResponsibleUser().getName(), repFromDB.getCompany().getResponsibleUser().getName());
    }

    private Report createTestReport() throws ParseException {
        Report reportTest = new Report();
        reportTest.setCompany(companyForTest);
        reportTest.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-02-11 10:00:00"));
        reportTest.setHourAmount(new BigDecimal(15000.25));
        reportTest.setId(1);
        return reportTest;
    }
}
