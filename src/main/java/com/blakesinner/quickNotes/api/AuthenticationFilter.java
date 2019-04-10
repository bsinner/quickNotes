package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

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
            int currentUserId = validateAccessToken(accessTokenCookie, context);

            if(currentUserId > 0) {
                addSecurityContext(context, currentUserId);
                return;
            }

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
    private int validateAccessToken(Cookie token, ContainerRequestContext context) {
        try {
            String tokenString = token.getValue();

            Jws<Claims> parsedClaims = Jwts.parser()
                    .setSigningKey("supersecret1111111111111111111111111111".getBytes())
                    .parseClaimsJws(tokenString);
            return Integer.parseInt(parsedClaims.getBody().getSubject());
        } catch (JwtException e) {
            sendUnauthorized(context);
        }
        return 0;
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

    private void addSecurityContext(ContainerRequestContext context, int userId) {
        final SecurityContext currentSecurityContext = context.getSecurityContext();
        context.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
              return new Principal() {
                  @Override
                  public String getName() {
                      return null;
                  }
              };
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
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
