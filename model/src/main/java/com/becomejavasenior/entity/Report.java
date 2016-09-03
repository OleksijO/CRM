package com.becomejavasenior.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Report {
    private int id;
    private Date date;
    private BigDecimal hourAmount;
    private Company company;

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getHourAmount() {
        return hourAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHourAmount(BigDecimal hourAmount) {
        this.hourAmount = hourAmount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;

        if (id != report.id) return false;
        return date != null ? date.equals(report.date) : report.date == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", date=" + date +
                ", hourAmount=" + hourAmount +
                ", company=" + company +
                '}';
    }
}
