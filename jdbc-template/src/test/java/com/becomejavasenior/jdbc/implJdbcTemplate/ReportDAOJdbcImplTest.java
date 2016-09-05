package com.becomejavasenior.jdbc.implJdbcTemplate;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDAOJdbcImplTest extends SpringDaoJdbcImplTests {

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

    /*@Test
    public void testGetDealsAmountForPreviousHour() {
        final int maxCompanies = 3;  // DO NOT CHANGE VALUE. MUST BE = 3.
        UtilsForDaoTests utilsForDaoTests = new UtilsForDaoTests();
        BigDecimal[] controlAmounts =
                utilsForDaoTests.setUpDatabaseForReportDaoTestGetDealsAmountForPreviousHour(maxCompanies, dealDAO, companyDAO);

        List<Report> listTest = reportDAO.makeReportsWithDealsAmountForPreviousHour();

        utilsForDaoTests.tearDownForReportDaoTestGetDealsAmountForPreviousHour(maxCompanies, dealDAO, companyDAO);

        Assert.assertEquals("Quantity of companies in reports: ", 3, listTest.size());
        BigDecimal testAmount[] = new BigDecimal[3];
        testAmount[0] = new BigDecimal(0);
        testAmount[1] = new BigDecimal(0);
        testAmount[2] = new BigDecimal(0);
        for (Report report : listTest) {
            final String name = report.getCompany().getName();
            final BigDecimal amount1 = report.getHourAmount();
            System.out.println(report);
            switch (name) {
                case "Company 0":
                    testAmount[0].add(amount1);
                    break;
                case "Company 1":
                    testAmount[1].add(amount1);
                    break;
                case "Company 2":
                    testAmount[2].add(amount1);
                    break;
                default:
                    throw new RuntimeException("Can't be !");
            }
        }
        Assert.assertArrayEquals(controlAmounts, testAmount);
    }*/


    @Test
    public void testCreate() {
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
    public void testGetByPK() {
        Report reportTest = new Report();
        Assert.assertTrue("Report ID before creation must be '0'", reportTest.getId() == 0);
        int reportTestId = 0;
        Date dateTest = new Date(System.currentTimeMillis());
        BigDecimal amountTest = new BigDecimal(Math.pow(10, 17) - 0.1);
        reportTest.setCompany(companyForTest);
        reportTest.setDate(dateTest);
        reportTest.setHourAmount(amountTest);
        reportTestId = reportDAO.insert(reportTest);
        System.out.println("Report of id must be id=" + reportTestId);
        Report reportFromDB = reportDAO.getById(reportTestId);
        try {
            //restore DB
            reportDAO.delete(reportTestId);
            // DB restored
        } catch (Exception e) {
            // Probably DB not restored
            System.out.println("Error whhile deleting test report from DB.\n" +
                    ">>>> Next error ignored by test!");
            e.printStackTrace();
        }
        Assert.assertEquals("Report read by PK not equels to test one", reportTest, reportFromDB);
    }

    @Test
    public void testGetReportByPK() {
        //TODO compare new report and from database
        //TODO check exceprion if not exist
    }
/*
    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";
        Timestamp updatedCreateDate = new Timestamp(1L << 41);
        User updatedUser = userDAO.getById(2);
        Company updatedCompany = companyDAO.getById(2);
        Stage updatedStage = stageDAO.getById(2);
        Contact updatedContact = contactDAO.getById(2);

        Report reportTest = new Report();
        reportTest.setName(DEFAULT_NAME);
        reportTest.setDateCreate(DEFAULT_DATE);
        reportTest.setCreator(userForReportTest);
        reportTest.setCompany(companyForReportTest);
        reportTest.setStage(stageForReportTest);
        reportTestId = reportDAO.insert(reportTest);
        Assert.assertNotNull("Report before update must not be null", reportTest);

        reportTest.setName(updatedName);
        reportTest.setDateCreate(updatedCreateDate);
        reportTest.setCreator(updatedUser);
        reportTest.setResponsibleUser(updatedUser);
        reportTest.setCompany(updatedCompany);
        reportTest.setStage(updatedStage);
        reportTest.setAmount(BigDecimal.valueOf(500.55));
        reportTest.setPrimaryContact(updatedContact);

        reportDAO.update(reportTest);

        Report updatedReport = reportDAO.getById(reportTestId);
        Assert.assertNotNull("Report after update is null", updatedReport);
        Assert.assertEquals("Report name update failed", updatedName, updatedReport.getName());
        Assert.assertEquals("Date of report creation update failed", updatedCreateDate, updatedReport.getDateCreate());
        Assert.assertEquals("Report creator update failed", updatedUser.getId(), updatedReport.getCreator().getId());
        Assert.assertEquals("Report responsible user update failed", updatedUser.getId(), updatedReport.getResponsibleUser().getId());
        Assert.assertEquals("Report link to Company update failed", updatedCompany.getId(), updatedReport.getCompany().getId());
        Assert.assertEquals("Report link to Stage update failed", updatedStage.getId(), updatedReport.getStage().getId());
        Assert.assertEquals("getAmount: ", BigDecimal.valueOf(500.55), updatedReport.getAmount());
        Assert.assertEquals("Report link to Primary Contact update failed", updatedContact.getId(), updatedReport.getPrimaryContact().getId());
    }

    @Test
    public void testDelete() {
        Report reportTest = new Report();
        reportTest.setName(DEFAULT_NAME);
        reportTest.setDateCreate(DEFAULT_DATE);
        reportTest.setCreator(userForReportTest);
        reportTest.setCompany(companyForReportTest);
        reportTest.setStage(stageForReportTest);
        reportTestId = reportDAO.insert(reportTest);

        List reportList = reportDAO.getAll();
        int oldListSize = reportList.size();
        Assert.assertTrue("Report list must not be empty", oldListSize > 0);

        reportDAO.delete(reportTestId);
        reportList = reportDAO.getAll();
        Assert.assertEquals("Report delete test failed", 1, oldListSize - reportList.size());
        Assert.assertNull("Report delete test failed", reportDAO.getById(reportTestId));
    }

    @Test
    public void testGetAll() {
        List reportList = reportDAO.getAll();
        Assert.assertNotNull("Report list must not be null", reportList);
        Assert.assertTrue("Report list must not be empty", reportList.size() > 0);
    }*/
}
