package com.codiform.moo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property as being ignored by translation and update.
 * 
 * This supercedes the source property required setting in
 * {@link com.codiform.moo.configuration.Configuration} and the optionality
 * setting in {@link com.codiform.moo.annotation.Property}
 * 
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ignore {
	String value() default "";
}
