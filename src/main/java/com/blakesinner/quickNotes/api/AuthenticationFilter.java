package com.blakesinner.quickNotes.api;

import io.jsonwebtoken.*;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.util.*;

/**
 * Filter that can be applied to api endpoints to validate them.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";

    /**
     * If an access token was included in the request, call a method to validate it,
     * otherwise send 401 Unauthorized.
     *
     * @param context the request context
     */
    @Override
    public void filter(ContainerRequestContext context) {

        Cookie accessTokenCookie = context.getCookies().get("access_token");

        if (accessTokenCookie != null) {
            Map<String, String[]> userIdAndRoles = validateAccessToken(accessTokenCookie, context);

            addSecurityContext(context, userIdAndRoles);
            return;
        }

        sendUnauthorized(context);
    }

    /**
     * Try to parse the access token, if there is an error parsing the token an exception
     * is thrown and the token is considered invalid.
     *
     * @param token   the access token
     * @param context the request context
     */
    private Map<String, String[]> validateAccessToken(Cookie token, ContainerRequestContext context) {
        try {
            String tokenString = token.getValue();

            Jws<Claims> parsedClaims = Jwts.parser()
                    .setSigningKey("supersecret1111111111111111111111111111".getBytes())
                    .parseClaimsJws(tokenString);

            Claims claims = parsedClaims.getBody();
            String[] roles = claims.get("rol").toString().split(" ");
            String id = claims.getSubject();

            if (roles == null || id == null || roles.length < 1 || id.length() < 1) {
                throw new JwtException("Access token roles or id not found");
            }

            return Collections.singletonMap(id, roles);
        } catch (JwtException e) {
            sendUnauthorized(context);
        }
        return null;
    }

    /**
     * Send unauthorized in the response if the request was invalid.
     *
     * @param context the request context
     */
    private void sendUnauthorized(ContainerRequestContext context) {
        context.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE
                        , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                .build()
        );
    }

    private void addSecurityContext(ContainerRequestContext context, Map<String, String[]> userIdAndRoles) {
        final SecurityContext currentSecurityContext = context.getSecurityContext();

        context.setSecurityContext(new SecurityContext() {

            private final String id = userIdAndRoles.keySet().iterator().next();
            private final String[] roles = userIdAndRoles.get(id);

            @Override
            public Principal getUserPrincipal() { return () -> id; }

            @Override
            public boolean isUserInRole(String s) {
                return Arrays.stream(roles).anyMatch(s::equals);
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }

        });
    }
}
