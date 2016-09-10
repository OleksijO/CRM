package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.service.CompanyService;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

//@WebServlet(name = "companyServlet", urlPatterns = "/company")
//@MultipartConfig
@Deprecated     //changed by AddCompany in mvc module
public class CompanyServlet extends HttpServlet {

    @Autowired
    private CompanyService companyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = companyService.getResponsibleUserList();
        userList.sort((User user1, User user2) -> user1.getName().compareTo(user2.getName()));
        request.setAttribute("responsibleUsers", userList);

        List<Contact> contactList = companyService.getContactList();
        contactList.sort((Contact contact1, Contact contact2) -> contact1.getName().compareTo(contact2.getName()));
        request.setAttribute("contactList", contactList);

        request.setAttribute("typeOfPhone", TypeOfPhone.values());

        request.setAttribute("typeOfPeriod", TypeOfPeriod.values());

        request.setAttribute("taskType", companyService.getTaskTypeList());

        request.setAttribute("stageDeals", companyService.getStageDealsList());

        request.setAttribute("timeListForTask", companyService.getTimeListForTask());

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/pages/newcompany.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Deal deal = getDealFromRequest(request);
        Contact contact = getContactFromRequest(request);
        Task task = getTaskFromRequest(request);
        Company company = getCompanyFromRequest(request);
        File attachedFile = getFileFromRequest(request);
        companyService.createNewCompany(company, contact, deal, task, attachedFile);
    }

    private Deal getDealFromRequest(HttpServletRequest request) {
        Deal deal = new Deal();
        deal.setName(request.getParameter("dealName"));
        Stage stage = new Stage();
        /**
         * Obtain key stages of the deal by value
         */
        Map<Integer, String> dealStages = companyService.getStageDealsList();
        for (Map.Entry entry : dealStages.entrySet()) {
            if (entry.getValue().equals(request.getParameter("dealStage"))) {
                stage.setId((Integer) entry.getKey());
            }
        }
        stage.setName(request.getParameter("dealStage"));
        deal.setStage(stage);

        if (!request.getParameter("dealBudget").isEmpty()) {
            deal.setAmount(new BigDecimal(request.getParameter("dealBudget")));
        }
        deal.setDelete(false);
        deal.setDateCreate(new Date());
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        deal.setCreator(creator);
        deal.setResponsibleUser(creator);
        return deal;
    }

    private Contact getContactFromRequest(HttpServletRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getParameter("contactName"));

        if (!request.getParameter("contactPosition").isEmpty()) {
            contact.setPosition(request.getParameter("contactPosition"));
        }
        contact.setTypeOfPhone(TypeOfPhone.valueOf(request.getParameter("typePhone")));
        contact.setPhone(request.getParameter("contactPhone"));
        if (!request.getParameter("contactEmail").isEmpty()) {
            contact.setEmail(request.getParameter("contactEmail"));
        }
        if (!request.getParameter("contactSkype").isEmpty()) {
            contact.setSkype(request.getParameter("contactSkype"));
        }
        contact.setDelete(false);
        contact.setDateCreate(new Date());
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        contact.setCreator(creator);
        contact.setResponsibleUser(creator);
        return contact;
    }

    private Task getTaskFromRequest(HttpServletRequest request) {
        Task task = new Task();
        task.setPeriod(TypeOfPeriod.valueOf(request.getParameter("period")));
        /**
         * We are looking for a user by name
         */
        List<User> userList = companyService.getResponsibleUserList();
        Optional<User> responsibleUser = userList.stream().filter(u -> u.getName().equals(request.getParameter("taskResponsibleUser"))).findFirst();
        task.setResponsibleUser(responsibleUser.get());
        /**
         * Obtain key task types by value
         */
        Map<Integer, String> taskTypes = companyService.getTaskTypeList();
        for (Map.Entry entry : taskTypes.entrySet()) {
            if (entry.getValue().equals(request.getParameter("taskType"))) {
                task.setTaskType(String.valueOf(entry.getKey()));
            }
        }
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        task.setCreator(creator);
        task.setStatus("1"); // TODO: Where can I take if the task does not have a field for status
        task.setDelete(false);
        task.setDateCreate(new Date());
        task.setTimeTask(request.getParameter("taskTime")); // TODO: check how its work after merge dao changes!!!!
        /**
         * Parsing date from String to Date()
         */
        String dateString = request.getParameter("dateTask");
        if (!dateString.isEmpty()) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            try {
                Date dateTask = dateFormat.parse(dateString);
                task.setDateTask(dateTask); // TODO: check how its work after merge dao changes!!!!
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return task;
    }

    private Company getCompanyFromRequest(HttpServletRequest request) {
        Company company = new Company();
        company.setName(request.getParameter("companyName"));
        /**
         *  Parse tags with Google Guava library
         */
        Splitter splitter = Splitter.on("#").omitEmptyStrings().trimResults();
        Iterable<String> tokens = splitter.split(request.getParameter("companyTag"));
        List<Tag> tags = new ArrayList<>();
        for (String token : tokens) {
            Tag tag = new Tag();
            tag.setName("#" + token);
            tag.setDelete(false);
            tags.add(tag);
        }
        company.setTags(tags);

        List<User> userList = companyService.getResponsibleUserList();
        Optional<User> responsibleUser = userList.stream().filter(u -> u.getName().equals(request.getParameter("taskResponsibleUser"))).findFirst();
        company.setResponsibleUser(responsibleUser.get());
        company.setPhone(request.getParameter("companyPhone"));
        company.setEmail(request.getParameter("companyEmail"));
        company.setWeb(request.getParameter("companyWeb"));
        company.setAddress(request.getParameter("companyAddress"));
        String noteContent = request.getParameter("companyNote");
        if (!noteContent.isEmpty()) {
            List<Note> noteList = new ArrayList<>();
            Note note = new Note();
            note.setNote(request.getParameter("companyNote"));
            note.setDateCreate(new Date());
            note.setDelete(false);
            User creator = new User();
            creator.setId(1); //TODO: change to user under which the logged in
            note.setCreator(creator);
            noteList.add(note);
            company.setNotes(noteList);
        } else {
            List<Note> noteList = new ArrayList<>();
            company.setNotes(noteList);
        }
        /**
         * We are looking for a contact by name
         */
        List<Contact> contactList = companyService.getContactList();
        Optional<Contact> companyContact = contactList.stream().filter(c -> c.getName().equals(request.getParameter("companyContact"))).findFirst();
        List<Contact> contacts = new ArrayList<>();
        contacts.add(companyContact.get());
        company.setContacts(contacts);
        company.setDelete(false);
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        company.setCreator(creator);
        company.setDateCreate(new Date());
        return company;
    }

    private File getFileFromRequest(HttpServletRequest request) throws IOException, ServletException {
        File attachedFile = new File();
        /**
         * reading file bytes into byte[]
         */
        Part filePart = request.getPart("companyFile");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream fileContent = filePart.getInputStream();

        byte[] bytes = new byte[8192];
        int read;
        while ((read = fileContent.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        /**
         * fill fields of the file
         */
        User creator = new User(); //TODO: change to user under which the logged in
        creator.setId(1);
        attachedFile.setCreator(creator);
        attachedFile.setDateCreate(new Date());
        attachedFile.setFileName(getFileName(filePart));
        attachedFile.setFileSize(outputStream.size());
        attachedFile.setDelete(false);
        attachedFile.setFile(bytes);
        return attachedFile;
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
