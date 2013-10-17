package com.codiform.moo.translator;

/**
 * A source from which Moo's translators may be retrieved. This helps to 
 * abstract the source of these, whether or not they are cached,
 * and so forth. That may also be an unnecessary abstraction.
 */
public interface TranslatorFactory {

	/**
	 * Gets the translator responsible for translating to a destination class.
	 * 
	 * @param <T> links the class to the type of the translator
	 * @param destinationClass the type to which we'd like to perform translation
	 * @return a translator capable of translating to the destination class
	 */
	<T> ObjectTranslator<T> getTranslator(Class<T> destinationClass);

	/**
	 * Gets the translator responsible for dealing with collections, typically
	 * either doing translations on them, or doing defensive copies.
	 * 
	 * @return the collection translator
	 */
	CollectionTranslator getCollectionTranslator();

	/**
	 * Performs defensive copies and translations on arrays.
	 * 
	 * @return the array translator
	 */
	ArrayTranslator getArrayTranslator();

	/**
	 * Get the value type translator corresponding to a destination type.
	 * 
	 * @param destinationType the destination type for the value type translator
	 * @return the value type translator corresponding to the destination type
	 */
	<V> ValueTypeTranslator<V> getValueTypeTranslator( Class<V> destinationType );
	
}
