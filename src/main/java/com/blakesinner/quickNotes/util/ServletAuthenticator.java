package com.blakesinner.quickNotes.util;

import com.blakesinner.quickNotes.entity.RefreshToken;
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
public class ServletAuthenticator {

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
     * Instantiates a new ServletAuthenticator.
     *
     * @param req the HttpServletRequest
     */
    public ServletAuthenticator(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    /**
     * Check if an access token is valid.
     *
     * @return true if the token is valid, false if the token is invalid
     */
    private boolean isInvalid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(new KeyLoader().getKeyBytes(SECRET))
                    .parseClaimsJws(token);

            return false;
        } catch (ExpiredJwtException eje){
            return true;
        } catch (JwtException je) {
            LOGGER.info("Invalid access token found: " + token);
            return true;
        }

    }

    public boolean isLoggedIn() {
        return getToken(ACCESS_NAME, req.getCookies()).isPresent()
                && !getToken(ACCESS_NAME, toDelete.toArray(new Cookie[0])).isPresent();
    }

    public void updateCookies() {
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

    private void createNewAccessToken(String refreshId) {
        AccessTokenProvider provider = new AccessTokenProvider();
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