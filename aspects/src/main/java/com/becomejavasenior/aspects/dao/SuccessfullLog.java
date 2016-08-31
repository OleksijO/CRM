package com.becomejavasenior.aspects.dao;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class SuccessfullLog extends DaoLog {

    public SuccessfullLog() {
        super("dao_success_log");
    }

    @AfterReturning("execution(* com.becomejavasenior.jdbc.*.*.*(..))")
    public void logSuccessDaoOperation(JoinPoint jp) {
        logger.info("DAO:\t"+parseJointPoint(jp)+"\tSUCCESS");
        //TODO comment next line
        System.out.println("DAO:\t"+parseJointPoint(jp)+"\tSUCCESS");
    }
}
