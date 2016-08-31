package com.becomejavasenior.aspects.dao;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ThrowedLog extends DaoLog {


    public ThrowedLog() {
        super("dao_thrown_log");
    }

    @AfterThrowing(value = "execution(* com.becomejavasenior.jdbc.*.*.*(..))",throwing = "e")
    public void logSuccessDaoOperation(JoinPoint jp, Exception e) {
        logger.error("DAO:\t"+parseJointPoint(jp)+"\t EXCEPTION: "+e.getClass()+" : "+e.getMessage());
        //System.out.println("DAO:\t"+parseJointPoint(jp)+"\t EXCEPTION: "+e.getClass()+" : "+e.getMessage());
    }
}
