package com.blakesinner.quickNotes.api;

import com.blakesinner.quickNotes.entity.User;
import com.blakesinner.quickNotes.persistence.GenericDAO;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    public static final String REGULAR_ROLES = "roles";
    public static final String OWNER_ROLES = "ownerOnlyRoles";
    public static final String ACCESS_COOKIE = "access_token";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext context) {
        Class<?> resourceClass = resourceInfo.getResourceClass();
        Map<String, List<Role>> roles = getRoles(resourceClass);

        Cookie cookie = context.getCookies().get(ACCESS_COOKIE);

        checkPermissions(roles, cookie);
    }

    private void checkPermissions(Map<String, List<Role>> roles, Cookie accessToken) {
        GenericDAO<User> dao = new GenericDAO<>(User.class);
//        User user = dao.get
//
//        findMatchingPermissions();
//        findMatchingPermissions();
        // check if the user matches regular roles first
//        checkRegularPermissions(roles.get(REGULAR_ROLES), user);

        // then check owner-only
//        checkOwnerPermissions(roles.get(OWNER_ROLES), user);

    }

    private boolean findMatchingPermissions(List<Role> roles, User user) {
        boolean access = false;
//        Role user.getRol

        return access;
    }
//    private void comparePermissions(List<Role> roles, User user) {
//        boolean access = false;
//        Role userRole = user.getRole();

//        for (Role role : roles) {
//            if (user.get)
//        }
//    }

    private Map<String, List<Role>> getRoles(AnnotatedElement annotatedElement) {
        Map<String, List<Role>> roles = new HashMap<>();

        if (annotatedElement == null) {
            return roles;

        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);

            if (secured == null) {
                return roles;

            } else {
                roles.put(REGULAR_ROLES, new ArrayList<>(Arrays.asList(secured.roles())));
                roles.put(OWNER_ROLES, new ArrayList<>(Arrays.asList(secured.ownerOnlyRoles())));

                roles.get(REGULAR_ROLES).removeAll(roles.get(OWNER_ROLES));
                return roles;
            }
        }

    }
}
