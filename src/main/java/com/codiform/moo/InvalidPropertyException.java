package com.codiform.moo;

import com.codiform.moo.translator.Property;

/**
 * Thrown when a method or field is marked with {@link Property} but can't be
 * used as a property.
 */
public class InvalidPropertyException extends TranslationException {

	private static final long serialVersionUID = 2394094419586942159L;

	private String propertyName;
	private Class<?> declaringClass;

	public InvalidPropertyException(String propertyName, Class<?> declaringClass, String message) {
		super(String.format(message, propertyName, declaringClass.getName()));
		this.propertyName = propertyName;
		this.declaringClass = declaringClass;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public Class<?> getDeclaringClass() {
		return declaringClass;
	}

}
