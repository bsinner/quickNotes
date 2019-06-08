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
     * Create access token string.
     *
     * @param refreshTokenId the refresh token id
     * @return the string
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
     * Create refresh token string.
     *
     * @param user the user
     * @return the string
     */
    public String createRefreshToken(User user) {
        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
        RefreshToken rt = new RefreshToken(user);

        return dao.insertWithUUID(rt).toString();
    }

    /**
     * Access cookie string string.
     *
     * @param token the token
     * @param path  the path
     * @return the string
     */
    public String accessCookieString(String token, String path) {
        return cookieString(ACCESS_NAME, token, path);
    }

    /**
     * Refresh cookie string string.
     *
     * @param token the token
     * @param path  the path
     * @return the string
     */
    public String refreshCookieString(String token, String path) {
        return cookieString(REFRESH_NAME, token, path);
    }

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

    private String cookieString(String name, String token, String path) {
        return name + "=" + token
                + "; Path=" + path
                + "; HttpOnly";
    }

    private void delete(RefreshToken token, GenericDAO<RefreshToken> dao) {
        String id = token.getId().toString();
        dao.delete(token);

        if (dao.getByUUID(id) != null) {
           LOGGER.warn("Error: RefreshToken ID: " + id + " could not be deleted.");
        }
    }

    private boolean isExpired(RefreshToken token) {
        return token.getExpireDate().isBefore(LocalDateTime.now());
    }

}
