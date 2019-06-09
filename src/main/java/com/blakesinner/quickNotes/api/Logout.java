package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.RefreshToken;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

/**
 * Log the user out.
 *
 * @author bsinner
 */
@Path("logout")
public class Logout {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    /**
     * Log the user out by deleting the cookies that stores the authentication data.
     *
     * @param context the servlet context, needed because the cookie path changes between
     *                localhost and live host
     * @return        the response
     */
    @POST
    public Response logout(@Context ServletContext context, @CookieParam("refresh_token") Cookie token) {

        deleteRefreshToken(token);

        return Response.status(Response.Status.OK)
                .cookie(NewCookie.valueOf(
                                "access_token=deleted-cookie; "
                                + "Expires=Thu, 01 Jan 1970 00:00:00 GMT; "
                                + "Path=" + context.getContextPath())
                        , NewCookie.valueOf(
                                "refresh_token=deleted-cookie2; "
                                + "Expires=Thu, 01 Jan 1970 00:00:00 GMT; "
                                + "Path=" + context.getContextPath())
                        , NewCookie.valueOf(
                            "access_token_data=deleted-cookie3; "
                            + "Expires=Thu, 01 Jan 1970 00:00:00 GMT; "
                            + "Path=" + context.getContextPath()))
                .build();
    }

    /**
     * Search for the refresh token in the database and delete if found.
     *
     * @param token the token to delete
     */
    private void deleteRefreshToken(Cookie token) {
        if (token == null) return;

        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);
        RefreshToken toDelete = dao.getByUUID(token.getValue());

        if (toDelete == null) {
            LOGGER.warn(String.format("Error: Refresh token cookie contained token that could not be found"
                    + "%nToken ID: " + token.getValue() + "%n"
            ));

            return;
        }

        dao.delete(toDelete);
    }

}
