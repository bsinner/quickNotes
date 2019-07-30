package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.AccessTokenProvider;
import com.blakesinner.quickNotes.util.StringDigester;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

/**
 * Endpoint for logging users in.
 */
@Path("login")
public class Login {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

    /**
     * Search for the email and password in the database and send an access
     * token in the response if the email and password are valid.
     *
     * @param email    the user email
     * @param password the user password
     * @param context  the request context
     * @return         return Ok response or an Unauthorized response depending
     *                 on if the email and password match
     */
    @POST
    @Produces({"text/plain"})
    public Response authenticate(
            @QueryParam("email") String email
            , @QueryParam("password") String password
            , @Context ServletContext context) {

        User user = queryUser(email, password);

        if (user != null) {
            return buildOkResponse(user, context.getContextPath()
                    , (byte[]) context.getAttribute("authKey"));
        }

        return buildUnauthorizedResponse();
    }

    /**
     * Search for a matching email and password in the database.
     *
     * @param email    the user email
     * @param password the user password
     * @return         the found user, or null of no user was found
     */
    private User queryUser(String email, String password) {
        GenericDAO<User> dao = new GenericDAO<>(User.class);

        Map<String, String> query = new HashMap<>();
        query.put("email", email);
        query.put("password", StringDigester.encrypt(password));

        List<User> foundUsers = dao.getByPropertiesEqual(query);

        if (foundUsers.size() < 1) {
            return null;
        }

        return foundUsers.get(0);
    }

    /**
     * Create a 200 Ok response with access and refresh tokens.
     *
     * @param user the logged in user
     * @param cxt  the context path, needed because the default
     *             path is the REST base path "quickNotes/api"
     * @return     the response
     */
    private Response buildOkResponse(User user, String cxt, byte[] key) {
        AccessTokenProvider provider = new AccessTokenProvider(key);

        String access = provider.createAccessToken(user);
        String refresh = provider.createRefreshToken(user);

        return Response.status(Response.Status.OK)
                .cookie(NewCookie.valueOf(provider.accessCookieString(access, cxt))
                        , NewCookie.valueOf(provider.refreshCookieString(refresh, cxt)))
                .entity(user.getUsername())
                .build();
    }

    /**
     * Create a 401 Unauthorized response.
     *
     * @return the response
     */
    private Response buildUnauthorizedResponse() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE
                        , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build();
    }

}
