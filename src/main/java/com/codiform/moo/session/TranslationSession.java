package com.codiform.moo.session;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.codiform.moo.cache.DefaultTranslationCache;
import com.codiform.moo.cache.TranslationCache;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.Translator;

/**
 * A translation session contains information relevant to a particular
 * invocation of the translator. It stores translations to avoid cycles, it may
 * store outside objects needed by translations.
 */
public class TranslationSession {

	private TranslationCache translationCache;
	private Configuration configuration;

	public TranslationSession(Configuration configuration) {
		translationCache = new DefaultTranslationCache();
		this.configuration = configuration;
	}

	public <T> T translate(Class<T> destination, Object source) {
		return getTranslator(destination).getTranslation(source,
				translationCache);
	}

	private <T> Translator<T> getTranslator(Class<T> destination) {
		return configuration.getTranslator(destination);
	}

	public <T> List<T> translateEach(Class<T> destination, List<?> sources) {
		return configuration.getTranslator(destination).getEachTranslation(
				sources, translationCache);
	}

	public <T> Collection<T> translateEach(Class<T> destination,
			Collection<?> sources) {
		return configuration.getTranslator(destination).getEachTranslation(
				sources, translationCache);
	}

	public <T> Set<T> translateEach(Class<T> destination, Set<?> sources) {
		return configuration.getTranslator(destination).getEachTranslation(
				sources, translationCache);
	}

}
