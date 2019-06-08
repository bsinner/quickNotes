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
 * Create an access token set to expire in one hour.
 *
 * @author bsinner
 */
public class AccessTokenProvider {

    private static final int LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";
    private static final String KEY_PATH = "/accessTokenPw.txt";
    private final Logger LOGGER = LogManager.getLogger(AccessTokenProvider.class);

    /**
     * Get the access token string.
     *
     * @param user the user
     * @return     the string
     */
    public String getToken(User user) { return createToken(user); }

    public String refreshAccess(String refreshTokenId) {
        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
        RefreshToken rToken = dao.getByUUID(refreshTokenId);

        if (rToken == null) return null;

        if (isExpired(rToken)) {
            delete(rToken, dao);
            return null;
        }

        return createToken(rToken.getUser());
    }

    private String createToken(User user) {
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

    public String cookieStringFor(String token, String path) {
        return "access_token=" + token
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
