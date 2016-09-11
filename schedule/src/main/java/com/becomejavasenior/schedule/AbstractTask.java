package com.becomejavasenior.schedule;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
abstract public class AbstractTask {
    @Autowired
    @Qualifier("scheduleLogger")
    protected Logger logger;

    protected void tryDoTask(){
        try{
            doTask();
            logger.info("Scheduler: task "+this.getClass().getSimpleName()+" executed successfully.");
        }catch (Exception e){
            logger.error("Scheduler: ERROR: task "+this.getClass().getSimpleName()+" aborted with error.",e);
        }
    }

    abstract protected void doTask();
}
