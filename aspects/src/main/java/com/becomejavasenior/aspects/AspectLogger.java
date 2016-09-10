package com.becomejavasenior.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;

abstract class AspectLogger {
    protected final Logger logger;

    public AspectLogger(String loggerName) {
        this.logger = Logger.getLogger(loggerName);
    }

    protected String parseJointPoint(JoinPoint jp) {
        StringBuilder message = new StringBuilder();
        message.append(jp.getTarget().getClass().getName()).append("::")
                .append(jp.getSignature().getName())
                .append("(")
                .append(getMethodArguments(jp))
                .append(")");
        return message.toString();
    }

    private String getMethodArguments(JoinPoint jp) {
        return Arrays.stream(jp.getArgs())
                .map((obj) -> (obj.toString()))
                .reduce((str1, str2) -> (str1 + " " + str2)).orElse("");
    }
}
