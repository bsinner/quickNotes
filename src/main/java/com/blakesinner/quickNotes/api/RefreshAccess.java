package com.blakesinner.quickNotes.api;

import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("/refresh")
public class RefreshAccess {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

    @POST
    public Response refreshAccessToken(@CookieParam("refresh_token") Cookie refreshToken) {

        if (refreshToken == null) {
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

}
