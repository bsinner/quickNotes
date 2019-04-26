package com.blakesinner.quickNotes.util;

import javax.servlet.http.Cookie;

/**
 * Checks passed in Cookie array for a valid access token
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
    public boolean isValid() { return false; }


}