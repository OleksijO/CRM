package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.Report;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.service.ReportService;

import java.util.List;

public class ReportServiceImpl implements ReportService {
    private ReportDAO reportDAO;

    @Override
    public void makeHourDealAmountReports() {
        System.out.println(this.getClass()+" prepare to obtain list :");
        List<Report> list = reportDAO.getDealsAmountForPreviousHour();
        System.out.println(this.getClass()+" list obtained :"+list);
        for (Report report : list) {
            reportDAO.insert(report);
        }
    }

    @Override
    public void setReportDAO(ReportDAO reportDao) {
        this.reportDAO = reportDao;
    }
}
