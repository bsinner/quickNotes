package com.blakesinner.quickNotes.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.Cookie;
import java.util.Optional;

/**
 * Checks passed in Cookie array for an JWT token signed by this webapp.
 *
 * @author bsinner
 */
public class JspFilter {

    private Cookie[] cookies;
    private static final String NAME = "access_token";
    private final Logger logger = LogManager.getLogger(this.getClass());
    private static final String SECRET = "/accessTokenPw.txt";

    /**
     * Instantiates a new JspFilter.
     *
     * @param cookies the cookies
     */
    public JspFilter(Cookie[] cookies) { this.cookies = cookies; }

    /**
     * Check if the cookies contain a valid access token.
     *
     * @return true or false depending on if the access token is valid
     */
    private boolean isValid(String token) {
//        Optional<String> token = getToken();
//
//        if (!token.isPresent()) return false;

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
        Optional<String> token = getToken();

        if (!token.isPresent()) return;

        if (isValid(token.get())) return;

        // make a refresh xhr
    }

    /**
     * Get the value of the cookie with name equal to instance
     * variable NAME.
     *
     * @return cookie value, or null of no matching token is found
     */
    private Optional<String> getToken() {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(NAME)) {
                    return Optional.of(c.getValue());
                }
            }
        }

        return Optional.empty();
    }


}