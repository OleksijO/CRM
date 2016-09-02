package com.becomejavasenior.servlets.beans;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddCompanyForm {

    private String dealName;
    private String dealStage;
    private BigDecimal dealBudget;

    private String contactName;
    private String contactPosition;
    private String typePhone;
    private String contactPhone;
    private String contactEmail;
    private String contactSkype;

    private String period;
    private String taskResponsibleUser;
    private String taskType;
    private String taskTime;
    private String dateTask;
    @NotNull(message = "{notnull.company.name}")
    @Size (min=3, max=100, message = "{size.company.name}")
    private String companyName;
    @NotNull(message="{notnull.company.responsibleUser}")
    private String companyResponsibleUser;
    @NotNull(message="{notnull.company.tag}")
    @Size (min=2, max=50, message="{size.company.tag}")
    private String companyTag;
    @NotNull(message="{notnull.company.phone}")
    @Pattern(regexp = "[+0-9()]{5,15}",
            message="{pattern.company.phone}"
    )
    private String companyPhone;
    @NotNull(message="{notnull.company.email}")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            message="{pattern.company.email}")
    private String companyEmail;
    @NotNull(message="{notnull.company.www}")
    @Pattern(regexp = "[\\-A-za-z0-9._/]{1,100}",
            message="{pattern.company.www}")
    private String companyWeb;
    @NotNull(message="{notnull.company.address}")
    @Size (min=2, max=100,message="{size.company.address}")
    private String companyAddress;
    @NotNull(message="{notnull.company.note}")
    @Size (min=2, max=100,message="{size.company.note}")
    private String companyNote;
    private String companyContact;

    public String getDealName() {
        return dealName;
    }

    public String getDealStage() {
        return dealStage;
    }

    public BigDecimal getDealBudget() {
        return dealBudget;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public String getTypePhone() {
        return typePhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactSkype() {
        return contactSkype;
    }

    public String getPeriod() {
        return period;
    }

    public String getTaskResponsibleUser() {
        return taskResponsibleUser;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public String getDateTask() {
        return dateTask;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyTag() {
        return companyTag;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCompanyWeb() {
        return companyWeb;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyNote() {
        return companyNote;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public void setDealStage(String dealStage) {
        this.dealStage = dealStage;
    }

    public void setDealBudget(BigDecimal dealBudget) {
        this.dealBudget = dealBudget;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public void setTypePhone(String typePhone) {
        this.typePhone = typePhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactSkype(String contactSkype) {
        this.contactSkype = contactSkype;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setTaskResponsibleUser(String taskResponsibleUser) {
        this.taskResponsibleUser = taskResponsibleUser;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setDateTask(String dateTask) {
        this.dateTask = dateTask;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyTag(String companyTag) {
        this.companyTag = companyTag;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public void setCompanyWeb(String companyWeb) {
        this.companyWeb = companyWeb;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setCompanyNote(String companyNote) {
        this.companyNote = companyNote;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public void setCompanyResponsibleUser(String companyResponsibleUser) {
        this.companyResponsibleUser = companyResponsibleUser;
    }

    public String getCompanyResponsibleUser() {

        return companyResponsibleUser;
    }
}
