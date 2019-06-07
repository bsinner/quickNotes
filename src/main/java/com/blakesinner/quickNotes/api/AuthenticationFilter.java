package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.util.KeyLoader;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.util.*;

/**
 * Filter to determine if the user trying to access a given api endpoint has an access
 * token that is signed by this webapp. Error codes include one of the following codes
 * to describe the error
 *
 * 401001: Access token expired
 * 401002: Token not found
 * 401003: Invalid token
 *
 * @author bsinner
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "Quick Notes";
    private static final String CHARSET = "UTF-8";
    private static final String SECRET = "/accessTokenPw.txt";
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

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
            Claims claims = validateAccessToken(accessTokenCookie, context);

            if (claims != null) {
                addSecurityContext(context, claims);
            }

            return;
        }

        sendUnauthorized(context, "401002", "Access token not found, please login to continue");
    }

    /**
     * Try to parse the access token, if there is an error parsing the token an exception
     * is thrown and the token is considered invalid.
     *
     * @param token   the access token
     * @param context the request context
     * @return        the token claims, or null if the token is invalid
     */
    private Claims validateAccessToken(Cookie token, ContainerRequestContext context) {
        try {
            String tokenString = token.getValue();

            Jws<Claims> parsedClaims = Jwts.parser()
                    .setSigningKey(new KeyLoader().getKeyBytes(SECRET))
                    .parseClaimsJws(tokenString);

            Claims claims = parsedClaims.getBody();

            if (claims == null || claims.get("rol") == null || claims.getSubject() == null) {
                JwtException e = new JwtException("");
                LOGGER.warn("Error: Access token with valid signature and malformed data detected", e);
                throw e;
            }

            return claims;
        } catch (ExpiredJwtException eje){
            sendUnauthorized(context, "401001", "Access token expired");
        } catch (JwtException je) {
            sendUnauthorized(context, "401003", "Invalid access token");
        }
        return null;
    }

    /**
     * Send unauthorized in the response if the request was invalid.
     *
     * @param context the request context
     * @param code    the error code
     * @param desc     the error description
     */
    private void sendUnauthorized(ContainerRequestContext context, String code, String desc) {
        context.abortWith(
            Response.status(401)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{"
                                + "\"authFilterError\" : {"
                                    + "\"code\" : \"" + code + "\""
                                    + ", \"desc\" : \"" + desc + "\""
                                + "}"
                            + "}")
                    .header(HttpHeaders.WWW_AUTHENTICATE
                            , " realm=\"" + REALM + "\", charset=\"" + CHARSET + "\"")
                    .build()
        );
    }

    /**
     * Set the security context to contain the current users's id and roles.
     *
     * @param context     context of the current request
     * @param claims      the data stored in the access token
     */
    private void addSecurityContext(ContainerRequestContext context, Claims claims) {
        final SecurityContext currentSecurityContext = context.getSecurityContext();

        context.setSecurityContext(new SecurityContext() {

            private final List<String> roles = new ArrayList<>(
                    Arrays.asList(claims.get("rol").toString().split(" "))
            );

            @Override
            public Principal getUserPrincipal() { return claims::getSubject; }

            @Override
            public boolean isUserInRole(String r) { return roles.contains(r); }

            @Override
            public boolean isSecure() { return currentSecurityContext.isSecure(); }

            @Override
            public String getAuthenticationScheme() { return "Bearer"; }

        });
    }

}
