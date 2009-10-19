package com.codiform.moo.session;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.codiform.moo.source.TranslationSource;

/**
 * A translation session contains information relevant to a particular
 * invocation of the translator. It stores translations to avoid cycles, it may
 * store outside objects needed by translations.
 */
public class TranslationCache {

	private IdentityHashMap<Object, Map<Class<?>, Object>> cache;

	public TranslationCache() {
		cache = new IdentityHashMap<Object, Map<Class<?>, Object>>();
	}

	public <T> T getTranslation(Object source, Class<T> destination) {
		Map<Class<?>, Object> translations = cache.get(source);
		if (translations != null && translations.containsKey(destination)) {
			return destination.cast(translations.get(destination));
		} else {
			return null;
		}
	}

	public void putTranslation(Object source, Object destination) {
		Map<Class<?>, Object> translations = cache.get(source);
		if (translations == null) {
			translations = new HashMap<Class<?>, Object>();
			cache.put(source, translations);
		}
		translations.put(destination.getClass(), destination);
	}
}
