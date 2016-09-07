package com.becomejavasenior.service;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.entity.User;
import com.becomejavasenior.jdbc.entity.CompanyDAO;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.jdbc.entity.UserDAO;
import com.becomejavasenior.service.SpringServiceTests;
import com.becomejavasenior.service.ReportService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ReportServiceTest extends SpringServiceTests{
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DealDAO dealDAO;
    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private ReportService reportService;


    private User userForDealTest;
    private Company companyForTest;
    private List<Deal> dealList = new ArrayList<>();
    private List<Company> companyList = new ArrayList<>();
    private int maxDeals = 9;
    private int maxCompanies = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

    @PostConstruct
    public void init() {
        companyForTest = companyDAO.getById(1);
        userForDealTest = userDAO.getById(1);
        companyForTest.setResponsibleUser(userForDealTest);
    }

    @Test
    public void testGetDealsAmountForPreviousHour() {
        Date date = DateUtils.truncate(new Date(System.currentTimeMillis()), Calendar.HOUR);
        setUp(date);

        int totalNumberOfReports = reportDAO.getAll().size();
        reportService.makeHourDealAmountReports();
        List<Report> listReport = reportDAO.getByPeriod(new Date(date.getTime() - 1000 * 3600), date);
        int numberOfTestReports = listReport.size();
        int newTotalNumberOfReports = reportDAO.getAll().size();

        tearDown(listReport);

        Assert.assertEquals("Quantitity of added reports: ",
                numberOfTestReports,
                newTotalNumberOfReports - totalNumberOfReports);
    }

    private void setUp(Date date) {
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyDAO.getById(1);
            company.setId(0);
            company.setResponsibleUser(userForDealTest);
            company.setName("Company " + i);
            company.setId(companyDAO.insert(company));
            companyList.add(company);
        }
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealDAO.getById(1);
            deal.setId(0);
            deal.setDateCreate(new Date(date.getTime() - 1000 * 60 * (i + 1)));
            deal.setAmount(new BigDecimal((Math.pow(10, i % 3) + i) * i));
            deal.setCompany(companyList.get(i % 3));
            deal= dealDAO.getById(dealDAO.insert(deal));
            deal.setCompany(companyList.get(i % 3));
            dealList.add(deal);
        }
    }

    private void tearDown(List<Report> listReport) {
        for(Report report:listReport){
            reportDAO.delete(report.getId());
        }
        List<Deal> dealsTODeleteList = new ArrayList<>();
        for (int i = 0; i < maxDeals; i++) {
            Deal deal = dealList.get(i);
            dealDAO.delete(deal.getId());
            if (dealDAO.getById(deal.getId()) == null) {
                dealsTODeleteList.add(deal);

            }
        }
        List<Company> companiesTODEleteList = new ArrayList<>();
        for (int i = 0; i < maxCompanies; i++) {
            Company company = companyList.get(i);
            companyDAO.delete(company.getId());
            if (companyDAO.getById(company.getId()) == null) {
                companiesTODEleteList.add(company);
            }
        }
        dealList.removeAll(dealsTODeleteList);
        companyList.removeAll(companiesTODEleteList);
        if (dealList.size() + companyList.size() == 0) {
        } else {
            System.out.println("\n------ Something left in database after test ---------- !!!!!!!!!!!!!!\n");
        }
        companyList.clear();
        dealList.clear();
    }


}
