package com.becomejavasenior.aspects.dao;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class SuccessfullLog extends DaoLog {

    @AfterReturning("execution(* com.becomejavasenior.jdbc.*.*.*(..))")
    public void logSuccessDaoOperation(JoinPoint jp) {
        log("DAO:\t"+parseJointPoint(jp)+"\tSUCCESS");
        //TODO comment next lines
        System.out.println("DAO:\t"+parseJointPoint(jp)+"\tSUCCESS");

    }
}
