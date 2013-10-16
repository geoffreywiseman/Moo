package com.codiform.moo.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.codiform.moo.NoDestinationException;
import com.codiform.moo.TranslationException;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.DefaultObjectTargetFactory;
import com.codiform.moo.translator.ObjectTranslator;
import com.codiform.moo.translator.TranslationTargetFactory;
import com.codiform.moo.translator.ValueTypeTranslator;

/**
 * A translation session contains information relevant to a particular translation pass. This
 * typically corresponds to a single invocation of the translator, although you could retain a
 * translation session if you wished to re-use it.
 * 
 * <p>
 * The TranslationSession is not intended to be thread-safe; it is a stateful construct, and several
 * threads running on it at once would have an undefined effect.
 * </p>
 */
public class TranslationSession implements TranslationSource {

	protected TranslationCache translationCache;
	private Configuration configuration;
	private Map<String, Object> variables;
	protected Map<Class<? extends TranslationTargetFactory>, TranslationTargetFactory> translationTargetFactoryCache;

	/**
	 * Creates a translation session with a known configuration.
	 * 
	 * @param configuration
	 *            the {@link Configuration} of the translation session
	 */
	public TranslationSession( Configuration configuration ) {
		translationCache = new TranslationCache();
		this.configuration = configuration;
		translationTargetFactoryCache = new HashMap<Class<? extends TranslationTargetFactory>, TranslationTargetFactory>();
	}

	/**
	 * Creates a translation session with a known configuration and with context variables.
	 * 
	 * @param configuration
	 *            the {@link Configuration} of the translation session
	 * @param variables
	 *            variables which can be used by translations to get external data or perform
	 *            external logic
	 */
	public TranslationSession( Configuration configuration, Map<String, Object> variables ) {
		this( configuration );
		this.variables = variables;
	}

	public <T> T getTranslation( Object source, Class<T> destinationClass ) {
		return getTranslation( source, DefaultObjectTargetFactory.class, destinationClass );
	}

	public <T> T getTranslation( Object source, Class<? extends TranslationTargetFactory> factory, Class<T> destinationClass ) {
		T translated = translationCache.getTranslation( source, destinationClass );
		if ( translated == null )
			translated = translate( source, factory, destinationClass );
		return translated;
	}

	/**
	 * Updates an object from a source object, performing any necessary translations along the way.
	 * 
	 * @param source
	 *            the object from which the values should be retrieved (and, if necessary,
	 *            translated)
	 * @param destination
	 *            the object to which the values should be applied
	 */
	@Override
	public void update( Object source, Object destination ) {
		assureDestination( destination );
		configuration.getTranslator( destination.getClass() ).castAndUpdate( source, destination, this, variables );
	}

	private void assureDestination( Object destination ) {
		if ( destination == null ) {
			throw new NoDestinationException();
		}
	}

	private <T> T translate( Object source, Class<? extends TranslationTargetFactory> factory, Class<T> destinationClass ) {
		if ( source == null ) {
			return null;
		} else {
			// Is it a value type or an object?
			ValueTypeTranslator<T> vtt = configuration.getValueTypeTranslator( destinationClass );
			if ( vtt != null ) {
				return vtt.getTranslation( source, destinationClass );
			} else {
				return getObjectTranslation( source, factory, destinationClass );
			}
		}
	}

	private <T> T getObjectTranslation( Object source, Class<? extends TranslationTargetFactory> factoryType, Class<T> destinationClass ) {
		ObjectTranslator<T> translator = getTranslator( destinationClass );
		TranslationTargetFactory factory = getTranslationTargetFactory( factoryType );
		T translated = factory.getTranslationTargetInstance( source, destinationClass );
		if( translated == null )
			throw new TranslationException( "Translation target factory (" + factory + ") returned null instance; cannot translate." );
		translationCache.putTranslation( source, translated );
		translator.update( source, translated, this, variables );
		return translated;
	}

	public TranslationTargetFactory getTranslationTargetFactory( Class<? extends TranslationTargetFactory> factoryType ) {
		if ( translationTargetFactoryCache.containsKey( factoryType ) )
			return translationTargetFactoryCache.get( factoryType );
		else {
			try {
				TranslationTargetFactory instance = factoryType.newInstance();
				translationTargetFactoryCache.put( factoryType, instance );
				return instance;
			} catch ( InstantiationException cause ) {
				throw new TranslationException( "Could not create translation target factory: " + factoryType, cause );
			} catch ( IllegalAccessException cause ) {
				throw new TranslationException( "Could not create translation target factory: " + factoryType, cause );
			}
		}
	}

	private <T> ObjectTranslator<T> getTranslator( Class<T> destination ) {
		return configuration.getTranslator( destination );
	}

	@Override
	public <T> List<T> getEachTranslation( List<?> sources, Class<T> destinationClass ) {
		List<T> results = new ArrayList<T>();
		for ( Object source : sources ) {
			results.add( getTranslation( source, destinationClass ) );
		}
		return results;
	}

	@Override
	public <T> Set<T> getEachTranslation( Set<?> sources, Class<T> destinationClass ) {
		Set<T> results = new HashSet<T>();
		for ( Object source : sources ) {
			results.add( getTranslation( source, destinationClass ) );
		}
		return results;
	}

	@Override
	public <T> Collection<T> getEachTranslation( Collection<?> sources, Class<T> destinationClass ) {
		List<T> results = new ArrayList<T>();
		for ( Object source : sources ) {
			results.add( getTranslation( source, destinationClass ) );
		}
		return results;
	}

}
