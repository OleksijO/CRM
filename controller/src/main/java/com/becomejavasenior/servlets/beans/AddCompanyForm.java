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
    @Size (min=3, max=50, message = "{size.company.name}")
    private String companyName;
    @NotNull
    private String companyResponsibleUser;
    @NotNull
    @Size (min=2, max=20)
    private String companyTag;
    @NotNull
    @Pattern(regexp = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$")
    private String companyPhone;
    @NotNull
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
    private String companyEmail;
    @NotNull
    @Pattern(regexp = "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$")
    private String companyWeb;
    @NotNull
    @Size (min=2, max=100)
    private String companyAddress;
    @NotNull
    @Size (min=2, max=100)
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
