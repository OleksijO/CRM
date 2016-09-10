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

    int insert(Deal t);

    void update(Deal t);

    List<Deal> getAll();

    Deal getById(int id);

    void delete(int id);

    List<Deal> getAllDealsByStage(String stage);

    List<Stage> getAllStage();

    List<Contact> getContactsByDealName(String dealName);

    List<Deal> getDealsForList();

}