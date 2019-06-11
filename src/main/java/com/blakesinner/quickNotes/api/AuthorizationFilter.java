package com.blakesinner.quickNotes.api;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * Filter to determine if a logged in user is authorized to access an endpoint.
 * Error responses contain a JSON body with one of the following error codes to
 * describe the error:
 *
 * 403001: User doesn't have required roles
 * 403002: User doesn't have required roles, and their account isn't activated
 *
 * @author bsinner
 */
@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    /**
     * Filter incoming requests by checking if the current resource is filtered
     * and if it is filtered, checking if the current user is authorized to access it.
     *
     * @param context the request context
     */
    @Override
    public void filter(ContainerRequestContext context) {
        Class<?> resourceClass = resourceInfo.getResourceClass();

        List<String> contextRoles = getRequestContextRoles(resourceClass);

        if (contextRoles.isEmpty() || isAuthorized(contextRoles)) {
            return;
        }

        if(isUnactivated()) {
            sendForbidden(context, "Users account isn't activated", "403002");
            return;
        }

        sendForbidden(context, "User doesn't have the required roles", "403001");
    }

    /**
     * Check if the user has one of the roles listed in the resource's
     * Secured annotation.
     *
     * @param roles the endpoint's allowed roles
     * @return      true if the user is authorized, false otherwise
     */
    private boolean isAuthorized(List<String> roles) {
        for (String role : roles) {
            if (securityContext.isUserInRole(role)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get if user is not yet activated.
     *
     * @return true if unactivated, false if user is activated
     */
    private boolean isUnactivated() { return securityContext.isUserInRole("UNACTIVATED"); }

    /**
     * Get roles of the secured annotation.
     *
     * @param annotatedElement the element with annotations
     * @return                 the roles of the elements secured annotation
     */
    private List<String> getRequestContextRoles(AnnotatedElement annotatedElement) {
        Secured secured = annotatedElement.getAnnotation(Secured.class);
        return new ArrayList<>(Arrays.asList(secured.roles()));
    }

    /**
     * Send 403 forbidden response.
     *
     * @param context the request context
     * @param msg     the error description
     * @param code    the error code
     */
    private void sendForbidden(ContainerRequestContext context, String msg, String code) {
        context.abortWith(
            Response.status(403)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{"
                                + "\"authFilterError\" : {"
                                    + "\"code\" : \"" + code + "\""
                                    + ", \"desc\" : \"" + msg + "\""
                                + "}"
                            + "}")
                    .build()
        );
    }
}
