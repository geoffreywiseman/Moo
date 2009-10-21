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
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.source.TranslationSource;

public class CollectionTranslator {

	private Configuration configuration;

	public CollectionTranslator(Configuration configuration) {
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	public Object translate(Object value, TranslateCollection annotation,
			TranslationSource cache) {
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
			TranslateCollection annotation, TranslationSource translationSource) {
		if (annotation == null) {
			return configuration.isPerformingDefensiveCopies() ? new ArrayList(
					value) : value;
		} else {
			return translationSource.getEachTranslation(value, annotation
					.value());
		}
	}

	@SuppressWarnings("unchecked")
	private List copyList(List value, TranslateCollection annotation,
			TranslationSource translationSource) {
		if (annotation == null) {
			return configuration.isPerformingDefensiveCopies() ? new ArrayList(
					value) : value;
		} else {
			return translationSource.getEachTranslation(value, annotation
					.value());
		}
	}

	@SuppressWarnings("unchecked")
	private Map copyMap(Map value, TranslateCollection annotation) {
		if (annotation == null) {
			return configuration.isPerformingDefensiveCopies() ? new HashMap(
					value) : value;
		} else {
			throw new TranslationException(
					"Support for translated maps not yet built.");
		}
	}

	@SuppressWarnings("unchecked")
	private SortedMap copySortedMap(SortedMap value,
			TranslateCollection annotation) {
		if (annotation == null) {
			if (configuration.isPerformingDefensiveCopies()) {
				SortedMap map = new TreeMap<Object, Object>(value.comparator());
				map.putAll(value);
				return map;
			} else {
				return value;
			}
		} else {
			throw new TranslationException(
					"Support for translated sorted maps not yet built");
		}
	}

	private Set<?> copySet(Set<?> value, TranslateCollection annotation,
			TranslationSource translationSource) {
		if (annotation == null) {
			return configuration.isPerformingDefensiveCopies() ? new HashSet<Object>(
					value)
					: value;
		} else {
			return translationSource.getEachTranslation(value, annotation
					.value());
		}
	}

	@SuppressWarnings("unchecked")
	private SortedSet<?> copySortedSet(SortedSet<?> value,
			TranslateCollection annotation) {
		if (annotation == null) {
			if (configuration.isPerformingDefensiveCopies()) {
				SortedSet result = new TreeSet(value.comparator());
				result.addAll(value);
				return result;
			} else {
				return value;
			}
		} else {
			throw new TranslationException(
					"Support for translated sorted sets not yet built");
		}
	}

}
