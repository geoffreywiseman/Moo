package com.codiform.moo.cache;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A translation session contains information relevant to a particular
 * invocation of the translator. It stores translations to avoid cycles, it may
 * store outside objects needed by translations.
 */
public class DefaultTranslationCache implements TranslationCache {

	private IdentityHashMap<Object, Map<Class<?>, Object>> cache;

	public DefaultTranslationCache() {
		cache = new IdentityHashMap<Object, Map<Class<?>, Object>>();
	}

	@Override
	public <T> T getTranslation(Class<T> destination, Object source) {
		Map<Class<?>, Object> translations = cache.get(source);
		if (translations != null && translations.containsKey(destination)) {
			return destination.cast(translations.get(destination));
		} else {
			return null;
		}
	}

	@Override
	public void putTranslation(Object source, Object destination) {
		Map<Class<?>, Object> translations = cache.get(source);
		if (translations == null) {
			translations = new HashMap<Class<?>, Object>();
			cache.put(source, translations);
		}
		translations.put(destination.getClass(), destination);
	}
}
