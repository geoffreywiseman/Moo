package com.codiform.moo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.codiform.moo.translator.CollectionMatcher;

/**
 * Configuration annotation to tell Moo that when updating a collection, a particular class
 * should be used to establish correspondence between the values on one side of the update
 * and the values on the other.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MatchWith {
	Class<? extends CollectionMatcher<?,?>> value();
}
