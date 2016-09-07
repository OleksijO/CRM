package com.becomejavasenior.service;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.*;

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

    void setCompanyDAO(com.becomejavasenior.jdbc.entity.CompanyDAO companyDAO);

    void setUserDAO(UserDAO userDAO);

    void setContactDAO(ContactDAO contactDAO);

    void setTaskDAO(TaskDAO taskDAO);

    void setDealDAO(DealDAO dealDAO);

    void setNoteDAO(NoteDAO noteDAO);

    void setTagDAO(TagDAO tagDAO);

    void setFileDAO(FileDAO fileDAO);
}
