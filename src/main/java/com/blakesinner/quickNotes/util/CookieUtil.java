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

public class CookieUtil {

    private static final Logger LOGGER = LogManager.getLogger(CookieUtil.class);
    private static final String PATH = "/accessTokenPw.txt";

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
