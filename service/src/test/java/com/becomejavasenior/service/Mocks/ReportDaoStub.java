package com.becomejavasenior.service.Mocks;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Report;
import com.becomejavasenior.jdbc.ReportDAO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ReportDaoStub implements ReportDAO {
    private int counter=0;

    @Override
    public int insert(Report report) {
        Date date = new Date(System.currentTimeMillis());
        Date testFinish = DateUtils.truncate(date, Calendar.HOUR);
        Date testStart = DateUtils.truncate(new Date(testFinish.getTime() - 1000 * 1800), Calendar.HOUR);
        if(!testStart.equals(report.getDate())){
            throw new RuntimeException(
                    "Formed report has wrong date. Exp:"+testStart+"; Act: "+report.getDate());
        }
        counter++;
        return counter;
    }

    @Override
    public void update(Report report) {

    }

    @Override
    public List<Report> getAll() {
        List<Report> list = new ArrayList<>();
        for(int i=0;i<counter;i++){
            list.add(new Report());
        }
        return list;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Report getById(int id) {
        return null;
    }

    @Override
    public Report getReportById(int id) {
        return null;
    }

    @Override
    public List<Report> getByCompanyAndPeriod(Company company, Date start, Date end) {
        return null;
    }

    @Override
    public List<Report> getByPeriod(Date start, Date end) {
        return null;
    }

    @Override
    public List<Report> getByCompany(Company company) {
        return null;
    }
}
