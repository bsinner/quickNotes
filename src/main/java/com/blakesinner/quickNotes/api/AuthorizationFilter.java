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

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext context) {
        Class<?> resourceClass = resourceInfo.getResourceClass();

        List<String> annotationRoles = getRequestContextRoles(resourceClass);
        if (annotationRoles.size() == 0 || checkPermissions(annotationRoles)) {
            return;
        }

        sendForbidden(context);
    }

    private boolean checkPermissions(List<String> roles) {
        for (String role : roles) {
            if (securityContext.isUserInRole(role)) {
                return true;
            }
        }

        return false;
    }

    private List<String> getRequestContextRoles(AnnotatedElement annotatedElement) {
        Secured secured = annotatedElement.getAnnotation(Secured.class);
        return new ArrayList<>(Arrays.asList(secured.roles()));
    }

    private void sendForbidden(ContainerRequestContext context) {
        context.abortWith(
            Response.status(Response.Status.FORBIDDEN)
            .build()
        );
    }
}
