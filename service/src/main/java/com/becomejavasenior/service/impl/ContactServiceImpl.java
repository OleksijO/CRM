package com.becomejavasenior.service.impl;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.jdbc.*;
import com.becomejavasenior.jdbc.exceptions.DatabaseException;
import com.becomejavasenior.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("contactService")
@Transactional
public class ContactServiceImpl implements ContactService {

    private static final String STR_0 = "0";
    private static final String STR_CONTENT_DISPOSITION = "content-disposition";
    private static final String STR_FILENAME = "filename";
    private static final String STR_ALL_DAY = "Весь день";

    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_USER_ID = "contact_user_id";
    private static final String CONTACT_POSITION = "contact_position";
    private static final String CONTACT_EMAIL = "contact_email";
    private static final String CONTACT_TYPE_OF_PHONE = "contact_type_of_phone";
    private static final String CONTACT_PHONE = "contact_phone";
    private static final String CONTACT_SKYPE = "contact_skype";
    private static final String CONTACT_TAG = "contact_tag";
    private static final String COMPANY_NEW = "company_new";
    private static final String COMPANY_ID = "company_id";
    private static final String COMPANY_NAME = "company_name";
    private static final String COMPANY_PHONE = "company_phone";
    private static final String COMPANY_EMAIL = "company_email";
    private static final String COMPANY_WEB = "company_web";
    private static final String COMPANY_ADDRESS = "company_address";
    private static final String DEAL_NAME = "deal_name";
    private static final String DEAL_STAGE_NAME = "deal_stage_name";
    private static final String DEAL_BUDGET = "deal_budget";
    private static final String NOTE_TEXT = "note_text";
    private static final String TASK_DATE = "task_date";
    private static final String TASK_TIME = "task_time";
    private static final String TASK_RESPONSIBLE_USER_ID = "task_responsible_user_id";
    private static final String TASK_TYPE = "task_type";
    private static final String TASK_TEXT = "task_text";
    private static final String TASK_STATUS_NEW = "В работе";

    @Autowired
    private ContactDAO contactDAO;
    private User currentUser;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private StageDAO stageDAO;
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private DealDAO dealDAO;
    @Autowired
    private TagDAO tagDAO;
    @Autowired
    private NoteDAO noteDAO;
    @Autowired
    private FileDAO fileDAO;

    @Override
    public int insert(Contact contact) {
        return contactDAO.insert(contact);
    }

    @Override
    public void update(Contact contact) {
        contactDAO.update(contact);
    }

    @Override
    public List<Contact> getAll() {
        return contactDAO.getAll();
    }

    @Override
    public Contact getById(int id) {
        return contactDAO.getById(id);
    }

    @Override
    public void delete(int id) {
        contactDAO.delete(id);
    }

    @Override
    public List<User> getUserList() {
        return userDAO.getAll();
    }

    @Override
    public List<Company> getCompanyList() {
        return companyDAO.getAll();
    }

    @Override
    public List<Stage> getStageList() {
        return stageDAO.getAll();
    }

    @Override
    public List<String> getTaskTypesList() {
        return taskDAO.getAllTaskType();
    }

    @Override
    public TypeOfPhone[] getPhoneTypes() {
        return TypeOfPhone.values();
    }

    @Override
    public TypeOfPeriod[] getPeriodTypes() {
        return TypeOfPeriod.values();
    }

    @Override
    public List<Tag> getTagList() {
        return tagDAO.getAll();
    }

    @Override
    public List<String> getTaskTimeList() {
        List<String> timeList = new ArrayList<>();
        timeList.add("Весь день");
        for (int i = 0; i < 48; i++) {
            timeList.add(String.format("%02d:%02d", i >> 1, (i & 1) * 30));
        }
        return timeList;
    }

    @Override
    public boolean createByParameters(Map<String, String[]> parameterMap, Part filePart) {

        Contact contact = null;
        if (!parameterMap.get(CONTACT_NAME)[0].isEmpty() && !parameterMap.get(CONTACT_USER_ID)[0].equals(STR_0)) {
            contact = new Contact();
            contact.setCreator(currentUser);
            contact.setName(parameterMap.get(CONTACT_NAME)[0]);

            User responsibleUser = new User();
            responsibleUser.setId(Integer.parseInt(parameterMap.get(CONTACT_USER_ID)[0]));
            contact.setResponsibleUser(responsibleUser);

            contact.setPosition(parameterMap.get(CONTACT_POSITION)[0]);
            if (!parameterMap.get(CONTACT_TYPE_OF_PHONE)[0].isEmpty()) {
                contact.setTypeOfPhone(TypeOfPhone.getById(Integer.parseInt(parameterMap.get(CONTACT_TYPE_OF_PHONE)[0])));
            }
            contact.setPhone(parameterMap.get(CONTACT_PHONE)[0]);
            contact.setEmail(parameterMap.get(CONTACT_EMAIL)[0]);
            contact.setSkype(parameterMap.get(CONTACT_SKYPE)[0]);
            contact.setTags(createTags(parameterMap.get(CONTACT_TAG)[0]));
            contactDAO.insert(contact);
        }

        if (contact != null) {
            contact.setCompany(getOrCreateCompany(parameterMap));
            createDeal(contact, parameterMap);
            createTask(contact, parameterMap);
            createNote(contact, parameterMap);
            createFile(contact, filePart);
        }

        return contact != null && contact.getId() > 0;
    }

    private Task createTask(Contact contact, Map<String, String[]> parameterMap) {
        Task task = null;
        if (!parameterMap.get(TASK_DATE)[0].isEmpty()
                && !parameterMap.get(TASK_TIME)[0].isEmpty()
                && !parameterMap.get(TASK_RESPONSIBLE_USER_ID)[0].isEmpty()
                && !parameterMap.get(TASK_TYPE)[0].isEmpty()) {

            User responsibleUser = new User();
            responsibleUser.setId(Integer.parseInt(parameterMap.get(TASK_RESPONSIBLE_USER_ID)[0]));

            task = new Task();
            task.setCreator(currentUser);
            task.setResponsibleUser(responsibleUser);
            task.setStatus(TASK_STATUS_NEW);
            task.setTaskType(parameterMap.get(TASK_TYPE)[0]);
            task.setContact(contact);
            task.setPeriod(TypeOfPeriod.ALL_DAY);
            task.setName(parameterMap.get(TASK_TEXT)[0]);

            java.util.Date dateTask;
            try {
                dateTask = new SimpleDateFormat("yyyy-MM-dd").parse(parameterMap.get(TASK_DATE)[0]);
            } catch (java.text.ParseException e) {
                throw new DatabaseException("Can't parse task date", e);
            }
            task.setDateTask(dateTask);

            String timeTask = STR_ALL_DAY;
            int timeId = Integer.parseInt(parameterMap.get(TASK_TIME)[0]);
            if (timeId > 0 && timeId < 49) {
                timeTask = Integer.toString(--timeId >> 1).concat(":");
                timeTask = timeTask.concat((timeId & 1) == 1 ? "30" : "00");
            }
            task.setTimeTask(timeTask);

            taskDAO.insert(task);
        }
        return task;
    }

    private List<Tag> createTags(String tagString) {
        List<Tag> tagList = null;
        String[] tagArray = tagString.split("#");
        if (tagArray.length > 1) {
            tagList = new ArrayList<>();
            for (int i = 1; i < tagArray.length; i++) {
                String tagName = tagArray[i].trim();
                if (!tagName.isEmpty()) {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tagList.add(tag);
                }
            }
        }
        return tagList == null || tagList.isEmpty() ? null : tagList;
    }

    private Note createNote(Contact contact, Map<String, String[]> parameterMap) {
        Note note = null;
        if (!parameterMap.get(NOTE_TEXT)[0].isEmpty()) {
            note = new Note();
            note.setNote(parameterMap.get(NOTE_TEXT)[0]);
            note.setContact(contact);
            note.setCreator(currentUser);

            noteDAO.insert(note);
        }
        return note;
    }

    private Deal createDeal(Contact contact, Map<String, String[]> parameterMap) {
        Deal deal = null;
        if (contact.getCompany() != null
                && !parameterMap.get(DEAL_NAME)[0].isEmpty()
                && !parameterMap.get(DEAL_STAGE_NAME)[0].isEmpty()) {

            Stage stage = new Stage();
            stage.setName(parameterMap.get(DEAL_STAGE_NAME)[0]);
            stageDAO.insert(stage);

            deal = new Deal();
            deal.setName(parameterMap.get(DEAL_NAME)[0]);
            deal.setStage(stage);
            deal.setAmount(new BigDecimal(parameterMap.get(DEAL_BUDGET)[0]));
            deal.setCompany(contact.getCompany());
            deal.setPrimaryContact(contact);
            deal.setCreator(currentUser);

            dealDAO.insert(deal);
        }
        return deal;
    }

    private Company getOrCreateCompany(Map<String, String[]> parameterMap) {
        Company company = null;
        if (parameterMap.get(COMPANY_NEW)[0].equals(STR_0) && !parameterMap.get(COMPANY_ID)[0].equals(STR_0)) {
            company = companyDAO.getById(Integer.parseInt(parameterMap.get(COMPANY_ID)[0]));
        } else if (!parameterMap.get(COMPANY_NEW)[0].equals(STR_0)
                && !parameterMap.get(COMPANY_NAME)[0].isEmpty()
                && !parameterMap.get(COMPANY_PHONE)[0].isEmpty()
                && !parameterMap.get(COMPANY_EMAIL)[0].isEmpty()
                && !parameterMap.get(COMPANY_ADDRESS)[0].isEmpty()) {
            company = new Company();
            company.setCreator(currentUser);
            company.setResponsibleUser(currentUser); //todo: absent in mockup. Add it to page?
            company.setName(parameterMap.get(COMPANY_NAME)[0]);
            company.setPhone(parameterMap.get(COMPANY_PHONE)[0]);
            company.setEmail(parameterMap.get(COMPANY_EMAIL)[0]);
            company.setAddress(parameterMap.get(COMPANY_ADDRESS)[0]);
            company.setWeb(parameterMap.get(COMPANY_WEB)[0]);

            companyDAO.insert(company);
        }
        return company;
    }

    private File createFile(Contact contact, Part filePart) {
        File file = null;
        if (filePart != null) {
            file = new File();
            file.setContact(contact);
            file.setCreator(currentUser);
            String filename = filePart.getName();
            if (filePart.getHeaderNames().contains(STR_CONTENT_DISPOSITION)) {
                String header = filePart.getHeader(STR_CONTENT_DISPOSITION);
                if (header.contains(STR_FILENAME)) {
                    header = header.substring(header.indexOf(STR_FILENAME) + 9).trim();
                    filename = header.substring(1, header.length() - 1);
                }
            }
            file.setFileName(filename);
            file.setFileSize((int) filePart.getSize());
//            file.setUrlFile();
            try {
                file.setFile(streamToByteArray(filePart.getInputStream()));
            } catch (IOException e) {
                throw new DatabaseException("Error while loading file from browser", e);
            }

            fileDAO.insert(file);
        }
        return file;
    }

    // copied from commons-io-2.2 - use as dependency?
    private byte[] streamToByteArray(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int n;
        try {
            while ((n = inputStream.read(buffer, 0, bufferSize)) >= 0) {
                outputStream.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new DatabaseException("Error while uploading file to server", e);
        }
        return outputStream.toByteArray();
    }

    public void setContactDAO(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setCompanyDAO(CompanyDAO companyDAO) {
        this.companyDAO = companyDAO;
    }

    public void setStageDAO(StageDAO stageDAO) {
        this.stageDAO = stageDAO;
    }

    public void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public void setDealDAO(DealDAO dealDAO) {
        this.dealDAO = dealDAO;
    }

    public void setTagDAO(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public void setFileDAO(FileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }
}
