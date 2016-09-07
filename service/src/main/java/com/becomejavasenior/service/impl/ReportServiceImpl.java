package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;
import com.becomejavasenior.service.ReportService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDAO reportDAO;
    @Autowired
    private DealDAO dealDAO;

    @Override
    public void makeHourDealAmountReports() {
        Date date = new Date(System.currentTimeMillis());
        Date finish = DateUtils.truncate(date, Calendar.HOUR);
        Date start = DateUtils.truncate(new Date(finish.getTime() - 1000 * 1800), Calendar.HOUR);
        Map<Company, BigDecimal> companyAmountMap = dealDAO.getDealAmountsForCompaniesForPeriod(start, finish);
        for (Map.Entry<Company, BigDecimal> pair : companyAmountMap.entrySet()) {
            Report report = new Report();
            report.setDate(start);
            report.setHourAmount(pair.getValue());
            report.setCompany(pair.getKey());
            reportDAO.insert(report);
        }
    }

    @Override
    public void setReportDAO(ReportDAO reportDao) {
        this.reportDAO = reportDao;
    }

    @Override
    public void setDealDAO(DealDAO dealDAO) {
        this.dealDAO = dealDAO;
    }

}
