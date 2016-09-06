package com.becomejavasenior.schedule;

import com.becomejavasenior.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HourDealAmountReportJob  {
    @Autowired
    ReportService reportService;

    @Scheduled(cron = "0/5 * * * * ?")
    protected void executeInternal() {
        reportService.makeHourDealAmountReports();
        System.out.println("Hour Company Deal Amount Report is done.");
    }

    /*protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        reportService.makeHourDealAmountReports();
        System.out.println("Hour Company Deal Amount Report is done.");
    }*/

}
