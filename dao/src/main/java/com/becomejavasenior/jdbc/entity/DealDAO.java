package com.becomejavasenior.jdbc.entity;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;

import java.util.List;

import java.util.Map;

public interface DealDAO extends GenericDAO<Deal> {
    public List<Deal> getDealsByStage(String stage);
    public List<Contact> getContactsByDealName(String dealName);
    public List<Stage> getAllStage();
    public List<Deal> getDealsForList();
    void addContactToDeal(Deal deal, Contact contact);
    Map<Integer, String> getStageDealsList();
}