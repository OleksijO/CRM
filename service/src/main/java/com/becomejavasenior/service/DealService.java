package com.becomejavasenior.service;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;

import java.util.List;

public interface DealService {
    int insert(Deal t);
    void update(Deal t);
    List<Deal> getAll();
    Deal getById(int id);
    void delete(int id);

    List<Deal> getAllDealsByStage(String stage);
    public List<Stage> getAllStage();
    public List<Contact> getContactsByDealName(String dealName);
    public List<Deal> getDealsForList();
}