package com.codiform.moo.curry;

import java.util.*;

import com.codiform.moo.session.TranslationSession;
import com.codiform.moo.translator.CachingTranslatorFactory;
import com.codiform.moo.translator.TranslatorFactory;

/**
 * This class represents a curried translation, where the destination class has been selected but no
 * source classes have yet been supplied.
 * 
 * <p>
 * Practically speaking, this could also be implemented through the
 * {@link com.codiform.moo.translator.ObjectTranslator} class, but this is a higher-level facade
 * class. The translator class is internal implementation and much more subject to change.
 * </p>
 * 
 * @param <T>
 *            the destination type for the curried translation
 */
public class Translate<T> {

	/**
	 * Externally mapped variables which can be referred to within translation expressions.
	 */
	Map<String, Object> variables;

	/**
	 * The translator factory used to retrieve translators.
	 */
	private TranslatorFactory translatorFactory;

	/**
	 * The destination class to which the translation will be performed.
	 */
	private Class<T> destinationClass;

	/**
	 * Create a curried translation using a known configuration and a backing class;
	 */
	public Translate( TranslatorFactory translatorFactory, Class<T> destinationClass ) {
		this.translatorFactory = translatorFactory;
		this.destinationClass = destinationClass;
		this.variables = new HashMap<>();
	}

	/**
	 * Translates from the given source object to an instance of the destination class.
	 * 
	 * @param source
	 *            the object from which values should be retried
	 * @return the translated instance of the destination class
	 */
	public T from( Object source ) {
		return new TranslationSession( translatorFactory, variables ).getTranslation( source, destinationClass );
	}

	/**
	 * Translates from the given source objects to instances of the destination class.
	 * 
	 * @param source
	 *            the collection of objects from which values should be retried
	 * @return the translated instances of the destination class
	 */
	public Collection<T> fromEach( Collection<?> source ) {
		return new TranslationSession( translatorFactory, variables ).getEachTranslation( source, destinationClass );
	}

	/**
	 * Translates from the given source objects to instances of the destination class.
	 * 
	 * @param source
	 *            the set of objects from which values should be retried
	 * @return the translated instances of the destination class
	 */
	public Set<T> fromEach( Set<?> source ) {
		return new TranslationSession( translatorFactory, variables ).getEachTranslation( source, destinationClass );
	}

	/**
	 * Translates from the given source objects to instances of the destination class.
	 * 
	 * @param source
	 *            the list of objects from which values should be retried
	 * @return the translated instances of the destination class
	 */
	public List<T> fromEach( List<?> source ) {
		return new TranslationSession( translatorFactory, variables ).getEachTranslation( source, destinationClass );
	}

	/**
	 * Stores a variable for use in translation.
	 * 
	 * @param variableName
	 *            the name of the variable to be stored
	 * @param variableValue
	 *            the value of the variable to be stored
	 * @return a curried translation for further action
	 */
	public Translate<T> withVariable( String variableName, Object variableValue ) {
		variables.put( variableName, variableValue );
		return this;
	}
	
	/**
	 * A common entry point for translate, if you're not invoking it from within
	 * {@link com.codiform.moo.Moo}. This allows you to start up a translate with a default moo
	 * configuration in a very DSL-like way.
	 * 
	 * @param <T>
	 *            links the supplied class to the curried translate to ensure that the latter
	 *            returns the former
	 * @param destinationClass
	 *            a class representing the type to which translation should be performed
	 * @return a 'curried' translate instance
	 */
	public static <T> Translate<T> to( Class<T> destinationClass ) {
		return new Translate<>( new CachingTranslatorFactory(), destinationClass );
	}

}
