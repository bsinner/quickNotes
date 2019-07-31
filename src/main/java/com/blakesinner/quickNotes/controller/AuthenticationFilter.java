package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.AccessTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@WebFilter(filterName = "authentication-filter")
public class AuthenticationFilter implements Filter {

    private static final String ROLES_MAP = "servletRoles";
    private static final String LOGIN_PAGE = "/login.jsp";
    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String ACCESS_JS_COOKIE = "access_token_data";
    private static final String AUTH_KEY = "authKey";
    private static final int MAX_AGE = 60 * 60;

    @Override
    public void init(FilterConfig config) { }

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (isLoggedInAndAuthorized((HttpServletRequest) req
                , (HttpServletResponse) res)) {

            chain.doFilter(req, res);
        } else {
            RequestDispatcher rd = req.getRequestDispatcher(LOGIN_PAGE);
            rd.forward(req, res);
        }
//
//        String url = ((HttpServletRequest) req).getServletPath();
//
//        @SuppressWarnings("unchecked")
//        String[] roles = ((Map<String, String[]>) req.getServletContext().getAttribute(ROLES_MAP))
//                .get(url);

    }

    private boolean isLoggedInAndAuthorized(HttpServletRequest req, HttpServletResponse res) {
        String token = getToken(ACCESS_COOKIE, req.getCookies());

        return token == null || validate(token, req, res);
    }

    private boolean validate(String accessToken, HttpServletRequest req, HttpServletResponse res) {
        ServletContext context = req.getServletContext();

        byte[] key = (byte[]) context.getAttribute(AUTH_KEY);
        String refreshToken = getToken(REFRESH_COOKIE, req.getCookies());

        if (isExpiredOrInvalid(accessToken, key)) {

            if(!tryRefreshToken(req, res, refreshToken, key)) {
                return false;
            }
        }

        String validatedToken = getToken(ACCESS_COOKIE, req.getCookies());

        return validatedToken != null && isAuthorized(validatedToken);
    }

    private boolean isExpiredOrInvalid(String accessToken, byte[] key) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(accessToken);
        } catch (JwtException je) {
            return true;
        }

        return false;
    }

    private boolean tryRefreshToken(HttpServletRequest req, HttpServletResponse res
            , String refreshToken, byte[] key) {

        if (refreshToken != null) {
            AccessTokenProvider provider = new AccessTokenProvider(key);
            String newAccessToken = provider.createAccessToken(refreshToken);

            if (newAccessToken != null) {
                Cookie cookie = new Cookie(ACCESS_COOKIE, newAccessToken);
                cookie.setMaxAge(MAX_AGE);
                cookie.setPath(req.getContextPath());

                return true;
            }
        }

        deleteCookies(req, res);
        return false;
    }

    private void deleteCookies(HttpServletRequest req, HttpServletResponse res) {
        String[] toDelete = { ACCESS_COOKIE, ACCESS_JS_COOKIE, REFRESH_COOKIE };

        for (String s : toDelete) {
            Cookie blankCookie = new Cookie(s, "");
            blankCookie.setMaxAge(0);
            blankCookie.setPath(req.getContextPath());

            res.addCookie(blankCookie);
        }
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

}
