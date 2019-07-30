package com.blakesinner.quickNotes.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class for updating the access token in the cookies passed to a servlet, and for
 * checking if the user trying to access a servlet is logged in.
 *
 * @author bsinner
 */
public class ServletAuthenticator {

    private HttpServletRequest req;
    private HttpServletResponse res;
    private byte[] key;
    private List<Cookie> toDelete = new ArrayList<>();

    private static final String ACCESS_NAME = "access_token";
    private static final String REFRESH_NAME = "refresh_token";
    private static final String ACCESS_JS_NAME = "access_token_data";
    private static final String SECRET = "/accessTokenPw.txt";
    private static final String LOGIN = "/login.jsp";
    private static final int MAX_SECONDS = 60 * 60;

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Instantiates a new ServletAuthenticator.
     *
     * @param req the HttpServletRequest
     */
    public ServletAuthenticator(HttpServletRequest req, HttpServletResponse res, byte[] key) {
        this.req = req;
        this.res = res;
        this.key = key;
    }

    /**
     * Authenticate the user accessing the servlet and set up the dispatcher to link to
     * the passed in JSP if authenticated, or the login page if unauthenticated.
     *
     * @param authenticatedJsp the page to go to if the user is authenticated
     * @param loginFormAction  the action of the login form displayed to unauthenticated
     *                         users, stored in request attribute "servlet" so it can be
     *                         loaded into the login form with JSP templates
     * @return                 the set up request dispatcher
     */
    public RequestDispatcher setUpDispatcher(String authenticatedJsp, String loginFormAction) {

        updateCookies();

        if (isLoggedIn()) {
            return req.getRequestDispatcher(authenticatedJsp);
        } else {
            req.setAttribute("servlet", loginFormAction);
            return req.getRequestDispatcher(LOGIN);
        }

    }

    /**
     * Check if a cookie with an access token is present.
     *
     * @return true if an access token is found, false if no access token is present or if
     *         the token is scheduled for deletion
     */
    private boolean isLoggedIn() {
        return getToken(ACCESS_NAME, req.getCookies()).isPresent()
                && !getToken(ACCESS_NAME, toDelete.toArray(new Cookie[0])).isPresent();
    }

    /**
     * Update expired or invalid access token cookies and add them to the Servlet
     * response, if no valid refresh token is found delete no longer used cookies.
     */
    private void updateCookies() {
        Optional<String> accessToken = getToken(ACCESS_NAME, req.getCookies());

        if (!accessToken.isPresent()) return;

        if (isInvalid(accessToken.get())) {
            Optional<String> rTokenId = getToken(REFRESH_NAME, req.getCookies());

            if (rTokenId.isPresent()) {
                createNewAccessToken(rTokenId.get());
            } else {
                deleteCookies(ACCESS_NAME, ACCESS_JS_NAME);
            }

        }
    }

    /**
     * Check if an access token is valid.
     *
     * @return true if the token is valid, false if the token is invalid
     */
    private boolean isInvalid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return false;
        } catch (ExpiredJwtException eje){
            return true;
        } catch (JwtException je) {
            LOGGER.info("Invalid access token found: " + token);
            return true;
        }

    }

    /**
     * Create a new access token, delete all authentication related
     * cookies if no valid refresh token can be found.
     *
     * @param refreshId the refresh token ID
     */
    private void createNewAccessToken(String refreshId) {
        AccessTokenProvider provider = new AccessTokenProvider(key);
        String accessToken = provider.createAccessToken(refreshId);

        if (accessToken != null) {
            Cookie cookie = new Cookie(ACCESS_NAME, accessToken);
            cookie.setMaxAge(MAX_SECONDS);
            cookie.setPath(req.getContextPath());

            res.addCookie(cookie);

        } else {
            deleteCookies(REFRESH_NAME, ACCESS_NAME, ACCESS_JS_NAME);
        }
    }

    /**
     * Set the response to delete cookies.
     *
     * @param cookies the cookies to delete
     */
    private void deleteCookies(String... cookies) {
        Arrays.stream(cookies).forEach(c -> {
            Cookie newCookie = new Cookie(c, "");
            newCookie.setMaxAge(0);
            newCookie.setPath(req.getContextPath());

            res.addCookie(newCookie);
            toDelete.add(newCookie);
        });
    }

    /**
     * Search for cookie.
     *
     * @param name    the cookie name
     * @param cookies the array of cookies to search
     * @return        the cookie value, or null if not found
     */
    private Optional<String> getToken(String name, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return Optional.of(c.getValue());
                }
            }
        }

        return Optional.empty();
    }


}