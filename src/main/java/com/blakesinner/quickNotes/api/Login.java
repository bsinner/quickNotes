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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("login")
public class Login {

    private static final int TOKEN_LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";
    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

    @POST
    @Produces({"text/plain"})
    public Response authenticate(@QueryParam("email") String email, @QueryParam("password") String password) {

        Map<String, String> results = queryUser(email, password);

        if (results != null) {
            Date expiry = new Date();
            expiry.setTime(expiry.getTime() + TOKEN_LIFESPAN);

            JwtBuilder accessToken = Jwts.builder()
                    .setIssuer(ISSUER)
                    .setIssuedAt(new Date())
                    .setSubject(results.get("username") + "|" + results.get("id"))
                    .setExpiration(expiry);

            accessToken.signWith(
                    Keys.hmacShaKeyFor("supersecret1111111111111111111111111111".getBytes())
                    , SignatureAlgorithm.HS256
            );

            String tokenString = accessToken.compact();

            return Response.status(Response.Status.OK)
                    .header(HttpHeaders.SET_COOKIE, "access_token=" + tokenString + "; HttpOnly")
                    .entity(results.get("username"))
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE
                    , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build();
    }

    // TODO: create by propertiesEqual dao method and search for email and password combination
    private Map<String, String> queryUser(String email, String password) {
        GenericDAO<User> dao = new GenericDAO<>(User.class);

        List<User> foundUsers = dao.getByPropertyEqual("email", email);

        if (foundUsers.size() < 1 || !foundUsers.get(0).getPassword().equals(password)) {
            return null;
        }

        Map<String, String> results = new HashMap<>();

        results.put("username", foundUsers.get(0).getUsername());
        results.put("id", String.valueOf(foundUsers.get(0).getId()));

        return results;
    }

}
