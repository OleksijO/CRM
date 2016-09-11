package com.becomejavasenior.schedule;

import com.becomejavasenior.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HourDealAmountReportJob  extends AbstractTask {
    @Autowired
    ReportService reportService;

    @Scheduled(cron = "0 0 0/1 1/1 * ?")
    protected void executeInternal() {
        tryDoTask();
    }


    @Override
    protected void doTask() {
        reportService.makeHourDealAmountReports();
    }
}
