package com.codiform.moo.session;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * The translation cache simply stores translations from a source object to a destination class
 * throughout the duration of a translation session.  This may improve performance, but is
 * primarily intended to ensure that the translator does not enter endless loops due to cycles
 * in the object graph.
 */
public class TranslationCache {

	/**
	 * The data structhre of the cache.
	 */
	private IdentityHashMap<Object, Map<Class<?>, Object>> cache;

	/**
	 * Creates a new translation cache empty of translations.
	 */
	public TranslationCache() {
		cache = new IdentityHashMap<>();
	}

	/**
	 * Retrieves any translation stored in the cache which matches the specified source
	 * and destination class.
	 * 
	 * @param <T> the type that binds the destination class to the return value
	 * @param source the source object that would have been translated
	 * @param destination the destination class to which the source object would have been translated
	 * @return the translation, if there is one, or null otherwise
	 */
	public <T> T getTranslation(Object source, Class<T> destination) {
		Map<Class<?>, Object> translations = cache.get(source);
		if (translations != null && translations.containsKey(destination)) {
			return destination.cast(translations.get(destination));
		} else {
			return null;
		}
	}

	/**
	 * Store a translation in the cache where it may be retrieved.
	 * 
	 * @param source the source object that was translated
	 * @param translation the resulting translation
	 */
	public void putTranslation(Object source, Object translation) {
		Map<Class<?>, Object> translations = cache.computeIfAbsent( source, k -> new HashMap<>() );
		translations.put(translation.getClass(), translation);
	}
}
