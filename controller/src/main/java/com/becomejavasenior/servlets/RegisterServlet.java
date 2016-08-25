package com.becomejavasenior.servlets;

import com.becomejavasenior.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private static final String URL_REGISTER = "/pages/authRegister.jsp";
    @Autowired
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("languageList", userService.getLanguageList());
            req.getRequestDispatcher(URL_REGISTER).forward(req, resp);

        } catch (ServletException | IOException e) {
            Logger.getRootLogger().error("WEB: doGet: forward to register page failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String serviceMessage = userService.createNewUser(
                req.getParameter("name"), req.getParameter("password"),
                req.getParameter("email"), Integer.parseInt(req.getParameter("language")));

        if ("".equals(serviceMessage)) {
            Logger.getRootLogger().info("WEB: AUTH: new user registered '" + req.getParameter("email") + "'");

            try {
                resp.sendRedirect("/login?updateUsers=1");
            } catch (IOException e) {
                Logger.getRootLogger().error("WEB: doPost: redirect to login page failed", e);
            }
        } else {
            req.setAttribute("serviceMessage", serviceMessage);
            try {
                req.getRequestDispatcher(URL_REGISTER).forward(req, resp);
            } catch (ServletException | IOException e) {
                Logger.getRootLogger().error("WEB: doPost: forward to register page failed", e);
            }
        }
    }
}
