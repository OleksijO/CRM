package com.becomejavasenior.service.Mocks;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;
import com.becomejavasenior.jdbc.DealDAO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class DealDaoStub implements DealDAO{
    @Override
    public int insert(Deal deal) {
        return 0;
    }

    @Override
    public void update(Deal deal) {

    }

    @Override
    public List<Deal> getAll() {
        return null;
    }

    @Override
    public Deal getById(int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<Deal> getDealsByStage(String stage) {
        return null;
    }

    @Override
    public List<Contact> getContactsByDealName(String dealName) {
        return null;
    }

    @Override
    public List<Stage> getAllStage() {
        return null;
    }

    @Override
    public List<Deal> getDealsForList() {
        return null;
    }

    @Override
    public void addContactToDeal(Deal deal, Contact contact) {

    }

    @Override
    public Map<Integer, String> getStageDealsList() {
        return null;
    }

    @Override
    public Map<Company, BigDecimal> getDealAmountsForCompaniesForPeriod(Date start, Date finish) {
        Date date = new Date(System.currentTimeMillis());
        Date testFinish = DateUtils.truncate(date, Calendar.HOUR);
        Date testStart = DateUtils.truncate(new Date(testFinish.getTime() - 1000 * 1800), Calendar.HOUR);
        if(testFinish.equals(finish)&&testStart.equals(testStart)){
            Map<Company, BigDecimal> map=new HashMap<>();
            map.put(new Company(),new BigDecimal(1));
            map.put(new Company(),new BigDecimal(1));
            map.put(new Company(),new BigDecimal(1));
            return map;
        }

        throw new RuntimeException("Test failed. Start or end of period do not match!");
    }
}
