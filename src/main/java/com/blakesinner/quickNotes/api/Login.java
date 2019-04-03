package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
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

    /**
     * Search for the email and password in the database and send an access
     * token in the response if the email and password are valid.
     *
     * @param email    the user email
     * @param password the user password
     * @return         return Ok response or an Unauthorized response depending
     *                 on if the email and password match
     */
    @POST
    @Produces({"text/plain"})
    public Response authenticate(@QueryParam("email") String email, @QueryParam("password") String password) {

        User user = queryUser(email, password);

        if (user != null) {
            return buildOkResponse(user);
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
     * Create a 200 Ok response with an access token set as it's cookie.
     *
     * @param user the logged in user
     * @return     the response
     */
    private Response buildOkResponse(User user) {
        Date expiry = new Date();
            expiry.setTime(expiry.getTime() + TOKEN_LIFESPAN);

            JwtBuilder accessToken = Jwts.builder()
                    .setIssuer(ISSUER)
                    .setIssuedAt(new Date())
                    .setSubject(user.getUsername() + "|" + user.getId())
                    .setExpiration(expiry);

            accessToken.signWith(
                    Keys.hmacShaKeyFor("supersecret1111111111111111111111111111".getBytes())
                    , SignatureAlgorithm.HS256
            );

            String tokenString = accessToken.compact();

            return Response.status(Response.Status.OK)
                    .header(HttpHeaders.SET_COOKIE, "access_token=" + tokenString + "; HttpOnly")
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
