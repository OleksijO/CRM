package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.entity.Contact;
import com.becomejavasenior.service.ContactService;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "contactCompanyViewServlet", urlPatterns = "/viewcompanies")
public class ContactCompanyViewServlet extends AbstractSpringAutowiredSupport {
    @Autowired
    private ContactService contactService;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Company> companyList = contactService.getCompanyList();
        companyList.sort((Company company1, Company company2) -> company1.getName().compareTo(company2.getName()));
        request.setAttribute("companyList", companyList);

        List<Contact> contactList = contactService.getAll();
        contactList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        request.setAttribute("contactList", contactList);
        request.setAttribute("userList", contactService.getUserList());
        request.setAttribute("tagList", contactService.getTagList());

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/pages/viewcompanies.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            logger.error("WEB: forward to contact/company view failed", e);
        }
    }
}