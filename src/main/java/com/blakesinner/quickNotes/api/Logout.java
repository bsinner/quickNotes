package com.blakesinner.quickNotes.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("logout")
public class Logout {

    // TODO: revoke access token in the database

    @POST
    @Produces("{text/plain}")
    public Response logout() {
        return Response.status(Response.Status.OK)
                .header(HttpHeaders.SET_COOKIE
                        , "access_token=deleted-cookie; expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .entity("")
                .build();
    }

}
