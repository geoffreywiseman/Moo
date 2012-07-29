package com.codiform.moo;

/**
 * Indicates a problem when attempting to instantiate a class as a destination
 * for translation. This is usually one of the usual suspects, like exception in
 * initializer, illegal access, etc.
 */
public class SetPropertyException extends TranslationException {
	
	private static final long serialVersionUID = -973162070342003782L;
	
	private String propertyName;
	private Class<?> propertyType;
	private Object value;

	public SetPropertyException(String name, Class<?> type, Object value,
			Throwable cause) {
		super( String.format( "Cannot set value (%s) for property %s (%s)",
				value, name, type.getSimpleName() ), cause );
		this.propertyName = name;
		this.propertyType = type;
		this.value = value;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public Object getValue() {
		return value;
	}

}
