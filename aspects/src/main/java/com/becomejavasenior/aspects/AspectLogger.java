package com.becomejavasenior.aspects;

import org.apache.log4j.Logger;

abstract class AspectLogger {
    protected final Logger logger;

    public AspectLogger(String loggerName) {
        this.logger = Logger.getLogger(loggerName);
    }
}
