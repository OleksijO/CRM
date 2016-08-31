package com.becomejavasenior.service;

import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional()
public interface DealService extends DisposableBean, InitializingBean {
    @Transactional()
    int insert(Deal t);

    @Transactional()
    void update(Deal t);

    @Transactional(readOnly = true)
    List<Deal> getAll();

    @Transactional(readOnly = true)
    Deal getById(int id);

    @Transactional()
    void delete(int id);

    @Transactional(readOnly = true)
    List<Deal> getAllDealsByStage(String stage);

    @Transactional(readOnly = true)
    public List<Stage> getAllStage();

    @Transactional(readOnly = true)
    public List<Contact> getContactsByDealName(String dealName);

    @Transactional(readOnly = true)
    public List<Deal> getDealsForList();
}