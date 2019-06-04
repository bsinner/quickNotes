package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.RefreshToken;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.UUID;

@Path("/refresh")
public class RefreshAccess {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @POST
    public Response refreshAccessToken(@CookieParam("refresh_token") Cookie cookie) {
        RefreshToken refreshToken = getRefreshToken(cookie);
        
        if (refreshToken == null) {
            return unauthorized();
        }

        String accessToken = null;

        if (accessToken == null) {
            return unauthorized();
        }

        return Response.status(200).build();
    }

    private Response unauthorized() {
        return Response.status(401)
                .header(HttpHeaders.WWW_AUTHENTICATE
                        , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build();
    }

    private RefreshToken getRefreshToken(Cookie cookie) {
        if (cookie != null) {

            GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
            RefreshToken token = dao.getById(UUID.fromString(cookie.getValue()));

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

    private void deleteToken(RefreshToken token, GenericDAO<RefreshToken> dao) {
        dao.delete(token);

        RefreshToken deleted = dao.getById(token.getId());

        if (deleted != null) {
            LOGGER.warn("Error: RefreshToken ID: " + token.getId().toString() + " could not be deleted");
        }
    }
}
