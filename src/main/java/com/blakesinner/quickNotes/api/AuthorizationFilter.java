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

    public static final String REGULAR_ROLES = "roles";
    public static final String OWNER_ROLES = "ownerOnlyRoles";
//    public static final String ACCESS_TOKEN_COOKIE = "access_token";
e id =
    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext context) {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        Map<String, List<String>> roles = getRequestContextRoles(resourceClass);

        if (roles == null || roles.size() == 0 || checkPermissions(roles.get(REGULAR_ROLES))) {
            return;
        } else if (checkPermissions(roles.get(OWNER_ROLES))) {
            // search the db to see if the resource is owned
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

    private Map<String, List<String>> getRequestContextRoles(AnnotatedElement annotatedElement) {
        Map<String, List<String>> roles = new HashMap<>();
        Secured secured = annotatedElement.getAnnotation(Secured.class);
        OwnerOnly ownerOnly = annotatedElement.getAnnotation(OwnerOnly.class);

        if (annotatedElement != null && secured != null) {
            roles.put(REGULAR_ROLES, new ArrayList<>(Arrays.asList(secured.roles())));
            roles.put(OWNER_ROLES, new ArrayList<>(Arrays.asList(ownerOnly.roles())));
            roles.get(REGULAR_ROLES).removeAll(roles.get(OWNER_ROLES));

            return roles;
        }

        return null;
    }

    public void sendForbidden(ContainerRequestContext context) {
        context.abortWith(
            Response.status(Response.Status.FORBIDDEN)
            .build()
        );
    }
}
