package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.entity.*;
import com.becomejavasenior.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ContactDAO contactDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private DealDAO dealDAO;
    @Autowired
    private NoteDAO noteDAO;
    @Autowired
    private TagDAO tagDAO;
    @Autowired
    private FileDAO fileDAO;

    @Override
    public int insert(Company company) {
        return companyDAO.insert(company);
    }

    @Override
    public void update(Company company) {
        companyDAO.update(company);
    }

    @Override
    public List<Company> getAll() {
        return companyDAO.getAll();
    }

    @Override
    public Company getById(int id) {
        return companyDAO.getById(id);
    }

    @Override
    public void delete(int id) {
        companyDAO.delete(id);
    }

    @Override
    public List<User> getResponsibleUserList() {
        return userDAO.getAll();
    }

    @Override
    public List<Contact> getContactList() {
        return contactDAO.getAll();
    }

    @Override
    public Map<Integer, String> getTaskTypeList() {
        return taskDAO.getTaskTypeList();
    }

    @Override
    public Map<Integer, String> getStageDealsList() {
        return dealDAO.getStageDealsList();
    }

    @Override
    public int createNewCompany(Company company, Contact contact, Deal deal, Task task, File file) {
        /**
         * fill fields of the company for record into the database
         */
        int companyId = companyDAO.insert(company);
        company.setId(companyId);
        /**
         * fill fields of the contact for record into the database
         */
        if (!"".equals(contact.getName())) {
            contact.setCompany(company);
            int contactId = contactDAO.insert(contact);
            contact.setId(contactId);
        }
        /**
         * fill fields of the company contact for record into the database
         */
        List<Contact> companyContacts = company.getContacts();
        Contact companyContact = companyContacts.get(0);
        companyContact.setCompany(company);
        companyContact.setId(0);
        companyContact.setDateCreate(new Date());
        int companyContactId = contactDAO.insert(companyContact);
        companyContact.setId(companyContactId);
        /**
         * fill fields of the deal for record into the database
         */
        deal.setCompany(company);
        if (!"".equals(deal.getName())) {
            int dealId = dealDAO.insert(deal);
            deal.setId(dealId);
        }
        /**
         * fill fields of the task for record into the database
         */
        task.setCompany(company);
        task.setContact(companyContact);
        if (!"".equals(deal.getName())) {
            task.setDeal(deal);
        }
        taskDAO.insert(task);
        /**
         * fill fields of the notes for record into the database
         */
        if (company.getNotes().size() != 0) {
            List<Note> notes = company.getNotes();
            Note note = notes.get(0);
            note.setCompany(company);
            noteDAO.insert(note);
        }
        /**
         * fill fields of the tags for record into the database
         */
        List<Tag> tagList = company.getTags();
        for (Tag tag : tagList) {
            int tagId = tagDAO.insert(tag);
            tag.setId(tagId);
            companyDAO.companyTag(company, tag);
        }
        /**
         * fill fields of the attached file for record into the database
         */
        if (!"".equals(file.getFileName())) {
            file.setCompany(company);
            fileDAO.insert(file);
        }

        return companyId;
    }

    @Override
    public List<String> getTimeListForTask() {
        List<String> timeList = new ArrayList<>();
        timeList.add("Весь день");
        timeList.add("0.00");
        int countHour = 0;
        for (int i = 1; i <= 48; i++) {
            if ((i % 2) == 0) {
                countHour++;
                timeList.add(String.valueOf(countHour) + ".00");
            } else {
                timeList.add(String.valueOf(countHour) + ".30");
            }
        }
        return timeList;
    }
    @Override
    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }
    @Override
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }
    @Override
    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    @Override
    public void setDealDAO(DealDAO dealDAO) {
        this.dealDAO = dealDAO;
    }
    @Override
    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }
    @Override
    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }
    @Override
    public void setFileDAO(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }
}
