package com.blakesinner.quickNotes.api;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

@Path("/refresh")
public class RefreshAccess {

    @POST
    public Response refreshAccessToken(@CookieParam("refresh_token") Cookie refreshToken) {
        
        return Response.status(200).build();
    }

}
