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
 * translation pass.  This typically corresponds to a single invocation
 * of the translator, although you could retain a translation session
 * if you wished to re-use it.
 * 
 * <p>The TranslationSession is not intended to be thread-safe; it is
 * a stateful construct, and several threads running on it at once would
 * have an undefined effect.</p>
 */
public class TranslationSession implements TranslationSource {

	private TranslationCache translationCache;
	private Configuration configuration;

	/**
	 * Creates a translation session with a known configuration.
	 * 
	 * @param configuration the {@link Configuration} of the translation session
	 */
	public TranslationSession(Configuration configuration) {
		translationCache = new TranslationCache();
		this.configuration = configuration;
	}

	/**
	 * Gets a translation to a specified class from the translation session.  Attempts to load from cache,
	 * but if not present, performs a translation using a translator.
	 * 
	 * @param source the source object to be translated
	 * @param destinationClass the class to which the translation should be performed
	 * @param <T> the type that binds the return value to the destination class
	 */
	public <T> T getTranslation(Object source, Class<T> destinationClass) {
		T translated = translationCache
				.getTranslation(source, destinationClass);
		if (translated == null)
			translated = translate(source, destinationClass);
		return translated;
	}

	/**
	 * Creates a list of translations from a list of source objects, first checking the
	 * cache and then, if needed, creating the translation.
	 * 
	 * @param sources the source objects to be translated
	 * @param destinationClass the class to which the translation should be performed
	 * @param <T> the type that binds the return values to the destination class
	 */
	public <T> List<T> getEachTranslation(List<?> sources,
			Class<T> destinationClass) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	/**
	 * Creates a collection of translations from a collection of source objects, first checking the
	 * cache and then, if needed, creating the translation.
	 * 
	 * @param sources the source objects to be translated
	 * @param destinationClass the class to which the translation should be performed
	 * @param <T> the type that binds the return values to the destination class
	 */
	public <T> Collection<T> getEachTranslation(Collection<?> sources,
			Class<T> destinationClass) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	/**
	 * Creates a set of translations from a set of source objects, first checking the
	 * cache and then, if needed, creating the translation.
	 * 
	 * @param sources the source objects to be translated
	 * @param destinationClass the class to which the translation should be performed
	 * @param <T> the type that binds the return values to the destination class
	 */
	public <T> Set<T> getEachTranslation(Set<?> sources,
			Class<T> destinationClass) {
		Set<T> results = new HashSet<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, destinationClass));
		}
		return results;
	}

	/**
	 * Updates an object from a source object, performing any necessary translations
	 * along the way.
	 * 
	 * @param source the object from which the values should be retrieved (and, if necessary, translated)
	 * @param destination the object to which the values should be applied
	 */
	public void update(Object source, Object destination) {
		configuration.getTranslator(destination.getClass()).castAndUpdate(
				source, destination, this);
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

	private <T> Translator<T> getTranslator(Class<T> destination) {
		return configuration.getTranslator(destination);
	}

	/*package*/ void setTranslationCache(TranslationCache cache) {
		this.translationCache = cache;
	}

}
