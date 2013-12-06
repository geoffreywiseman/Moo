package com.codiform.moo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.DefaultMapTargetFactory;
import com.codiform.moo.translator.TranslationTargetFactory;

/**
 * Configuration annotation to tell Moo that the values within the collection should be translated
 * to a the class specified in {@link #itemClass()}.
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface MapProperty {

	/**
	 * The type to which keys should be translated.
	 * 
	 * @return A class object representing the type to which they should be translated, or
	 *         Object.class if the items don't need to be translated and can simply be copied.
	 */
	Class<?> keyClass() default Object.class;

	/**
	 * The type to which values should be translated.
	 * 
	 * @return A class object representing the type to which they should be translated, or
	 *         Object.class if the items don't need to be translated and can simply be copied.
	 */
	Class<?> valueClass() default Object.class;

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
	 * Whether or not Orphans (keys in the destination that have no match in the source) should be
	 * removed during a map update.
	 * 
	 * @return true if they should be removed, false otherwise
	 */
	boolean removeOrphans() default true;

	/**
	 * Specifies the factory class that should be used to create the target Map instance for this
	 * property. By default, this is the DefaultCollectionTargetFactory.
	 */
	Class<? extends TranslationTargetFactory> factory() default DefaultMapTargetFactory.class;

	/**
	 * Source expression for the key, if the key's source is anything more complicated than the 
	 * key itself. This would usually be used to extract a sub-component of the key. 
	 * 
	 * @return the source expression
	 */
	String keySource() default "";

	/**
	 * Indicates if null keys should be allowed when copying or translating the map. Defaults to
	 * true.
	 * 
	 * @return true if null keys are allowed, false if they are not
	 */
	boolean nullKeys() default true;
}
