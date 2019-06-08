package com.blakesinner.quickNotes.util;

import com.blakesinner.quickNotes.entity.RefreshToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for creating access and refresh tokens. Also has methods for creating
 * token cookie strings.
 *
 * @author bsinner
 */
public class AccessTokenProvider {

    private static final int LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";
    private static final String KEY_PATH = "/accessTokenPw.txt";
    private static final String REFRESH_NAME = "refresh_token";
    private static final String ACCESS_NAME = "access_token";
    private final Logger LOGGER = LogManager.getLogger(AccessTokenProvider.class);

    /**
     * Create an access token from an user.
     *
     * @param user the user
     * @return     the access token JWT string
     */
    public String createAccessToken(User user) { return createAccessJwt(user); }

    /**
     * Create access token from a refresh token, if the refresh token is expired
     * delete it and return null.
     *
     * @param refreshTokenId the refresh ID, used instead of a RefreshToken
     *                       so the caller doesn't need to find the token in
     *                       DB and check for expiration
     * @return               the access token JWT string, or null if the refresh
     *                       token is expired or not found
     */
    public String createAccessToken(String refreshTokenId) {
        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
        RefreshToken rToken = dao.getByUUID(refreshTokenId);

        if (rToken == null) return null;

        if (isExpired(rToken)) {
            delete(rToken, dao);
            return null;
        }

        return createAccessJwt(rToken.getUser());
    }

    /**
     * Create a refresh token.
     *
     * @param user the refresh token owner
     * @return     the token ID
     */
    public String createRefreshToken(User user) {
        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
        RefreshToken rt = new RefreshToken(user);

        return dao.insertWithUUID(rt).toString();
    }

    /**
     * Create an HTTP Set Cookie string containing an access token.
     *
     * @param token the access token
     * @param path  the path, needed because default may be base path
     *              of the Jersey servlet
     * @return      the cookie string
     */
    public String accessCookieString(String token, String path) {
        return cookieString(ACCESS_NAME, token, path);
    }

    /**
     * Create an HTTP Set Cookie string containing a refresh token.
     *
     * @param token the refresh token
     * @param path  the path, needed because default may be base path
     *              of the Jersey servlet
     * @return      the cookie string
     */
    public String refreshCookieString(String token, String path) {
        return cookieString(REFRESH_NAME, token, path);
    }

    /**
     * Create a JWT access token.
     *
     * @param user the token owner
     * @return     the token string
     */
    private String createAccessJwt(User user) {
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
                        , SignatureAlgorithm.HS256
                ).compact();
    }

    /**
     * Get an HTTP Set Cookie string for a given token.
     *
     * @param name  the type of token
     * @param token the token string
     * @param path  the applications base path
     * @return      the cookie string
     */
    private String cookieString(String name, String token, String path) {
        return name + "=" + token
                + "; Path=" + path
                + "; HttpOnly";
    }

    /**
     * Delete refresh token from DB.
     *
     * @param token the token to delete
     * @param dao   a GenericDAO set to access refresh tokens
     */
    private void delete(RefreshToken token, GenericDAO<RefreshToken> dao) {
        String id = token.getId().toString();
        dao.delete(token);

        if (dao.getByUUID(id) != null) {
           LOGGER.warn("Error: RefreshToken ID: " + id + " could not be deleted.");
        }
    }

    /**
     * Get if refresh token is expired.
     *
     * @param token the refresh token
     * @return      true if the token is expired,
     *              false if it's valid
     */
    private boolean isExpired(RefreshToken token) {
        return token.getExpireDate().isBefore(LocalDateTime.now());
    }

}
