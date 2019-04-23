package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.persistence.GenericDAO;

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

//    private static final String REGULAR_ROLES = "roles";
//    private static final String OWNER_ROLES = "ownerOnlyRoles";
//    public static final String ACCESS_TOKEN_COOKIE = "access_token";

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext context) {
        Class<?> resourceClass = resourceInfo.getResourceClass();
//        Map<String, List<String>> roles = getRequestContextRoles(resourceClass);
        List<String> annotationRoles = getRequestContextRoles(resourceClass);
        if (annotationRoles.size() == 0 || checkPermissions(annotationRoles)) {
            return;
        }
//        if (checkPermissions(roles.get(REGULAR_ROLES))) {
//            return;
//        } else if (checkPermissions(roles.get(OWNER_ROLES))) {
//            if (isOwner(resourceClass, context)) return;
//        } else if (roles.size() == 2 && roles.get(OWNER_ROLES).size() == 0 && roles.get(REGULAR_ROLES).size() == 0) {
//            return;
//        }

        sendForbidden(context);
    }

//    private boolean isOwner(AnnotatedElement annotatedElement, ContainerRequestContext context) {
//        OwnerOnly ownerOnly = annotatedElement.getAnnotation(OwnerOnly.class);
//
//        GenericDAO<?> dao = new GenericDAO(ownerOnly.resourceType());
//        Map<String, String> props = new HashMap<>();
//        props.put(ownerOnly.pkParam(), context.getHeaderString(ownerOnly.fkParam()));
//        props.put(ownerOnly.fkParam(), securityContext.getUserPrincipal().getName());
//
//        List<?> results = dao.getByPropertiesEqual(props);
//
//        return false;
//    }

    private boolean checkPermissions(List<String> roles) {
        for (String role : roles) {
            if (securityContext.isUserInRole(role)) {
                return true;
            }
        }

        return false;
    }

//    private Map<String, List<String>>
    private List<String> getRequestContextRoles(AnnotatedElement annotatedElement) {
//        Map<String, List<String>> roles = new HashMap<>();
        Secured secured = annotatedElement.getAnnotation(Secured.class);
//        OwnerOnly ownerOnly = annotatedElement.getAnnotation(OwnerOnly.class);
        return new ArrayList<>(Arrays.asList(secured.roles()));
//        roles.put(REGULAR_ROLES, secured == null
//                ? Collections.emptyList()
//                : new ArrayList<>(Arrays.asList(secured.roles()))
//        );
//
//        roles.put(OWNER_ROLES, ownerOnly == null
//                ? Collections.emptyList()
//                : new ArrayList<>(Arrays.asList(ownerOnly.roles()))
//        );
//
//        if (roles.size() > 1) {
//            roles.get(REGULAR_ROLES).removeAll(roles.get(OWNER_ROLES));
//        }
//
//        return roles;
    }

    private void sendForbidden(ContainerRequestContext context) {
        context.abortWith(
            Response.status(Response.Status.FORBIDDEN)
            .build()
        );
    }
}
