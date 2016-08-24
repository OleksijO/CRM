package com.becomejavasenior.servlets;

import com.becomejavasenior.service.ContactService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "contactCreateServlet", urlPatterns = "/contactcreate")
@MultipartConfig(maxFileSize = 102400)
@Controller("contactCreateServlet")
public class ContactCreateServlet extends HttpServlet {
    @Autowired
    private ContactService contactService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

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
            Logger.getRootLogger().error("WEB: forward to contact create failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.getRootLogger().error(e);
        }
        try {
            contactService.createByParameters(request.getParameterMap(), request.getPart("file_file"));
        } catch (IOException | ServletException e) {
            Logger.getRootLogger().error("WEB: error while parse or create contact", e);
        }
    }
}
