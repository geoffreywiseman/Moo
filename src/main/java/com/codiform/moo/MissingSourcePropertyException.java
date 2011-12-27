package com.codiform.moo;

public class MissingSourcePropertyException extends TranslationException {

	private static final long serialVersionUID = -7805671888971187925L;

	private static final String MESSAGE = "Could not find required source property for expression '%s' (source: %s)";

	private String propertyExpression;

	public MissingSourcePropertyException(String propertyExpression, Class<?> sourceClass,
			Throwable cause) {
		super( getMessage( propertyExpression, sourceClass ), cause );
		this.propertyExpression = propertyExpression;
	}

	private static String getMessage(String propertyExpression,
			Class<?> sourceClass) {
		return String.format( MESSAGE, propertyExpression, sourceClass.getName() );
	}

	public MissingSourcePropertyException(String propertyExpression, Class<?> sourceClass ) {
		super( getMessage( propertyExpression, sourceClass ) );
		this.propertyExpression = propertyExpression;
	}

	public String getPropertyExpression() {
		return propertyExpression;
	}

}
