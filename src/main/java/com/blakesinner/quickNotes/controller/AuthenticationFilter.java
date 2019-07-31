package com.blakesinner.quickNotes.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "authentication-filter")
public class AuthenticationFilter implements Filter {

    private static final String ROLES_MAP = "servletRoles";
    private static final String LOGIN_PAGE = "/login.jsp";
    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String ACCESS_JS_COOKIE = "access_token_data";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (isLoggedIn()) {
            chain.doFilter(req, res);
        } else {
            RequestDispatcher rd = req.getRequestDispatcher(LOGIN_PAGE);
            rd.forward(req, res);
        }

        String url = ((HttpServletRequest) req).getServletPath();

        @SuppressWarnings("unchecked")
        String[] roles = ((Map<String, String[]>) req.getServletContext().getAttribute(ROLES_MAP))
                .get(url);

    }

    private boolean isLoggedIn(HttpServletRequest req) {
        String token = getToken(ACCESS_COOKIE, req.getCookies());

        return token == null || validate(token);
    }

    private String getToken(String name, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c.getValue();
                }
            }
        }

        return null;
    }

    @Override
    public void init(FilterConfig config) { }

    @Override
    public void destroy() {}

}
