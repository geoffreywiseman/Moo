package com.codiform.moo.property.source;

/**
 * A source for origin objects which is responsible for determining the way in which 
 * origin values are retrieved. An origin source might use reflection to access fields,
 * or a translation expression in an expression language, a factory, or something else
 * entirely. 
 */
public interface SourcePropertyFactory {
	
	/**
	 * Retrieves the origin corresponding to a property.
	 * 
	 * @param property the property for which an origin is to be retrieved
	 * @return the origin object
	 */
	SourceProperty getSourceProperty( String expression );

	/**
	 * Gets the origin corresponding to a prefixed expression, along the lines of "<prefix>:<expression>"
	 * 
	 * @param expressionPrefix the prefix of the expression
	 * @param unprefixedExpression the expression, with the prefix removed
	 * @return the origin object
	 */
	SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression );

	/**
	 * Indicates if the origin source supports the specified prefix.
	 * 
	 * @param prefix the prefix about whose support an inquiry is being made
	 * @return true if the prefix is supported
	 */
	boolean supportsPrefix( String prefix );

}
