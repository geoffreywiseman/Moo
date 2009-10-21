package com.codiform.moo.curry;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSession;

/**
 * A curried update operation, where the source of the update has been
 * specified, but the object to which updates should be applied has not.
 * 
 * <p>This can be used to perform updates on several objects, or simply to
 * act as a kind of easy-to-use DSL.</p>
 */
public class Update {

	/**
	 * The source of data to be applied as updates.
	 */
	private Object source;
	
	/**
	 * The configuration backing the updates.
	 */
	private Configuration configuration;
	
	public Update( Configuration configuration, Object source ) {
		this.configuration = configuration;
		this.source = source;
	}
	
	/**
	 * Apply the update from the source to the specified destination.
	 * 
	 * @param destination the object to which updates should be applied.
	 */
	public void to( Object destination ) {
		new TranslationSession( configuration ).update(source, destination);
	}
	
	/**
	 * Create a curried update based on a particular source.
	 * 
	 * @param source the source of the update
	 * @return the curried update operation
	 */
	public static Update from( Object source ) {
		return new Update( new Configuration(), source );
	}

}
