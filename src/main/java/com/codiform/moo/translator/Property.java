package com.codiform.moo.translator;



/**
 * Represents a destination into which a value can be stored.
 */
public interface Property {

	/**
	 * Gets the name of the property this represents. Property names are
	 * determined through the use of reflection.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Sets the value of the property on an object instance.
	 * 
	 * @param instance
	 *            the object instance on which the value is to be set
	 * @param value
	 *            the value to be set
	 */
	void setValue(Object instance, Object value);

	/**
	 * Gets the type that can be stored in this property.
	 * 
	 * @return the type
	 */
	Class<?> getType();

	/**
	 * Gets the class in which this property is declared.
	 */
	Class<?> getDeclaringClass();

	/**
	 * Gets the translation expression used to find this property on another
	 * object.
	 * 
	 * @return the expression
	 */
	String getTranslationExpression();

	/**
	 * Indicates if the property should be translated (has translate=true in the
	 * {@link Property} annotation)
	 * 
	 * @return boolean if the property should be translated
	 */
	boolean shouldBeTranslated();

	/**
	 * Indicates if the property supports a null value (e.g. can store and
	 * retrieve one).
	 * 
	 * @return true if a null value is supported; false otherwise
	 */
	boolean canSupportNull();

	/**
	 * Indicates if the property was explicitly defined using @Property or if it
	 * was implicitly defined by the presence of a field or method matching the
	 * access mode and other criteria.
	 * 
	 * @return true if the property was explicitly defined
	 */
	boolean isExplicit();

	/**
	 * Indicates if the property should be ignored (no value stored during
	 * Translate or Update).
	 * 
	 * @return true if the property should be ignored
	 */
	boolean isIgnored();

	/**
	 * Indicates if the property should be updated rather than replaced when an
	 * update is taking place. In particular, this implies that an update can
	 * succeed because the destination is mutable and the important properties
	 * can be copied from one side to the other.
	 * 
	 * @return true if the object should be updated, assuming it can be
	 */
	boolean shouldUpdate();

	/**
	 * Indicates if the property can be used to retrieve a value, or if the
	 * value can only be set.
	 * 
	 * @return true if the value can be retrieved, false if the value can only
	 *         be set
	 */
	boolean canGetValue();

	/**
	 * Retrieves the value represented by the property from an object instance.
	 * 
	 * @param the
	 *            instance from which the value should be retrieved
	 * @return the current value of the property
	 * @throws UnsupportedOperationException
	 *             if the property's value can not be retrieved
	 * @see #canGetValue()
	 */
	Object getValue(Object instance);

	/**
	 * Does this property require that there be a source property that maps to
	 * this one, or is it an optional requirement?
	 * 
	 * @param default the default setting as to whether or not source properties
	 *        are required based on Moo configuration
	 * @return true if the source property is required, false if the property is
	 *         optional
	 */
	boolean isSourceRequired(boolean defaultSetting);

	/**
	 * Indicates if the property is the specified type or a subtype of the specified type.
	 * 
	 * @param type the type against which the property should be compared
	 * @return true if the property is of the type or a subtype
	 */
	boolean isTypeOrSubtype(Class<?> type);

}
