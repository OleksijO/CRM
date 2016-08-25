package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            req.getRequestDispatcher("/pages/authLogout.jsp").forward(req, resp);

        } catch (ServletException | IOException e) {
            Logger.getRootLogger().error("WEB: doGet: forward to logout page failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session != null) {

            User user = (User) session.getAttribute("user");
            Logger.getRootLogger().info("WEB: AUTH: '" + user.getEmail() + "' logged out");

            session.invalidate();
            try {
                res.sendRedirect(res.encodeRedirectURL("/login"));

            } catch (IOException e) {
                Logger.getRootLogger().error("WEB: doPost: redirect to login page failed", e);
            }
        }
    }
}
