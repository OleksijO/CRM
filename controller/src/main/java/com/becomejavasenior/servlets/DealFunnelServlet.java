package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.Deal;
import com.becomejavasenior.entity.Stage;
import com.becomejavasenior.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DealFunnelServlet", urlPatterns = "/dealFunnel")
public class DealFunnelServlet extends HttpServlet {
    @Autowired
    @Qualifier("dealService")
    private DealService dealService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        //    OR:
        //ApplicationContext ac = (ApplicationContext) config.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        //this.dealService = (DealService)ac.getBean("dealService");
    }

//    private static final Logger logger = Logger.getLogger(DealFunnelServlet.class);

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

}