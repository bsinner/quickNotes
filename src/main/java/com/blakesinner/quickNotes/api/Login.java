package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.RefreshToken;
import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import com.blakesinner.quickNotes.util.KeyLoader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Ref;
import java.util.*;

/**
 * Endpoint for logging users in.
 */
@Path("login")
public class Login {

    private static final int TOKEN_LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";
    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET = "/accessTokenPw.txt";

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
            , @Context ServletContext context)
    {

        User user = queryUser(email, password);

        if (user != null) {
            return buildOkResponse(user, context.getContextPath());
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
        query.put("password", password);

        List<User> foundUsers = dao.getByPropertiesEqual(query);

        if (foundUsers.size() < 1) {
            return null;
        }

        return foundUsers.get(0);
    }

    /**
     * Create a 200 Ok response with access and refresh tokens.
     *
     * @param user        the logged in user
     * @param contextPath the context path, needed because the default
     *                    path is the REST base path "quickNotes/api"
     * @return            the response
     */
    private Response buildOkResponse(User user, String contextPath) {

        return Response.status(Response.Status.OK)
                .cookie(NewCookie.valueOf(
                                "access_token=" + getAccessToken(user)
                                + "; Path=" + contextPath
                                + "; HttpOnly")
                        , NewCookie.valueOf(
                                "refresh_token=" + getRefreshToken(user)
                                + "; Path=" + contextPath
                                + "; HttpOnly"
                        ))
                .entity(user.getUsername())
                .build();
    }

    /**
     * Create and get the access token.
     *
     * @param user the current user
     * @return     the access token converted to string
     */
    private String getAccessToken(User user) {
        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + TOKEN_LIFESPAN);

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getId());
        claims.put("rol", user.getRolesString());

        JwtBuilder accessToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(expiry);

        accessToken.signWith(
                Keys.hmacShaKeyFor(new KeyLoader().getKeyBytes(SECRET))
                , SignatureAlgorithm.HS256
        );

        return accessToken.compact();
    }

    /**
     * Create a refresh token.
     *
     * @param user the user account refreshed by the token
     * @return     the refresh token id
     */
    private String getRefreshToken(User user) {
        GenericDAO<RefreshToken> dao = new GenericDAO<>(RefreshToken.class);

        RefreshToken rt = new RefreshToken(user);

        return dao.insertWithUUID(rt).toString();
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
