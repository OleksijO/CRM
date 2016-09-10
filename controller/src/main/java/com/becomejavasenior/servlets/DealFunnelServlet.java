package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;
import com.becomejavasenior.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DealFunnelServlet", urlPatterns = "/dealFunnel")
public class DealFunnelServlet extends AbstractSpringAutowiredSupport {
    @Autowired
    private DealService dealService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession (false);


        List<Stage> stageList = dealService.getAllStage();
        session.setAttribute("stageList", stageList);
        session.setAttribute("dealService", dealService);

        List<Deal> dealList = dealService.getAll();
        session.setAttribute("dealList", dealList);

        response.sendRedirect("/pages/dealFunnel.jsp");


    }

    public void setDealService(DealService dealService) {
        this.dealService = dealService;
    }
}