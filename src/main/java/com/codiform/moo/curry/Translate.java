package com.codiform.moo.curry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSession;
import com.codiform.moo.translator.Translator;

/**
 * This class represents a curried translation, where the destination class has been
 * selected but no source classes have yet been supplied.
 * 
 * <p>Practically speaking, this could also be implemented through the {@link Translator} 
 * class, but this is a higher-level facade class.  The translator class is internal
 * implementation and much more subject to change.</p>
 *
 * @param <T> the destination type for the curried translation
 */
public class Translate<T> {
	
	/**
	 * The moo configuration backing the translation.
	 */
	private Configuration configuration;
	
	/**
	 * The destination class to which the translation will be performed.
	 */
	private Class<T> destinationClass;
	
	/**
	 * Create a curried translation using a known configuration and a backing class;
	 */
	public Translate(Configuration configuration, Class<T> destinationClass) {
		this.configuration = configuration;
		this.destinationClass = destinationClass;
	}
	
	public T from(Object source) {
		return new TranslationSession(configuration).getTranslation( source, destinationClass);
	}
	
	public Collection<T> fromEach(Collection<?> source) {
		return new TranslationSession(configuration).getEachTranslation( source, destinationClass);
	}
	
	public Set<T> fromEach(Set<?> source) {
		return new TranslationSession(configuration).getEachTranslation( source, destinationClass);
	}
	
	public List<T> fromEach(List<?> source) {
		return new TranslationSession(configuration).getEachTranslation( source, destinationClass);
	}
	
	public static <T> Translate<T> to( Class<T> destinationClass ) {
		return new Translate<T>( new Configuration(), destinationClass );
	}
}
