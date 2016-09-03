package com.becomejavasenior.service;

import com.becomejavasenior.jdbc.entity.ReportDAO;

public interface ReportService {

    void makeHourDealAmountReports();

    void setReportDAO(ReportDAO reportDAO);

}
