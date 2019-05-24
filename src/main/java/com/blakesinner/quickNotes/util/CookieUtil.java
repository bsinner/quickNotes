package com.blakesinner.quickNotes.util;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Cookie;
import java.util.List;

/**
 * Class with utilities for parsing cookies with access tokens.
 *
 * @author bsinner
 */
public class CookieUtil {

    private static final Logger LOGGER = LogManager.getLogger(CookieUtil.class);
    private static final String PATH = "/accessTokenPw.txt";

    /**
     * Get the user object from the cookie.
     *
     * @param cookie the cookie to parse
     * @return       the found user, or null if no user could be found
     */
    public static User jaxRsGetUser(Cookie cookie) {
        String id = jaxRsGetId(cookie);

        if (id != null) {
            List<User> users = new GenericDAO<>(User.class)
                    .getByPropertyEqual("id", id);

            if (users.size() > 0) {
                return users.get(0);
            }

        }

        return null;
    }

    /**
     * Get the user ID from the cookie.
     *
     * @param cookie the cookie to parse
     * @return       the found ID, or null if none could be found
     */
    public static String jaxRsGetId(Cookie cookie) {
        if (cookie != null) {
            String token = cookie.getValue();

            if (token != null) {
                Jws<Claims> claims = parseToken(token);

                if (claims != null) {
                    return claims.getBody().getSubject();
                }

            }

        }

        return null;
    }

    /**
     * Get claims in the JWT token.
     *
     * @param token the token to parse
     * @return      the found claims, or null if the token couldn't be parsed
     */
    private static Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(new KeyLoader().getKeyBytes(PATH))
                    .parseClaimsJws(token);
        } catch (JwtException je) {
            LOGGER.trace(je);
        }

        return null;
    }

}