package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.AccessTokenProvider;
import io.jsonwebtoken.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Authentication filter for servlets, to apply authorization to a servlet
 * add the Secured annotation and define at least url pattern in the
 * WebServlet annotation.
 *
 * @author bsinner
 */
@WebFilter(filterName = "authentication-filter")
public class AuthenticationFilter implements Filter {

    private static final String ROLES_MAP = "servletRoles";
    private static final String LOGIN_PAGE = "/login.jsp";
    private static final String ACCESS_COOKIE = "access_token";
    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String ACCESS_JS_COOKIE = "access_token_data";
    private static final String ROLES_CLAIM = "rol";
    private static final String AUTH_KEY = "authKey";
    private static final int MAX_AGE = 60 * 60;

    /**
     * Init method for interface.
     *
     * @param config the filter config
     */
    @Override
    public void init(FilterConfig config) { }

    /**
     * Destroy method for interface.
     */
    @Override
    public void destroy() {}

    /**
     * If the user is logged in and authorized continue the request, otherwise forward
     * to the login page.
     *
     * @param req               the request object
     * @param res               the response object
     * @param chain             the filter chain
     * @throws IOException      if an I/O exception occurs
     * @throws ServletException if a Servlet exception occurs
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (isLoggedInAndAuthorized((HttpServletRequest) req
                , (HttpServletResponse) res)) {

            chain.doFilter(req, res);
        } else {
            req.setAttribute("servlet", ((HttpServletRequest) req).getServletPath());
            RequestDispatcher rd = req.getRequestDispatcher(LOGIN_PAGE);
            rd.forward(req, res);
        }

    }

    /**
     * Check if the user is logged in and has the required role to access a servlet.
     *
     * @param req the request object
     * @param res the response object
     * @return    true if the user can access the servlet, false if they must log in
     */
    private boolean isLoggedInAndAuthorized(HttpServletRequest req, HttpServletResponse res) {
        ServletContext context = req.getServletContext();
        String refreshToken = getToken(REFRESH_COOKIE, req.getCookies());
        String accessToken = getToken(ACCESS_COOKIE, req.getCookies());

        byte[] key = (byte[]) context.getAttribute(AUTH_KEY);

        if (isExpiredOrInvalid(accessToken, key)) {

            String newToken = tryRefreshToken(req, res, refreshToken, key);

            if (newToken == null) {
                return false;
            }

            accessToken = newToken;
        }

        return isAuthorized(accessToken, context, key, req.getServletPath());
    }

    /**
     * Get if token is expired or malformed.
     *
     * @param accessToken the token to verify
     * @param key         the secret key
     * @return            true if the token is invalid, false otherwise
     */
    private boolean isExpiredOrInvalid(String accessToken, byte[] key) {
        if (accessToken != null) {
            try {
                Jwts.parser()
                        .setSigningKey(key)
                        .parseClaimsJws(accessToken);

                return false;
            } catch (JwtException je) {
                return true;
            }
        }

        return true;
    }

    /**
     * Try to refresh an access token.
     *
     * @param req          the request object
     * @param res          the response object
     * @param refreshToken the refresh token
     * @param key          the secret key
     * @return             the new token string, or null if the token couldn't be refreshed
     */
    private String tryRefreshToken(HttpServletRequest req, HttpServletResponse res
            , String refreshToken, byte[] key) {

        if (refreshToken != null) {
            AccessTokenProvider provider = new AccessTokenProvider(key);
            String newAccessToken = provider.createAccessToken(refreshToken);

            if (newAccessToken != null) {
                Cookie cookie = new Cookie(ACCESS_COOKIE, newAccessToken);
                cookie.setMaxAge(MAX_AGE);
                cookie.setPath(req.getContextPath());

                res.addCookie(cookie);
                return newAccessToken;
            }
        }

        deleteCookies(req, res);
        return null;
    }

    /**
     * Get if user is authorized to access servlet, if a servlet has no roles associated with it
     * any user will be authorized to access it.
     *
     * @param accessToken the access token
     * @param context     the servlet context
     * @param key         the secret key
     * @param url         the url of the servlet the request is trying to access
     * @return            true if the user is authorized, false if they are unauthorized
     */
    private boolean isAuthorized(String accessToken, ServletContext context, byte[] key, String url) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(accessToken);

            @SuppressWarnings("unchecked")
            String[] servletRoles = ((Map<String, String[]>) context.getAttribute(ROLES_MAP))
                    .get(url);

            return compareRoles(servletRoles, claimsJws.getBody());

        } catch (JwtException je) {
            return false;
        }
    }

    /**
     * Delete access token cookie, js cookie and refresh token cookie.
     *
     * @param req the request object
     * @param res the response object
     */
    private void deleteCookies(HttpServletRequest req, HttpServletResponse res) {
        String[] toDelete = { ACCESS_COOKIE, ACCESS_JS_COOKIE, REFRESH_COOKIE };

        for (String s : toDelete) {
            Cookie blankCookie = new Cookie(s, "");
            blankCookie.setMaxAge(0);
            blankCookie.setPath(req.getContextPath());

            res.addCookie(blankCookie);
        }
    }

    /**
     * Find if a user is in at least one of the servlet roles.
     *
     * @param servletRoles the servlet roles.
     * @param claims       the access token's claims object
     * @return             true if the user is in at least one servlet role, false otherwise
     */
    private boolean compareRoles(String[] servletRoles, Claims claims) {
        if (servletRoles == null || servletRoles.length == 0) {
            return true;
        }

        List<String> userRoles = Arrays.asList(claims.get(ROLES_CLAIM)
                .toString().split(" ")
        );

        for (String s : servletRoles) {
            if (userRoles.contains(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find a token in an array of cookie objects.
     *
     * @param name    the name of the cookie containing the token
     * @param cookies the cookie array
     * @return        the found token, or null if no token could be found
     */
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
