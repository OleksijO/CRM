package com.becomejavasenior.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DailyUnfinishedDealReminding  {



    @Scheduled(cron = "0/1 * * * * ?") //
    public void executeInternal()  {
        System.out.println("Send remindings");
    }
}
