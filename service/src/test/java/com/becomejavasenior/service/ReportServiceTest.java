package com.becomejavasenior.service;

import com.becomejavasenior.jdbc.entity.ReportDAO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ReportServiceTest extends SpringServiceTests{
    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private ReportService reportService;



    @Test
    public void testGetDealsAmountForPreviousHour() {
        int totalNumberOfReports = reportDAO.getAll().size();
        reportService.makeHourDealAmountReports();
        int numberOfTestReports = reportDAO.getAll().size();
        int newTotalNumberOfReports = reportDAO.getAll().size();

        Assert.assertEquals("Quantitity of added reports: ",
                numberOfTestReports,
                newTotalNumberOfReports - totalNumberOfReports);
    }



}
