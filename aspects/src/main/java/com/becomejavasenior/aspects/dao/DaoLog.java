package com.becomejavasenior.aspects.dao;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;


abstract public class DaoLog {
protected  final Logger logger;

    public DaoLog(String logger) {
        this.logger = Logger.getLogger(logger);
    }

    protected String parseJointPoint(JoinPoint jp){
        StringBuilder message = new StringBuilder();
        message.append(jp.getTarget().getClass().getName()).append("::")
                .append(jp.getSignature().getName())
                .append("(")
                //.append(jp.getArgs())
                .append(")");
        return message.toString();
    }
}
