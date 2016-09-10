package com.becomejavasenior.jdbc;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import java.util.Map;

public interface DealDAO extends GenericDAO<Deal> {
    public List<Deal> getDealsByStage(String stage);
    public List<Contact> getContactsByDealName(String dealName);
    public List<Stage> getAllStage();
    public List<Deal> getDealsForList();
    void addContactToDeal(Deal deal, Contact contact);
    Map<Integer, String> getStageDealsList();

    Map<Company,BigDecimal> getDealAmountsForCompaniesForPeriod(Date start, Date finish);
}