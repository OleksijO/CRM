package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
public class ViewCompaniesServlet extends HttpServlet {
    @Autowired
    private CompanyService companyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companyList = companyService.getAll();
        companyList.sort((Company company1, Company company2) -> company1.getName().compareTo(company2.getName()));
        request.setAttribute("companyList", companyList);

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/pages/viewcompanies.jsp");
        requestDispatcher.forward(request, response);
    }
}