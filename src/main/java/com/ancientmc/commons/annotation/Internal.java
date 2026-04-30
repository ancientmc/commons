package com.ancientmc.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Marks a public type in an API as intended for internal use.
 *
 * @author moist-mason
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({TYPE, METHOD, FIELD, PARAMETER, LOCAL_VARIABLE, ANNOTATION_TYPE})
public @interface Internal { }
