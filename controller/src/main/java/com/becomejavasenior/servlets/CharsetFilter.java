package com.becomejavasenior.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 *  Changes encoding to correct use with java.lang.String
 */
@WebFilter(filterName = "CharsetFilter")
public class CharsetFilter implements Filter {
    private FilterConfig config;
    private static final String CONTENT_TYPE="text/html; charset=UTF-8";
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding(config.getInitParameter("codepage"));
        resp.setContentType(CONTENT_TYPE);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

        this.config = config;
    }

}
