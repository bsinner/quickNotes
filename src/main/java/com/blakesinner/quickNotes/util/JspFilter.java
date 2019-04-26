package com.blakesinner.quickNotes.util;

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
    public boolean isValid() {
        Optional<String> token = getToken();

        if (!token.isPresent()) return false;

        return true;
    }

    /**
     * Get the value of the cookie with name equal to instance
     * variable NAME.
     *
     * @return cookie value, or null of no matching token is found
     */
    private Optional<String> getToken() {
        for (Cookie c : cookies) {
            if (c.getName().equals(NAME)) {
                return Optional.of(c.getValue());
            }
        }

        return Optional.empty();
    }


}