package com.becomejavasenior.entity;

import java.io.Serializable;
import java.util.Date;

public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int targetId;
    private String message;
    private boolean isError;
    private Date date;

    public Audit() {
    }

    public Audit(int targetId, String message, boolean isError, Date date) {
        this.targetId = targetId;
        this.message = message;
        this.isError = isError;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Audit audit = (Audit) o;

        if (id != audit.id) return false;
        if (targetId != audit.targetId) return false;
        if (isError != audit.isError) return false;
        if (message != null ? !message.equals(audit.message) : audit.message != null) return false;
        return date != null ? date.equals(audit.date) : audit.date == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + targetId;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (isError ? 1 : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", targetId=" + targetId +
                ", message='" + message + '\'' +
                ", isError=" + isError +
                ", date=" + date +
                '}';
    }
}
