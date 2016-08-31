package com.becomejavasenior.aspects.dao;

import org.aspectj.lang.JoinPoint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


abstract public class DaoLog {
    protected String path;

    public DaoLog(){
    }

    public DaoLog(String path) {
        this.path = path;
    }

    protected String parseJointPoint(JoinPoint jp) {
        StringBuilder message = new StringBuilder();
        message.append(jp.getTarget().getClass().getName()).append("::")
                .append(jp.getSignature().getName())
                .append("(")
                //.append(jp.getArgs())
                .append(")");
        return message.toString();
    }


    private File logfile = null;
    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private final SimpleDateFormat logFileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    protected void log(String message) {
        checkLogfile();
        try (
                FileWriter fileWriter = new FileWriter(logfile, true)) {
            fileWriter.write(dataFormat.format(new Date()) + "\t" + message + "\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkLogfile() {
        if (logfile == null) {
            createNewLogFile();
        }
    }

    private void createNewLogFile() {
        if (path==null){
            System.out.println("Log filename is not set.");
        }
        logfile = new File(path);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("\t LOG FILE AT PATH:  " + logfile.getAbsolutePath());
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private String getNewFileName() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
