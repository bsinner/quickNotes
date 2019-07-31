package com.blakesinner.quickNotes.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "authentication-filter")
public class AuthenticationFilter implements Filter {

    private static final String ROLES_MAP = "servletRoles";

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String url = ((HttpServletRequest) req).getServletPath();

        @SuppressWarnings("unchecked")
        String[] roles = ((Map<String, String[]>) req.getServletContext().getAttribute(ROLES_MAP))
                .get(url);

        chain.doFilter(req, res);
    }

    public void init(FilterConfig config) { }

    public void destroy() {}

}
