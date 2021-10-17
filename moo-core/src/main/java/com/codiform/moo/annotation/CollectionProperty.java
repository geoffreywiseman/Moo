package com.codiform.moo.annotation;

import java.lang.annotation.*;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.*;

/**
 * Configuration annotation to tell Moo that the values within the collection should be translated
 * to a the class specified in {@link #itemClass()}.
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface CollectionProperty {

	/**
	 * The type to which items should be translated.
	 *
	 * @return A class object representing the type to which they should be translated, or
	 * Object.class if the items don't need to be translated and can simply be copied.
	 */
	Class<?> itemClass() default Object.class;

	/**
	 * The type that will be used to instantiate item classes, or {@link DefaultObjectTargetFactory} if the
	 * default item factory will be used.
	 *
	 * @return a class implementing {@link TranslationTargetFactory}
	 */
	Class<? extends TranslationTargetFactory> itemFactory() default DefaultObjectTargetFactory.class;

	/**
	 * The class that should be used to match source and destination items if they can't be matched
	 * by collection order.
	 *
	 * @return IndexMatcher.class if they should be matched in order
	 */
	Class<? extends CollectionMatcher> matcher() default IndexMatcher.class;

	/**
	 * Defines the expression used to locate the source property; if no expression specified, the
	 * source property name will simply be the destination property name.
	 */
	String source() default "";

	/**
	 * Indicates whether the items in the collection should be subject to updates, or if they should
	 * simply be replaced.
	 *
	 * @return true if the items should be updated; false for replacement
	 */
	boolean update() default false;

	/**
	 * Indicates if a source value is required, optional or 'default' for this property, where
	 * default uses the global configuration for all properties established in
	 * {@link Configuration#isSourcePropertyRequired()}.
	 *
	 * @return the optionality of this property
	 */
	Optionality optionality() default Optionality.DEFAULT;

	/**
	 * Whether or not Orphans (items in the destination that have no match in the source) should be
	 * removed during a collection update.
	 *
	 * @return true if they should be removed, false otherwise
	 */
	boolean removeOrphans() default true;

	/**
	 * The source property expression to be used to get the real source item from each element
	 * within the source collection. If no expression is specified here, the default would be to use
	 * the collection element as the source item. This is useful if you wish to convert the type
	 * contained within the collection or extract some of its total data without taking all
	 * elements.
	 *
	 * @return the expression to be used to retrieve the source item; empty string if default to be
	 * used.
	 */
	String itemSource() default "";

	Class<? extends TranslationTargetFactory> factory() default DefaultCollectionTargetFactory.class;
}
