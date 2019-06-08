package com.blakesinner.quickNotes.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Checks passed in Cookie array for an JWT token signed by this webapp.
 *
 * @author bsinner
 */
public class JspFilter {

    private HttpServletRequest req;
    private HttpServletResponse res;
    private List<Cookie> toDelete = new ArrayList<>();

    private static final String ACCESS_NAME = "access_token";
    private static final String REFRESH_NAME = "refresh_token";
    private static final String ACCESS_JS_NAME = "access_token_data";
    private static final String SECRET = "/accessTokenPw.txt";
    private static final int MAX_SECONDS = 60 * 60;

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Instantiates a new JspFilter.
     *
     * @param req the HttpServletRequest
     */
    public JspFilter(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    /**
     * Check if an access token is valid.
     *
     * @return true if the token is valid, false if the token is invalid
     */
    private boolean isValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(new KeyLoader().getKeyBytes(SECRET))
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException eje){
            return false;
        } catch (JwtException je) {
            LOGGER.info("Invalid access token found: " + token);
            return false;

        }

    }

    public boolean isLoggedIn() {
        return getToken(ACCESS_NAME, req.getCookies()).isPresent()
                && !getToken(ACCESS_NAME, toDelete.toArray(new Cookie[0])).isPresent();
    }

    public void updateCookies() {
        Optional<String> token = getToken(ACCESS_NAME, req.getCookies());

        // if access token is missing exit
        if (!token.isPresent()) return;

        // if token is found but not valid try to refresh it
        if (!isValid(token.get())) {
            Optional<String> refreshId = getToken(REFRESH_NAME, req.getCookies());

            // if there is a refresh token try to refresh the access token
            if (refreshId.isPresent()) {
                AccessTokenProvider provider = new AccessTokenProvider();
                String newAccessToken = provider.createAccessToken(refreshId.get());

                // if the access token was refreshed update the response
                if (newAccessToken != null) {
                    Cookie cookie = new Cookie(ACCESS_NAME, newAccessToken);
                    cookie.setMaxAge(MAX_SECONDS);
                    cookie.setPath(req.getContextPath());

                    res.addCookie(cookie);

                // if the access token couldn't be refreshed delete refresh + access cookies
                } else {
                    deleteCookies(REFRESH_NAME, ACCESS_NAME, ACCESS_JS_NAME);
                }

            // if there was no refresh token delete the access token, because it is invalid
            // and impossible to refresh
            } else {
                deleteCookies(ACCESS_NAME, ACCESS_JS_NAME);
            }

        }
    }

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
     * @param name the cookie name
     * @return     the cookie value, or null if not found
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