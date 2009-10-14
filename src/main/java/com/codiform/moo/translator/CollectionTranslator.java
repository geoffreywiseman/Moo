package com.codiform.moo.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.cache.TranslationCache;
import com.codiform.moo.cache.TranslatorCache;

public class CollectionTranslator {

	private TranslatorCache translatorCache;

	public CollectionTranslator(TranslatorCache translatorSource) {
		this.translatorCache = translatorSource;
	}

	@SuppressWarnings("unchecked")
	public Object translate(Object value, TranslateCollection annotation,
			TranslationCache cache) {
		if (value instanceof SortedSet<?>) {
			return copySortedSet((SortedSet<?>) value, annotation);
		} else if (value instanceof Set<?>) {
			return copySet((Set<?>) value, annotation, cache);
		} else if (value instanceof SortedMap) {
			return copySortedMap((SortedMap) value, annotation);
		} else if (value instanceof Map) {
			return copyMap((Map) value, annotation);
		} else if (value instanceof List) {
			return (List) copyList((List) value, annotation, cache);
		} else {
			return copyCollection((Collection) value, annotation, cache);
		}
	}

	@SuppressWarnings("unchecked")
	private Collection copyCollection(Collection value,
			TranslateCollection annotation, TranslationCache cache) {
		if (annotation == null) {
			return new ArrayList(value);
		} else {
			return translatorCache.getTranslator(annotation.value())
					.getEachTranslation(value, cache);
		}
	}

	@SuppressWarnings("unchecked")
	private List copyList(List value, TranslateCollection annotation,
			TranslationCache cache) {
		if (annotation == null) {
			return new ArrayList(value);
		} else {
			return translatorCache.getTranslator(annotation.value())
					.getEachTranslation(value, cache);
		}
	}

	@SuppressWarnings("unchecked")
	private Map copyMap(Map value, TranslateCollection annotation) {
		if (annotation == null) {
			return new HashMap(value);
		} else {
			throw new TranslationException(
					"Support for translated maps not yet built.");
		}
	}

	@SuppressWarnings("unchecked")
	private SortedMap copySortedMap(SortedMap value,
			TranslateCollection annotation) {
		if (annotation == null) {
			SortedMap map = new TreeMap<Object, Object>(value.comparator());
			map.putAll(value);
			return map;
		} else {
			throw new TranslationException(
					"Support for translated sorted maps not yet built");
		}
	}

	private Set<?> copySet(Set<?> value, TranslateCollection annotation,
			TranslationCache cache) {
		if (annotation == null) {
			return new HashSet<Object>(value);
		} else {
			return translatorCache.getTranslator(annotation.value())
					.getEachTranslation(value, cache);
		}
	}

	@SuppressWarnings("unchecked")
	private SortedSet<Object> copySortedSet(SortedSet<?> value,
			TranslateCollection annotation) {
		if (annotation == null) {
			SortedSet result = new TreeSet(value.comparator());
			result.addAll(value);
			return result;
		} else {
			throw new TranslationException(
					"Support for translated sorted sets not yet built");
		}
	}

}
