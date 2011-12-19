package com.codiform.moo;

public class MissingSourcePropertyException extends TranslationException {

	private static final long serialVersionUID = -7805671888971187925L;

	private static final String MESSAGE_PREFIX = "Could not find required source property for expression: ";

	private String propertyExpression;

	public MissingSourcePropertyException(String propertyExpression,
			Throwable cause) {
		super( MESSAGE_PREFIX + propertyExpression, cause );
		this.propertyExpression = propertyExpression;
	}

	public MissingSourcePropertyException(String propertyExpression) {
		super( MESSAGE_PREFIX + propertyExpression );
		this.propertyExpression = propertyExpression;
	}

	public String getPropertyExpression() {
		return propertyExpression;
	}

}
