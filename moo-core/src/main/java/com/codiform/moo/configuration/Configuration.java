package com.codiform.moo.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codiform.moo.MissingSourcePropertyException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.property.Property;
import com.codiform.moo.property.source.ReflectionSourcePropertyFactory;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import com.codiform.moo.translator.ArrayTranslator;
import com.codiform.moo.translator.CollectionTranslator;
import com.codiform.moo.translator.StringValueTypeTranslator;
import com.codiform.moo.translator.ObjectTranslator;
import com.codiform.moo.translator.TranslatorFactory;
import com.codiform.moo.translator.ValueTypeTranslator;

/**
 * Represents a configuration of Moo; this can contain no information, at which point Moo work from
 * convention and use reflection, or it can use outside configuration, which is of particular merit
 * when you'd like to do complex object mapping without marking up the objects you intend to map.
 */
public class Configuration implements TranslatorFactory {

	private CollectionTranslator collectionTranslator;
	private Map<Class<?>, ValueTypeTranslator<?>> valueTypeTranslators;
	private ArrayTranslator arrayTranslator;
	private boolean performingDefensiveCopies = true;
	private boolean sourcePropertyRequired = true;
	private AccessMode defaultAccessMode = AccessMode.FIELD;
	private List<SourcePropertyFactory> sourcePropertyFactories = new ArrayList<SourcePropertyFactory>();
	private Logger log = LoggerFactory.getLogger( getClass() );

	/**
	 * Creates a default configuration.
	 */
	public Configuration() {
		collectionTranslator = new CollectionTranslator( this );
		valueTypeTranslators = new HashMap<Class<?>,ValueTypeTranslator<?>>();
		arrayTranslator = new ArrayTranslator( this );
		sourcePropertyFactories = new ArrayList<SourcePropertyFactory>();
		
		configureDefaults( );
		configureExtensions();
	}

	private void configureDefaults() {
		sourcePropertyFactories.add( new ReflectionSourcePropertyFactory() );
		valueTypeTranslators.put( String.class, new StringValueTypeTranslator() );
	}

	/**
	 * Adding optional extensions to the core configuration.
	 */
	private void configureExtensions() {
		try {
			Class<?> originClass = Class.forName( "com.codiform.moo.property.source.MvelSourcePropertyFactory" );
			sourcePropertyFactories.add( (SourcePropertyFactory)originClass.newInstance() );
		} catch ( ClassNotFoundException e ) {
			// No MVEL Extension. That's ok. In fact, to be expected.
		} catch ( InstantiationException exception ) {
			log.warn( "Instantiation exception while configuring extensions.", exception );
		} catch ( IllegalAccessException exception ) {
			log.warn( "Instantiation exception while configuring extensions.", exception );
		}
	}

	/**
	 * Used to control whether or not arrays and collections should be copied as a defensive
	 * measure.
	 * 
	 * <p>
	 * This defaults to true. If you know that you will not modify the array or collection (e.g. if
	 * you're going to serialize it right away) this might introduce a small amount of additional
	 * performance overhead which you can remove by disabling.
	 * </p>
	 * 
	 * @param performingDefensiveCopies
	 * @see #isPerformingDefensiveCopies()
	 */
	public void setPerformingDefensiveCopies( boolean performingDefensiveCopies ) {
		this.performingDefensiveCopies = performingDefensiveCopies;
	}

	/**
	 * Indicates if arrays and collections should be copied as a defensive measure to ensure that
	 * the source arrays and collections aren't accidentally modified by modifying the arrays and
	 * collections in the translation.
	 * 
	 * @see #setPerformingDefensiveCopies(boolean)
	 */
	public boolean isPerformingDefensiveCopies() {
		return this.performingDefensiveCopies;
	}

	public <T> ObjectTranslator<T> getTranslator( Class<T> destinationClass ) {
		return new ObjectTranslator<T>( destinationClass, this );
	}

	public CollectionTranslator getCollectionTranslator() {
		return collectionTranslator;
	}

	public ArrayTranslator getArrayTranslator() {
		return arrayTranslator;
	}

	/**
	 * Controls whether a source property is required for a translation to succeed. If you wish
	 * translation to fail with a {@link com.codiform.moo.MissingSourcePropertyException} when a property in
	 * the source object cannot be found to correspond to a destination property, set this value to
	 * true.
	 * 
	 * @param sourcePropertyRequired
	 *            true if a source property is required for each destination property
	 */
	public void setSourcePropertiesRequired( boolean sourcePropertyRequired ) {
		this.sourcePropertyRequired = sourcePropertyRequired;
	}

	/**
	 * Indicates if source properties are required for translation to succeed. The default value,
	 * false, allows the translation to continue even if source properties can't be found to match
	 * all of the destination fields.
	 * 
	 * @return true if source properties are required, false otherwise
	 */
	public boolean isSourcePropertyRequired() {
		return sourcePropertyRequired;
	}

	public AccessMode getDefaultAccessMode() {
		return defaultAccessMode;
	}

	public void setDefaultAccessMode( AccessMode accessMode ) {
		if ( accessMode == null ) {
			throw new NullPointerException( "Default access mode cannot be null." );
		} else {
			this.defaultAccessMode = accessMode;
		}
	}

	public SourceProperty getSourceProperty( Property property ) {
		String expression = property.getSourcePropertyExpression().trim();
		return getSourceProperty( expression );
	}

	public SourceProperty getSourceProperty( String expression ) {
		String prefix = getPrefix( expression );
		if ( prefix == null ) {
			return getUnprefixedSourceProperty( expression );
		} else {
			return getPrefixedSourceProperty( prefix, expression );
		}
	}

	private SourceProperty getPrefixedSourceProperty( String prefix, String expression ) {
		String unprefixed = expression.substring( prefix.length() + 1 );
		for ( SourcePropertyFactory item : sourcePropertyFactories ) {
			if ( item.supportsPrefix( prefix ) ) {
				SourceProperty property = item.getSourceProperty( prefix, unprefixed );
				if ( property != null ) {
					return property;
				}
			}
		}
		throw new MissingSourcePropertyException( expression );
	}

	private SourceProperty getUnprefixedSourceProperty( String expression ) {
		for ( SourcePropertyFactory item : sourcePropertyFactories ) {
			SourceProperty property = item.getSourceProperty( expression );
			if ( property != null ) {
				return property;
			}
		}
		throw new MissingSourcePropertyException( expression );
	}

	private String getPrefix( String expression ) {
		int colonIndex = expression.indexOf( ':' );
		if ( colonIndex > 0 && colonIndex < ( expression.length() - 1 ) ) {
			return expression.substring( 0, colonIndex );
		} else {
			return null;
		}
	}
	
	/* package */ boolean containsSourcePropertyFactory( Class<? extends SourcePropertyFactory> factoryType ) {
		for( SourcePropertyFactory factory : sourcePropertyFactories ) {
			if( factoryType.isInstance( factory ) ) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings( "unchecked" )
	public <V> ValueTypeTranslator<V> getValueTypeTranslator( Class<V> destinationType ) {
		return (ValueTypeTranslator<V>)valueTypeTranslators.get( destinationType );
	}

}
