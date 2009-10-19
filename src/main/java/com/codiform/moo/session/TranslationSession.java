package com.codiform.moo.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.source.TranslationSource;
import com.codiform.moo.translator.Translator;

/**
 * A translation session contains information relevant to a particular
 * invocation of the translator. It stores translations to avoid cycles, it may
 * store outside objects needed by translations.
 */
public class TranslationSession implements TranslationSource {

	private TranslationCache translationCache;
	private Configuration configuration;

	public TranslationSession(Configuration configuration) {
		translationCache = new TranslationCache();
		this.configuration = configuration;
	}

	public <T> T getTranslation(Object source, Class<T> destinationClass) {
		T translated = translationCache
				.getTranslation(source, destinationClass);
		if (translated == null)
			translated = translate(source, destinationClass);
		return translated;
	}

	public <T> List<T> getEachTranslation(List<?> sources,
			Class<T> destinationClass) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	public <T> Collection<T> getEachTranslation(Collection<?> sources,
			Class<T> destinationClass) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	public <T> Set<T> getEachTranslation(Set<?> sources,
			Class<T> destinationClass) {
		Set<T> results = new HashSet<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	private <T> T translate(Object source, Class<T> destinationClass) {
		if (source == null) {
			return null;
		} else {
			Translator<T> translator = getTranslator(destinationClass);
			T translated = translator.create();
			translationCache.putTranslation(source, translated);
			translator.update(source, translated, this);
			return translated;
		}
	}

	public <T> void update(Object source, T destination) {
		configuration.getTranslator(destination.getClass()).castAndUpdate(
				source, destination, this);
	}

	private <T> Translator<T> getTranslator(Class<T> destination) {
		return configuration.getTranslator(destination);
	}

}
