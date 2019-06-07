package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.RefreshToken;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.AccessTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

/**
 * User refresh token to get new access token.
 *
 * @author bsinner
 */
@Path("/refresh")
public class RefreshAccess {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Use refresh token to get new access token.
     *
     * @param cookie  the refresh token cookie
     * @param context the context, needed for setting relevant
     *                path in access token cookie
     * @return        the access token cookie response, or 401 Unauthorized
     *                response if the refresh token is invalid
     */
    @POST
    public Response refreshAccessToken(@CookieParam("refresh_token") Cookie cookie, @Context ServletContext context) {
        RefreshToken refreshToken = getRefreshToken(cookie);

        if (refreshToken == null) {
            return unauthorized();
        }

        String accessTokenCookie = AccessTokenProvider
                .get(refreshToken.getUser(), context.getContextPath());

        return Response.status(200)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
                .build();
    }

    /**
     * Get Unauthorized response.
     *
     * @return the Unauthorized response
     */
    private Response unauthorized() {
        return Response.status(401)
                .header(HttpHeaders.WWW_AUTHENTICATE
                        , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build();
    }

    /**
     * Try to get the refresh token in the cookie.
     *
     * @param cookie the refresh token cookie
     * @return       the refresh token, or null if the
     *               token is invalid or not found
     */
    private RefreshToken getRefreshToken(Cookie cookie) {
        if (cookie != null) {

            GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
            RefreshToken token = dao.getByUUID(cookie.getValue());

            if (token != null) {
                if (token.getExpireDate().isBefore(LocalDateTime.now())) {
                    deleteToken(token, dao);
                    return null;
                } else {
                    return token;
                }

            }

            LOGGER.warn(String.format("Error: Refresh cookie with token that could not be found detected%n"
                    + "Token ID: " + cookie.getValue()
            ));

        }

        return null;
    }

    /**
     * Delete refresh token from the database, used to delete expired tokens.
     *
     * @param token the refresh token
     * @param dao   the DAO
     */
    private void deleteToken(RefreshToken token, GenericDAO<RefreshToken> dao) {
        dao.delete(token);

        RefreshToken deleted = dao.getByUUID(token.getId().toString());

        if (deleted != null) {
            LOGGER.warn("Error: RefreshToken ID: " + token.getId().toString() + " could not be deleted");
        }
    }
}
