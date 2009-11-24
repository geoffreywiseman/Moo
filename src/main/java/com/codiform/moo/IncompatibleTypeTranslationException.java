package com.codiform.moo;

public class IncompatibleTypeTranslationException extends TranslationException {

	private static final long serialVersionUID = 5324683855325641246L;

	private Object value;

	private String propertyName;

	private Class<?> propertyClass;

	public IncompatibleTypeTranslationException(Object value,
			String propertyName, Class<?> propertyClass) {
		super("Cannot put value " + value + " (" + value.getClass().getName()
				+ ") into property '" + propertyName + "' ("
				+ propertyClass.getName() + ")");
		this.value = value;
		this.propertyName = propertyName;
		this.propertyClass = propertyClass;
	}

	public Object getValue() {
		return value;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
	}

}
