package com.codiform.moo;

/**
 * Used when Moo has a source property, a means of accessing a value, but can't find a value
 * to go with it.
 */
public class MissingSourcePropertyValueException extends TranslationException {

	private static final long serialVersionUID = -7805671888971187925L;

	private static final String MESSAGE = "Could not find required source property value for expression '%s' (source: %s)";

	private String propertyExpression;

	public MissingSourcePropertyValueException(String propertyExpression, Class<?> sourceClass,
			Throwable cause) {
		super( getMessage( propertyExpression, sourceClass ), cause );
		this.propertyExpression = propertyExpression;
	}

	private static String getMessage(String propertyExpression,
			Class<?> sourceClass) {
		return String.format( MESSAGE, propertyExpression, sourceClass.getName() );
	}

	public MissingSourcePropertyValueException(String propertyExpression, Class<?> sourceClass ) {
		super( getMessage( propertyExpression, sourceClass ) );
		this.propertyExpression = propertyExpression;
	}

	public String getPropertyExpression() {
		return propertyExpression;
	}

}
