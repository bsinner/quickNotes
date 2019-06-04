package com.blakesinner.quickNotes.util;

import com.blakesinner.quickNotes.entity.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create an access token set to expire in one hour.
 *
 * @author bsinner
 */
public class AccessTokenProvider {

    private static final int LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";
    private static final String KEY_PATH = "/accessTokenPw.txt";

    /**
     * Get the access token string.
     *
     * @param user the user
     * @return     the string
     */
    public static String getToken(User user) {
        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + LIFESPAN);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId());
        claims.put("rol", user.getRolesString());

        JwtBuilder token = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(expiry);

        return token.signWith(
                        Keys.hmacShaKeyFor(new KeyLoader().getKeyBytes(KEY_PATH))
                        , SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get the access token cookie string.
     *
     * @param user        the user
     * @param contextPath the context path, needed because the default
     *                    path may be the base path of the Jersey servlet
     *                    instead of the application's base path
     * @return            the
     */
    public static String get(User user, String contextPath) {
        return "access_token=" + getToken(user)
                + "; Path=" + contextPath
                + "; HttpOnly";
    }

}
