package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DealListServlet", urlPatterns = "/dealList")
public class DealListServlet extends AbstractSpringAutowiredSupport {
    @Autowired
    private DealService dealService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        List<Deal> dealList = dealService.getDealsForList();

        session.setAttribute("dealList", dealList);
        session.setAttribute("dealService", dealService);
        response.sendRedirect("/pages/dealList.jsp");

    }

    public void setDealService(DealService dealService) {
        this.dealService = dealService;
    }
}