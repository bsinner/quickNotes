package com.blakesinner.quickNotes.util;

import javax.servlet.http.Cookie;

/**
 * Filters cookies to see if they contain a valid access token.
 *
 * @author bsinner
 */
public class JspFilter {

    private Cookie cookie;

    /**
     * Instantiates a new JspFilter.
     *
     * @param cookie the cookie
     */
    public JspFilter(Cookie cookie) { this.cookie = cookie; }

    /**
     * Check if a cookie contains a valid access token.
     *
     * @return true or false depending on if the access token is valid
     */
    public boolean filterCookie() { return false; }

    /**
     * Gets cookie.
     *
     * @return the cookie
     */
    public Cookie getCookie() { return cookie; }

    /**
     * Sets cookie.
     *
     * @param cookie the cookie
     * @return       the cookie
     */
    public Cookie setCookie(Cookie cookie) { this.cookie = cookie; }

}
