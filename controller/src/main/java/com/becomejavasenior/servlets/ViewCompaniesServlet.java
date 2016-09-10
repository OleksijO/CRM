package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Company;
import com.becomejavasenior.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
public class ViewCompaniesServlet extends AbstractSpringAutowiredSupport {
    @Autowired
    private CompanyService companyService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Company> companyList = companyService.getAll();
        companyList.sort((Company company1, Company company2) -> company1.getName().compareTo(company2.getName()));
        request.setAttribute("companyList", companyList);

        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/WEB-INF/pages/viewcompanies.jsp");
        requestDispatcher.forward(request, response);
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }
}