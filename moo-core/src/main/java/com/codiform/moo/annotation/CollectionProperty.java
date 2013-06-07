package com.codiform.moo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.CollectionMatcher;
import com.codiform.moo.translator.IndexMatcher;

/**
 * Configuration annotation to tell Moo that the values within the collection
 * should be translated to a the class specified in {@link #itemClass()}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface CollectionProperty {
	
	/**
	 * The type to which items should be translated.
	 * 
	 * @return A class object representing the type to which they should be translated, or Object.class if the items don't need to be translated and can simply be copied.
	 */
	Class<?> itemClass() default Object.class;
	
	/**
	 * The class that should be used to match source and destination items if they can't be matched by collection order.
	 * 
	 * @return IndexMatcher.class if they should be matched in order
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends CollectionMatcher> matcher() default IndexMatcher.class;

	/**
	 * Defines the expression used to locate the source property; if no expression specified, the source property name
	 * will simply be the destination property name.
	 */
	String source() default "";

	/**
	 * Indicates whether the items in the collection should be subject to updates, or if they should simply be replaced.
	 * 
	 * @return true if the items should be updated; false for replacement
	 */
	boolean update() default false;

	/**
	 * Indicates if a source value is required, optional or 'default' for this property, where default uses the global configuration
	 * for all properties established in {@link Configuration#isSourcePropertyRequired()}. 
	 * 
	 * @return the optionality of this property
	 */
	Optionality optionality() default Optionality.DEFAULT;

	/**
	 * Whether or not Orphans (items in the destination that have no match in the source) should be removed during a collection update.
	 * 
	 * @return true if they should be removed, false otherwise
	 */
	boolean removeOrphans() default true;
}
