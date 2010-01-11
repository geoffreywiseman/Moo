package com.codiform.moo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A configuration annotation to tell Moo where to find the source for this
 * field. The translation expression ({@link #value()}) is run through MVEL.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
public @interface Translation {

	String value();

}
