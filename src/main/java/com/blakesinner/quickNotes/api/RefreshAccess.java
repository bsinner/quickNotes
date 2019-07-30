package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.util.AccessTokenProvider;
import javax.servlet.ServletContext;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * User refresh token to get new access token.
 *
 * @author bsinner
 */
@Path("/refresh")
public class RefreshAccess {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

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

        AccessTokenProvider provider = new AccessTokenProvider((byte[]) cxt.getAttribute("authKey"));

        if (cookie == null) {
            return unauthorized();
        }

        String accessToken = provider.createAccessToken(cookie.getValue());

        if (accessToken == null) {
            return unauthorized();
        }

        return Response.status(200)
                .header(HttpHeaders.SET_COOKIE
                        , provider.accessCookieString(accessToken, cxt.getContextPath()))
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

}
