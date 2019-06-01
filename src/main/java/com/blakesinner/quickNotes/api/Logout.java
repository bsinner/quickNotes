package com.blakesinner.quickNotes.api;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Log the user out.
 */
@Path("logout")
public class Logout {

    // TODO: revoke access token in the database

    /**
     * Log the user out by deleting the cookie that stores the access token.
     *
     * @param context the servlet context, needed because the cookie path changes between
     *                localhost and live host
     * @return        the response
     */
    @POST
    @Produces("{text/plain}")
    public Response logout(@Context ServletContext context) {
        return Response.status(Response.Status.OK)
                .header(HttpHeaders.SET_COOKIE
                        , "access_token=deleted-cookie; "
                                + "Expires=Thu, 01 Jan 1970 00:00:00 GMT; "
                                + "Path=" + context.getContextPath())
                .entity("")
                .build();
    }

}
