package com.blakesinner.quickNotes.api;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
/**
 * Used to bind authentication and authorization filters to api
 * endpoints. Note that if roles is left blank, and no OwnerOnly
 * annotation is present all users with valid access tokens will
 * have access to a given endpoint.
 */
public @interface Secured {
    String[] roles() default {};
}