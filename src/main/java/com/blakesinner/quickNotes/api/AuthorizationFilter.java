package com.blakesinner.quickNotes.api;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * Filter to determine if a logged in user is authorized to access an
 * api endpoint.
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

        sendForbidden(context);
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
     */
    private void sendForbidden(ContainerRequestContext context) {
        context.abortWith(
            Response.status(Response.Status.FORBIDDEN)
            .build()
        );
    }
}
