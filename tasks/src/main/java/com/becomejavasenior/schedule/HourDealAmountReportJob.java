package com.becomejavasenior.schedule;

import com.becomejavasenior.service.ReportService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

//@Service
public class HourDealAmountReportJob extends QuartzJobBean {
    //@Autowired
    ReportService reportService;
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("before calling report task. Service: "+reportService);
        reportService.makeHourDealAmountReports();
        System.out.println("Hour Company Deal Amount Report is done.");
    }
}
