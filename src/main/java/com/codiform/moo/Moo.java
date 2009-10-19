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
 */
public class Moo {

	private Configuration configuration;

	public Moo( ) {
		this( new Configuration() );
	}
	
	public Moo( Configuration configuration ) {
		this.configuration = configuration;
	}

	private TranslationSession newSession() {
		return new TranslationSession(configuration);
	}
	
	public <T> Translate<T> translate( Class<T> destinationClass ) {
		return new Translate<T>( configuration, destinationClass );
	}

	public <T> T translate( Object source, Class<T> destinationClass ) {
		return newSession().translate( source, destinationClass );
	}

	public <T> List<T> translateEach( List<?> sources, Class<T> destinationClass ) {
		return newSession().translateEach( sources, destinationClass );
	}

	public <T> Collection<T> translateEach( Collection<?> sources, Class<T> destinationClass ) {
		return newSession().translateEach( sources, destinationClass );
	}

	public <T> Set<T> translateEach( Set<?> sources, Class<T> destinationClass ) {
		return newSession().translateEach( sources, destinationClass );
	}

}
