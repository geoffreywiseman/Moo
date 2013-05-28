package com.codiform.moo;

/**
 * Missing source property exceptions indicate that the source property couldn't be found,
 * usually because the expression can't be interpreted, or there's supported factory for
 * the property's prefix. 
 */
public class MissingSourcePropertyException extends TranslationException {

	private static final long serialVersionUID = -7805671888971187925L;

	private static final String MESSAGE = "Could not find required source property for expression '%s'";

	private String propertyExpression;

	public MissingSourcePropertyException( String propertyExpression ) {
		super( getMessage( propertyExpression ) );
		this.propertyExpression = propertyExpression;
	}

	public MissingSourcePropertyException( String propertyExpression, Throwable cause ) {
		super( getMessage( propertyExpression ), cause );
		this.propertyExpression = propertyExpression;
	}

	private static String getMessage( String propertyExpression ) {
		return String.format( MESSAGE, propertyExpression );
	}

	public String getPropertyExpression() {
		return propertyExpression;
	}

}
