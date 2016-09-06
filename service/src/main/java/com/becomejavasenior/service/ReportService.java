package com.becomejavasenior.service;

import com.becomejavasenior.jdbc.entity.DealDAO;
import com.becomejavasenior.jdbc.entity.ReportDAO;

public interface ReportService {

    void makeHourDealAmountReports();

    void setReportDAO(ReportDAO reportDAO);

    void setDealDAO(DealDAO dealDAO);
}
