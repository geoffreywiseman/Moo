package com.codiform.moo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.session.TranslationSession;

/**
 * Central facade class for interacting with Moo to do mapping from objects to objects.  
 * Much of the capability of the framework can be exercised at this
 * level without ever delving into many of the classes within.
 * 
 * <p>Alternately, you might find working directly with {@link Translate} and {@link Update} more to your taste.</p>
 * 
 * <p>Some simple examples of Moo in action:<p>
 * <ul>
 * 	<li>Moo.translate( UserDto.class ).from( currentUser );</li>
 * 	<li>Moo.translate( UserDto.class ).fromEach( activeUsers );</li>
 * 	<li>Moo.translate( currentUser, UserDto.class );</li>
 * 	<li>Moo.translate( activeUsers, UserDto.class );</li>
 * </ul>
 * 
 * <p>
 * Moo supports a limited amount of configuration through annotations, in 
 * particular {@link Translation}, {@link Translate} and {@link TranslateCollection}.
 * </p>
 * 
 * <p>
 * Instances of Moo are intended to be typesafe, they can be used by more than
 * one thread at once without an issue, and can be cached and re-used.
 * </p>
 */
public class Moo {

	private Configuration configuration;

	/**
	 * Creates a new instance of Moo with a default {@link Configuration}.
	 */
	public Moo( ) {
		this( new Configuration() );
	}
	
	/**
	 * Creates a new instance of Moo with a known (and specified) {@link Configuration}.
	 * 
	 * @param configuration the configuration for this instance of Moo
	 */
	public Moo( Configuration configuration ) {
		this.configuration = configuration;
	}

	/**
	 * Prepare for translation to a destination class by creating a 'curried' operation
	 * which can be invoked upon to do translations.
	 * 
	 * @param <T> the type to which translations should be performed
	 * @param destinationClass the class for the type to which translations should be performed
	 * @return a class representing the 'curried' {@link Translate}
	 */
	public <T> Translate<T> translate( Class<T> destinationClass ) {
		return new Translate<T>( configuration, destinationClass );
	}

	/**
	 * Perform a translation of a source object into an instance of the destination class.
	 * 
	 * @param <T> the type to which translation should take place
	 * @param source the object to be used as the source of the translation
	 * @param destinationClass the class of the type to which translation should take place
	 * @return the instance of the destination class with values translated from the source instance
	 */
	public <T> T translate( Object source, Class<T> destinationClass ) {
		return newSession().getTranslation( source, destinationClass );
	}

	/**
	 * Perform a translation of a list of source objects into an list of instances of the destination class.
	 * 
	 * @param <T> the type to which translation should take place
	 * @param sources the objects to be used as the sources of the translation
	 * @param destinationClass the class of the type to which translation should take place
	 * @return a list of instances of the destination class with values translated from the source instances
	 */
	public <T> List<T> translateEach( List<?> sources, Class<T> destinationClass ) {
		return newSession().getEachTranslation( sources, destinationClass );
	}

	/**
	 * Perform a translation of a collection of source objects into an collection of instances of the destination class.
	 * 
	 * @param <T> the type to which translation should take place
	 * @param sources the objects to be used as the sources of the translation
	 * @param destinationClass the class of the type to which translation should take place
	 * @return a collection of instances of the destination class with values translated from the source instances
	 */
	public <T> Collection<T> translateEach( Collection<?> sources, Class<T> destinationClass ) {
		return newSession().getEachTranslation( sources, destinationClass );
	}

	/**
	 * Perform a translation of a set of source objects into an set of instances of the destination class.
	 * 
	 * @param <T> the type to which translation should take place
	 * @param sources the objects to be used as the sources of the translation
	 * @param destinationClass the class of the type to which translation should take place
	 * @return a set of instances of the destination class with values translated from the source instances
	 */
	public <T> Set<T> translateEach( Set<?> sources, Class<T> destinationClass ) {
		return newSession().getEachTranslation( sources, destinationClass );
	}

	protected TranslationSession newSession() {
		return new TranslationSession(configuration);
	}
	
}
