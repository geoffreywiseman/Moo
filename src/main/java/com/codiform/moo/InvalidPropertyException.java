package com.codiform.moo;

import com.codiform.moo.translator.Property;

/**
 * Thrown when a method or field is marked with {@link Property} but can't be
 * used as a property.
 */
public class InvalidPropertyException extends TranslationException {

	private static final long serialVersionUID = 2394094419586942159L;

	private Property property;

	public InvalidPropertyException(Property property, String message) {
		super(String.format(message, property.getName(), property
				.getDeclaringClass().getName()));
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

}
