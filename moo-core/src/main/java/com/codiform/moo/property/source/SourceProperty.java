package com.codiform.moo.property.source;

import java.util.Map;

/**
 * Represents a generic origin for a property value, whether this is a field, a method or something
 * more complicated like a factory, an expression in an expression language, etc.
 */
public interface SourceProperty {
	
	/**
	 * Gets the value from the source object.
	 * 
	 * @param source the object from which the value should be retrieved
	 * @throws MissingSourcePropertyValueException if the origin can't retrieve a value from the source object
	 * @return the value retrieved from the source
	 */
	Object getValue( Object source );

	/**
	 * Gets the value from the source object.
	 * 
	 * @param source the object from which the value should be retrieved
	 * @param variables variables that can be referenced in the origin expression
	 * @return the value retrieved from the source
	 */
	Object getValue( Object source, Map<String, Object> variables );

	String getExpression();
}
