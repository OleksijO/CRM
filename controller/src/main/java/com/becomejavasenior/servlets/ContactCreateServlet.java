package com.becomejavasenior.servlets;

import com.becomejavasenior.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "contactCreateServlet", urlPatterns = "/contactcreate")
@MultipartConfig(maxFileSize = 102400)
public class ContactCreateServlet extends AbstractSpringAutowiredSupport {
    @Autowired
    private ContactService contactService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("userList", contactService.getUserList());
        request.setAttribute("companyList", contactService.getCompanyList());
        request.setAttribute("stageList", contactService.getStageList());
        request.setAttribute("taskTypeList", contactService.getTaskTypesList());
        request.setAttribute("typeOfPhoneArray", contactService.getPhoneTypes());
        request.setAttribute("typeOfPeriodArray", contactService.getPeriodTypes());
        request.setAttribute("taskTimeList", contactService.getTaskTimeList());

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/pages/contactCreate.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.error("WEB: forward to contact create failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        try {
            contactService.createByParameters(request.getParameterMap(), request.getPart("file_file"));
        } catch (IOException | ServletException e) {
            logger.error("WEB: error while parse or create contact", e);
        }
    }

    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }
}
