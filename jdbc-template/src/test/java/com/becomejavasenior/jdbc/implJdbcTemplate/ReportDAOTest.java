package com.becomejavasenior.jdbc.implJdbcTemplate;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportDAOTest extends SpringDaoTests {

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
    private BigDecimal amount[] = new BigDecimal[3];


    private User userForDealTest;
    private Stage stageForDealTest;
    private Company companyForDealTest;
    private int dealTestId;
    private List<Deal> dealList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();
    private int maxDeals = 9;
    private int maxCompanies = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");


    @PostConstruct
    public void init() {


    }

    @Before

    public void setUp() throws ParseException {
        System.out.println(">>>>>>>>>>>>>>>  SET UP   >>>>>>>>>>>>>>>>>");
        userForDealTest = userDAO.getById(1);
        amount[0] = new BigDecimal(0);
        amount[1] = new BigDecimal(0);
        amount[2] = new BigDecimal(0);
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyDAO.getById(1);
            company.setId(0);
            company.setResponsibleUser(userForDealTest);
            company.setName("Company " + i);
            company.setId(companyDAO.insert(company));
            System.out.println("Added: " + company.toString());
            companyList.add(company);
        }
        System.out.println(new Date(System.currentTimeMillis()));
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealDAO.getById(1);
            deal.setId(0);
            Date date = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR);
            deal.setDateCreate(new Date(date.getTime() - 1000 * 60 * (i + 1)));
            deal.setAmount(new BigDecimal((Math.pow(10, i % 3) + i) * i));
            deal.setCompany(companyList.get(i % 3));
            deal= dealDAO.getById(dealDAO.insert(deal));
            deal.setCompany(companyList.get(i % 3));
            System.out.println("Added: " + deal.toString() + " | " + deal.getDate() + " | " + deal.getCompany().toString());
            amount[i % 3].add(deal.getAmount());
            dealList.add(deal);
        }
        System.out.println(">>>>>>>>>>  SET UP  >>>>> DONE !  ");
    }

    @After

    public void tearDown() throws SQLException {
        System.out.println(">>>>>>>>>>>>>>>  TEAR DOWN >>>>>>>>>>>>>>>>>>");
        List<Deal> dealsTODEleteList = new ArrayList<>();
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealList.get(i);
            dealDAO.delete(deal.getId());
            if (dealDAO.getById(deal.getId()) == null) {
                System.out.println("Deleted: " + deal.toString());
                dealsTODEleteList.add(deal);

            }
        }
        List<Company> companiesTODEleteList = new ArrayList<>();
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyList.get(i);
            companyDAO.delete(company.getId());
            if (companyDAO.getById(company.getId()) == null) {
                System.out.println("Deleted: " + company.toString());
                companiesTODEleteList.add(company);
            }
        }
        dealList.removeAll(dealsTODEleteList);
        companyList.removeAll(companiesTODEleteList);
        System.out.println("-------------------------------------------------------------");
        if (dealList.size() + companyList.size() == 0) {
            System.out.println("------ Everything is cleared.");
        } else {
            System.out.println("------ Something left in database ---------- !!!!!!!!!!!!!!");
        }
        System.out.println("-------------------------------------------------------------");
        companyList.clear();
        dealList.clear();
        amount[0] = new BigDecimal(0);
        amount[1] = new BigDecimal(0);
        amount[2] = new BigDecimal(0);
        System.out.println(">>>>>>>>>>  TEAR DOWN  >>>>> DONE !  ");
    }

    @Test
    public void testGetDealsAmountForPreviousHour() {

        List<Report> list = reportDAO.getDealsAmountForPreviousHour();

        Assert.assertEquals("Quantitity of companies in reports: ", 3, list.size());
        BigDecimal testAmount[] = new BigDecimal[3];
        testAmount[0] = new BigDecimal(0);
        testAmount[1] = new BigDecimal(0);
        testAmount[2] = new BigDecimal(0);
        for (Report report : list) {
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
                default: throw new RuntimeException("Can't be !");
            }
        }
        Assert.assertArrayEquals(amount,testAmount);
    }
    /*@Test
    public void testCreate() {
        Deal dealTest = new Deal();
        Assert.assertTrue("Deal ID before creation must be '0'", dealTest.getId() == 0);

        try {
            dealTestId = dealDAO.insert(dealTest);
        } catch (Exception e) {
            Assert.assertTrue("Empty deal ID must be '0'", dealTest.getId() == 0);
        } finally {
            Assert.assertTrue("Empty deal ID must be '0'", dealTestId == 0);
        }

        dealTest.setName(DEFAULT_NAME);
        dealTest.setStage(stageForDealTest);
        dealTest.setCompany(companyForDealTest);
        dealTest.setDateCreate(DEFAULT_DATE);
        dealTest.setCreator(userForDealTest);
        dealTestId = dealDAO.insert(dealTest);

        Assert.assertNotNull("Deal creation failed", dealTest);
        Assert.assertTrue("Deal ID after creation must be not '0'", dealTestId > 0);
        Assert.assertNotNull("Deal date of creation must be not null", dealTest.getDateCreate());
        Assert.assertNotNull("Deal creator must be not null", dealTest.getCreator());
    }

    @Test
    public void testGetByPK() {
        Assert.assertNotNull("Deal read by PK failed", dealDAO.getById(1));
    }

    @Test
    public void testUpdate() throws SQLException {
        String updatedName = "Updated Name";
        Timestamp updatedCreateDate = new Timestamp(1L << 41);
        User updatedUser = userDAO.getById(2);
        Company updatedCompany = companyDAO.getById(2);
        Stage updatedStage = stageDAO.getById(2);
        Contact updatedContact = contactDAO.getById(2);

        Deal dealTest = new Deal();
        dealTest.setName(DEFAULT_NAME);
        dealTest.setDateCreate(DEFAULT_DATE);
        dealTest.setCreator(userForDealTest);
        dealTest.setCompany(companyForDealTest);
        dealTest.setStage(stageForDealTest);
        dealTestId = dealDAO.insert(dealTest);
        Assert.assertNotNull("Deal before update must not be null", dealTest);

        dealTest.setName(updatedName);
        dealTest.setDateCreate(updatedCreateDate);
        dealTest.setCreator(updatedUser);
        dealTest.setResponsibleUser(updatedUser);
        dealTest.setCompany(updatedCompany);
        dealTest.setStage(updatedStage);
        dealTest.setAmount(BigDecimal.valueOf(500.55));
        dealTest.setPrimaryContact(updatedContact);

        dealDAO.update(dealTest);

        Deal updatedDeal = dealDAO.getById(dealTestId);
        Assert.assertNotNull("Deal after update is null", updatedDeal);
        Assert.assertEquals("Deal name update failed", updatedName, updatedDeal.getName());
        Assert.assertEquals("Date of deal creation update failed", updatedCreateDate, updatedDeal.getDateCreate());
        Assert.assertEquals("Deal creator update failed", updatedUser.getId(), updatedDeal.getCreator().getId());
        Assert.assertEquals("Deal responsible user update failed", updatedUser.getId(), updatedDeal.getResponsibleUser().getId());
        Assert.assertEquals("Deal link to Company update failed", updatedCompany.getId(), updatedDeal.getCompany().getId());
        Assert.assertEquals("Deal link to Stage update failed", updatedStage.getId(), updatedDeal.getStage().getId());
        Assert.assertEquals("getAmount: ", BigDecimal.valueOf(500.55), updatedDeal.getAmount());
        Assert.assertEquals("Deal link to Primary Contact update failed", updatedContact.getId(), updatedDeal.getPrimaryContact().getId());
    }

    @Test
    public void testDelete() {
        Deal dealTest = new Deal();
        dealTest.setName(DEFAULT_NAME);
        dealTest.setDateCreate(DEFAULT_DATE);
        dealTest.setCreator(userForDealTest);
        dealTest.setCompany(companyForDealTest);
        dealTest.setStage(stageForDealTest);
        dealTestId = dealDAO.insert(dealTest);

        List dealList = dealDAO.getAll();
        int oldListSize = dealList.size();
        Assert.assertTrue("Deal list must not be empty", oldListSize > 0);

        dealDAO.delete(dealTestId);
        dealList = dealDAO.getAll();
        Assert.assertEquals("Deal delete test failed", 1, oldListSize - dealList.size());
        Assert.assertNull("Deal delete test failed", dealDAO.getById(dealTestId));
    }

    @Test
    public void testGetAll() {
        List dealList = dealDAO.getAll();
        Assert.assertNotNull("Deal list must not be null", dealList);
        Assert.assertTrue("Deal list must not be empty", dealList.size() > 0);
    }*/
}
