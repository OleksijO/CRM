package com.becomejavasenior.service;

import com.becomejavasenior.entity.*;

import java.util.List;
import java.util.Map;

public interface CompanyService {
    int insert(Company company);
    void update(Company company);
    List<Company> getAll();
    Company getById(int id);
    void delete(int id);
    List<User> getResponsibleUserList();
    List<Contact> getContactList();
    Map<Integer, String> getTaskTypeList();
    Map<Integer, String> getStageDealsList();
    int createNewCompany(Company company, Contact contact, Deal deal, Task task, File file);
    List<String> getTimeListForTask();
}
