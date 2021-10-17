package com.codiform.moo.configuration;

import com.codiform.moo.annotation.AccessMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a configuration of Moo; this can contain no information, at which point Moo work from
 * convention and use reflection, or it can use outside configuration, which is of particular merit
 * when you'd like to do complex object mapping without marking up the objects you intend to map.
 */
public class Configuration {

	private boolean performingDefensiveCopies = true;
	private boolean sourcePropertyRequired = true;
	private AccessMode defaultAccessMode = AccessMode.FIELD;
	private Logger log = LoggerFactory.getLogger( getClass() );

	/**
	 * Creates a default configuration.
	 */
	public Configuration() {
		log.debug( "Configuration initialized." );
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
	 * @param performingDefensiveCopies true if arrays and collections should be copied defensively
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


}
