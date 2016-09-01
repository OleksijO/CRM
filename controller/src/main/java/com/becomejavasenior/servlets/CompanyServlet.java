package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.*;
import com.becomejavasenior.service.CompanyService;
import com.becomejavasenior.servlets.beans.AddCompanyForm;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/company")
public class CompanyServlet {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    public String doGet(Model model) {
        List<User> userList = companyService.getResponsibleUserList();
        userList.sort((User user1, User user2) -> user1.getName().compareTo(user2.getName()));
        model.addAttribute("responsibleUsers", userList);

        List<Contact> contactList = companyService.getContactList();
        contactList.sort((Contact contact1, Contact contact2) -> contact1.getName().compareTo(contact2.getName()));
        model.addAttribute("contactList", contactList);

        model.addAttribute("typeOfPhone", TypeOfPhone.values());

        model.addAttribute("typeOfPeriod", TypeOfPeriod.values());

        model.addAttribute("taskType", companyService.getTaskTypeList());

        model.addAttribute("stageDeals", companyService.getStageDealsList());

        model.addAttribute("timeListForTask", companyService.getTimeListForTask());

        model.addAttribute("addCompanyForm",new AddCompanyForm());

        return "newcompany";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doPost(@ModelAttribute("addCompanyForm")AddCompanyForm addCompanyForm, Model model) {
        Exception error=null;
        Company company=null;
        try {
            Deal deal = getDealFromForm(addCompanyForm);
            Contact contact = getContactFromForm(addCompanyForm);
            Task task = getTaskFromForm(addCompanyForm);
            company = getCompanyFromForm(addCompanyForm);
            //getFileFromForm(addCompanyForm); // TODO implement file upload
            File attachedFile = new File();    // TODO implement file upload
            attachedFile.setFile(new byte[0]); // TODO implement file upload
            companyService.createNewCompany(company, contact, deal, task, attachedFile);
        } catch(Exception e) {
            error = e;
        }
        String message;
        if ((company!=null)&&company.getId()>0) {
            message="Company with id="+company.getId()+" sucessfully added.";
        } else {
            if (error!=null) {
                message = "Error. Can't create company because of: " + error.getMessage();
            } else {
                message = "Error. Can't create company becouse of unknown reasons......";
            }
        }
        model.addAttribute("message", message);
        return "message";
    }

    private Deal getDealFromForm(AddCompanyForm form) {
        Deal deal = new Deal();
        deal.setName(form.getDealName());
        Stage stage = new Stage();
        /**
         * Obtain key stages of the deal by value
         */
        Map<Integer, String> dealStages = companyService.getStageDealsList();
        for (Map.Entry entry : dealStages.entrySet()) {
            if (entry.getValue().equals(form.getDealStage())) {
                stage.setId((Integer) entry.getKey());
            }
        }
        stage.setName(form.getDealStage());
        deal.setStage(stage);
        deal.setAmount(form.getDealBudget());
        deal.setDelete(false);
        deal.setDateCreate(new Date());
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        deal.setCreator(creator);
        deal.setResponsibleUser(creator);
        return deal;
    }

    private Contact getContactFromForm(AddCompanyForm form) {
        Contact contact = new Contact();
        contact.setName(form.getContactName());

        if (!form.getContactPosition().isEmpty()) {
            contact.setPosition(form.getContactPosition());
        }
        contact.setTypeOfPhone(TypeOfPhone.valueOf(form.getTypePhone()));
        contact.setPhone(form.getContactPhone());
        if (!form.getContactEmail().isEmpty()) {
            contact.setEmail(form.getContactEmail());
        }
        if (!form.getContactSkype().isEmpty()) {
            contact.setSkype(form.getContactSkype());
        }
        contact.setDelete(false);
        contact.setDateCreate(new Date());
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        contact.setCreator(creator);
        contact.setResponsibleUser(creator);
        return contact;
    }

    private Task getTaskFromForm(AddCompanyForm form) {
        Task task = new Task();
        task.setPeriod(TypeOfPeriod.valueOf(form.getPeriod()));
        /**
         * We are looking for a user by name
         */
        List<User> userList = companyService.getResponsibleUserList();
        Optional<User> responsibleUser =
                userList.stream().filter(u -> u.getName().equals(form.getTaskResponsibleUser())).findFirst();
        task.setResponsibleUser(responsibleUser.get());
        /**
         * Obtain key task types by value
         */
        Map<Integer, String> taskTypes = companyService.getTaskTypeList();
        for (Map.Entry entry : taskTypes.entrySet()) {
            if (entry.getValue().equals(form.getTaskType())) {
                task.setTaskType(String.valueOf(entry.getKey()));
            }
        }
        User creator = new User();
        creator.setId(1); //TODO: change to user under which the logged in
        task.setCreator(creator);
        task.setStatus("1"); // TODO: Where can I take if the task does not have a field for status
        task.setDelete(false);
        task.setDateCreate(new Date());
        task.setTimeTask(form.getTaskTime()); // TODO: check how its work after merge dao changes!!!!
        /**
         * Parsing date from String to Date()
         */
        String dateString = form.getDateTask();
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

    private Company getCompanyFromForm(AddCompanyForm form) {
        Company company = new Company();
        company.setName(form.getCompanyName());
        /**
         *  Parse tags with Google Guava library
         */
        Splitter splitter = Splitter.on("#").omitEmptyStrings().trimResults();
        Iterable<String> tokens = splitter.split(form.getCompanyTag());
        List<Tag> tags = new ArrayList<>();
        for (String token : tokens) {
            Tag tag = new Tag();
            tag.setName("#" + token);
            tag.setDelete(false);
            tags.add(tag);
        }
        company.setTags(tags);

        List<User> userList = companyService.getResponsibleUserList();
        Optional<User> responsibleUser = userList.stream().filter(u -> u.getName().equals(form.getCompanyResponsibleUser())).findFirst();
        company.setResponsibleUser(responsibleUser.get());
        company.setPhone(form.getCompanyPhone());
        company.setEmail(form.getCompanyEmail());
        company.setWeb(form.getCompanyWeb());
        company.setAddress(form.getCompanyAddress());
        String noteContent = form.getCompanyNote();
        if (!noteContent.isEmpty()) {
            List<Note> noteList = new ArrayList<>();
            Note note = new Note();
            note.setNote(form.getCompanyNote());
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
        Optional<Contact> companyContact = contactList.stream().filter(c -> c.getName().equals(form.getCompanyContact())).findFirst();
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

    private File getFileFromForm(HttpServletRequest request) throws IOException, ServletException {
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
