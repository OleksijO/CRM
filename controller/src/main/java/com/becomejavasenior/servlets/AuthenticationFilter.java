package com.becomejavasenior.servlets;

import com.becomejavasenior.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private static final String LOGIN_PAGE = "/login";
    private static final String REGISTER_PAGE = "/register";

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // nothing to init
    }

    @Override
    public void destroy() {
        // nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        if (user != null || LOGIN_PAGE.equals(requestURI) || REGISTER_PAGE.equals(requestURI)) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute("fromPage", requestURI);
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
        }
    }
}