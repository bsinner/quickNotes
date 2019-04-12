package com.blakesinner.quickNotes.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface OwnerOnly {
    Class<?> resourceType();
    String pkParam() default "id";
    String fkParam();
    String[] roles() default {};
}
