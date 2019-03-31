package com.blakesinner.quickNotes.api;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.util.Date;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

@Path("login")
public class Login {

    private static final int TOKEN_LIFESPAN = 60 * 60000;
    private static final String ISSUER = "Quick Notes";

    @POST
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) {

        Date expiry = new Date();
        expiry.setTime(expiry.getTime() + TOKEN_LIFESPAN);

        JwtBuilder accessToken = Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setSubject("<username>|<id>")
                .setExpiration(expiry);

        accessToken.signWith(
                Keys.hmacShaKeyFor("supersecret1111111111111111111111111111".getBytes())
                , SignatureAlgorithm.HS256
        );

        String tokenString = accessToken.compact();

        return Response.status(Response.Status.OK)
                .header(HttpHeaders.SET_COOKIE, "access_token=" + tokenString + "; HttpOnly")
                .build();
    }
}
