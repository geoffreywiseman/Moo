package com.codiform.moo;

/**
 * Indicates a problem when attempting to instantiate a class as a destination
 * for translation. This is usually one of the usual suspects, like exception in
 * initializer, illegal access, etc.
 */
public class GetPropertyException extends TranslationException {
	
	private static final long serialVersionUID = -7680847961914575143L;
	
	private String propertyName;
	private Class<?> propertyType;

	public GetPropertyException(String name, Class<?> type,
			Throwable cause) {
		super( String.format( "Cannot get value for property %s (%s)",
				name, type.getSimpleName() ), cause );
		this.propertyName = name;
		this.propertyType = type;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

}
