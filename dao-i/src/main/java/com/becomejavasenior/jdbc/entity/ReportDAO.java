package com.becomejavasenior.jdbc.entity;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;

import java.util.Date;
import java.util.List;

public interface ReportDAO extends GenericDAO<Report> {

    /**
     *
     * @param date    - must be date with exact number hours to get report for previous full hour
     * @return        - returns report for hour before specified date,
     *                  i.e. date=2000-04-27 12:00:00 report has deals' amount from 11:00:00.000 to 11:59:59.999.
     *                  date=2000-04-27 12:59:59 will give the same result //TODO
     */
    List<Report> getByDateAndCompany(Date date, Company company);

    List<Report> getByCompany(Company company);

    List<Report> getByDate(Date date);

    List<Report> getDealsAmountForPreviousHour();
}
