package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.User;
import com.becomejavasenior.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends AbstractSpringAutowiredSupport {

    private static final int MAX_INACTIVE_INTERVAL = 1800;
    @Autowired
    private UserService userService;
    private Map<String, User> userMap;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            if (req.getParameter("updateUsers") != null) {
                    userMap = userService.getUserMap();
            }
            req.getRequestDispatcher("/pages/authLogin.jsp").forward(req, res);

        } catch (ServletException | IOException e) {
            Logger.getRootLogger().error("WEB: doGet: forward to login page failed", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        User user = null;
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fromPage = req.getParameter("fromPage");

        if (fromPage == null || fromPage.isEmpty()|| fromPage.equals("/")) {
            //todo go to dashboard
            fromPage = "/logout";
        }

        if (email != null && !email.isEmpty() && password != null) {
            user = userMap.get(email);
        }

        if (user != null && password.equals(user.getPassword())) {

            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);

            Cookie userName = new Cookie("user", email);
            userName.setSecure(true);
            userName.setMaxAge(MAX_INACTIVE_INTERVAL);
            res.addCookie(userName);

            Logger.getRootLogger().info("WEB: AUTH: '" + email + "' logged in");
        } else {
            req.getSession().setAttribute("authMessage", "Предоставлены неверные учетные данные");
            Logger.getRootLogger().info("WEB: AUTH: login failed for '" + email + "'");
        }

        try {
            res.sendRedirect(res.encodeRedirectURL(fromPage));
        } catch (IOException e) {
            Logger.getRootLogger().error("WEB: doPost: redirect to page " + fromPage + " failed", e);
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
