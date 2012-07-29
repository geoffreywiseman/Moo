package com.codiform.moo;

/**
 * Indicates that a translation is not supported because the destination type
 * does not support the null value found in the source.
 */
public class NullUnsupportedTranslationException extends
		UnsupportedTranslationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6490368018179484960L;

	private String propertyName;

	private Class<?> propertyType;

	public NullUnsupportedTranslationException(String name, Class<?> type) {
		super(
				"Cannot store null in property " + name
						+ " (" + type.getSimpleName() + ")" );
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
