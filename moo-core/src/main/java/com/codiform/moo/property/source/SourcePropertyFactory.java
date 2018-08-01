package com.codiform.moo.property.source;

/**
 * A factory for source property objects which is responsible for determining the way in which
 * source property values are retrieved. An source property factory might use reflection to access
 * fields, or a translation expression in an expression language, or something else entirely.
 */
public interface SourcePropertyFactory {

	/**
	 * Retrieves the source property corresponding to an expression.
	 * 
	 * @param expression
	 *            the expression for which a source property is to be retrieved
	 * @return the source property object
	 */
	SourceProperty getSourceProperty( String expression );

	/**
	 * Gets the source property corresponding to a prefixed expression, along the lines of
	 * "&lt;prefix&gt;:&lt;expression&gt;"
	 * 
	 * @param expressionPrefix
	 *            the prefix of the expression
	 * @param unprefixedExpression
	 *            the expression, with the prefix removed
	 * @return the source property object
	 */
	SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression );

	/**
	 * Indicates if the source property factory supports the specified prefix.
	 * 
	 * @param prefix
	 *            the prefix about whose support an inquiry is being made
	 * @return true if the prefix is supported
	 */
	boolean supportsPrefix( String prefix );

}
