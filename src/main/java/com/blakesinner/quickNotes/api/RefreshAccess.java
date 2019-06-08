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
//    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Use refresh token to get new access token.
     *
     * @param cookie the refresh token cookie
     * @param cxt    the context, needed for setting relevant
     *               path in access token cookie
     * @return       the access token cookie response, or 401 Unauthorized
     *               response if the refresh token is invalid
     */
    @POST
    public Response refreshAccessToken(@CookieParam("refresh_token") Cookie cookie, @Context ServletContext cxt) {

        AccessTokenProvider provider = new AccessTokenProvider();

        if (cookie == null) {
            return unauthorized();
        }

        String accessToken = provider.refreshAccess(cookie.getValue());

        if (accessToken == null) {
            return unauthorized();
        }

        return Response.status(200)
                .header(HttpHeaders.SET_COOKIE
                        , provider.cookieStringFor(accessToken, cxt.getContextPath()))
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
//    private RefreshToken getRefreshToken(Cookie cookie) {
//        if (cookie != null) {
//            return AccessTokenProvider.re
//        }
//
//        return null;
//    }

    /**
     * Delete refresh token from the database, used to delete expired tokens.
     *
     * @param token the refresh token
     * @param dao   the DAO
     */
//    private void deleteToken(RefreshToken token, GenericDAO<RefreshToken> dao) {
//        dao.delete(token);
//
//        RefreshToken deleted = dao.getByUUID(token.getId().toString());
//
//        if (deleted != null) {
//            LOGGER.warn("Error: RefreshToken ID: " + token.getId().toString() + " could not be deleted");
//        }
//    }
}
