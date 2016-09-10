package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;

import java.util.Date;
import java.util.List;

public interface ReportDAO extends GenericDAO<Report> {
    /**
     * Returns NULL if there is no Report with specified id.
     * Use method getReportById(int id) instead.
     */
    @Deprecated
    Report getById(int id);

    /**
     * Throws EmptyResultDataAccessException if there is no Report with specified id.
     */
    Report getReportById(int id);

    List<Report> getByCompanyAndPeriod(Company company, Date start, Date end);

    List<Report> getByPeriod(Date start, Date end);

    List<Report> getByCompany(Company company);

}
