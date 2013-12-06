package com.codiform.moo.property;

public interface MapProperty extends Property {
	
	/**
	 * Whether or not orphans (items found in the destination with no match in the source) should be removed.
	 * 
	 * @return true if orphans should be removed (default); false otherwise
	 */
	boolean shouldRemoveOrphans();
	
	/**
	 * The type to which keys to be inserted into this map should be translated (if necessary), or null if
	 * the key type is unspecified.
	 * 
	 * @return the type, or null 
	 */
	Class<?> getKeyClass();
	
	/**
	 * The type to which values to be inserted into this map should be translated (if necessary), or null if
	 * the value type is unspecified.
	 * 
	 * @return the type, or null 
	 */
	Class<?> getValueClass();

	/**
	 * Gets the source expression for the map key, if one was specified, null otherwise.
	 * 
	 * @return the source expression
	 */
	String getKeySource();

	/**
	 * When copying or translating the map, should null keys be allowed?
	 * 
	 * @return true if null keys are allowed, false if they are not
	 */
	boolean allowNullKeys();
}
