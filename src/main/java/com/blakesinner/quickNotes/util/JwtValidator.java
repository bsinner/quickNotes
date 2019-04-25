package com.blakesinner.quickNotes.util;

/**
 * Used for validating JWT tokens.
 *
 * @author bsinner
 */
public class JwtValidator {

    private String token;

    /**
     * Instantiates a new JspFilter.
     *
     * @param token the access token
     */
    public JwtValidator(String token) { this.token = token; }

    /**
     * Check if a token is signed by this webapp.
     *
     * @return true or false depending on if the access token is valid
     */
    public boolean validateToken() {

        return false;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) { this.token = token; }

}
