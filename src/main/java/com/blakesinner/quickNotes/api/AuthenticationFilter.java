package com.blakesinner.quickNotes.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        Cookie accessTokenCookie = context.getCookies().get("access_token");

        if (accessTokenCookie != null) {
            String accessTokenString = accessTokenCookie.getValue();
            validateAccessToken(accessTokenString, context);
            return;
        }

        sendUnauthorized(context);
    }

    private void validateAccessToken(String token, ContainerRequestContext context) {
        try {
            Jws<Claims> parsedClaims = Jwts.parser()
                    .setSigningKey("supersecret1111111111111111111111111111".getBytes())
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            sendUnauthorized(context);
        }
    }

    private void sendUnauthorized(ContainerRequestContext context) {
        context.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE
                        , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build()
        );
    }

}
