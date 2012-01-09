package com.codiform.moo.annotation;

import com.codiform.moo.TranslationException;

/**
 * When an annotation cannot be correctly interpreted because of an error made
 * in how it was applied for with the combination of attributes contained
 * therein.
 */
public class InvalidAnnotationException extends TranslationException {

	private static final long serialVersionUID = 2848629746692310508L;
	
	private String propertyName;
	private Class<?> declaringClass;

	public InvalidAnnotationException(String message, String propertyName,
			Class<?> declaringClass) {
		super( String.format( message, propertyName,
				declaringClass.getSimpleName() ) );
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
