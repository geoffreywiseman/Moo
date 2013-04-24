package com.codiform.moo;

/**
 * Indicates that a translation is not supported because the types are incompatible.
 */
public class IncompatibleTypeTranslationException extends UnsupportedTranslationException {

	private static final long serialVersionUID = 5324683855325641246L;

	private Object value;

	private String propertyName;

	private Class<?> propertyType;

	public IncompatibleTypeTranslationException(Object value,
			String propertyName, Class<?> propertyType) {
		super("Cannot put value " + value + " (" + value.getClass().getName()
				+ ") into property '" + propertyName + "' ("
				+ propertyType.getName() + ")");
		this.value = value;
		this.propertyName = propertyName;
		this.propertyType = propertyType;
	}

	public Object getValue() {
		return value;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

}
