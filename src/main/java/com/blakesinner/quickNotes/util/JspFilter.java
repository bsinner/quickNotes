package com.blakesinner.quickNotes.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Checks passed in Cookie array for an JWT token signed by this webapp.
 *
 * @author bsinner
 */
public class JspFilter {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private static final String ACCESS_NAME = "access_token";
    private static final String REFRESH_NAME = "refresh_token";
    private final Logger logger = LogManager.getLogger(this.getClass());
    private static final String SECRET = "/accessTokenPw.txt";

    /**
     * Instantiates a new JspFilter.
     *
     * @param req the HttpServletRequest
     */
    public JspFilter(HttpServletRequest req, HttpServletResponse res) {
        this.request = req;
        this.response = res;
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
            logger.info("Invalid access token found: " + token);
            return false;

        }

    }

    public void updateCookies() {
        Optional<String> token = getToken(ACCESS_NAME);

        if (!token.isPresent()) return;

        if (!isValid(token.get())) {
            Optional<String> refreshId = getToken(REFRESH_NAME);

            if (refreshId.isPresent()) {
                //refresh the token
                // if (refreshFail) {
                //     delete refresh + access
                // }
            } else {
                // delete access token
            }

        }
    }

    private void deleteCookies(String... cookies) {
        Arrays.stream(request.getCookies()).forEach(c -> {
            Cookie cookie = new Cookie(c.getName(), "");
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        });
    }

    /**
     * Search for cookie.
     *
     * @param name the cookie name
     * @return     the cookie value, or null if not found
     */
    private Optional<String> getToken(String name) {
        Cookie[] cookies = request.getCookies();

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