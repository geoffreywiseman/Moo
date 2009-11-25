package com.codiform.moo;


public class NothingToTranslateException extends TranslationException {

	private static final long serialVersionUID = -5271404016031303386L;
	
	private Class<?> sourceClass;
	private Class<?> destinationClass;

	public NothingToTranslateException(Class<?> sourceClass,
			Class<?> destinationClass) {
		super("Cannot translate " + sourceClass.getName() + " to "
				+ destinationClass.getName()
				+ ", as they seem to have no common properties.");
		this.sourceClass = sourceClass;
		this.destinationClass = destinationClass;
	}

	public Class<?> getSourceClass() {
		return sourceClass;
	}

	public Class<?> getDestinationClass() {
		return destinationClass;
	}

}
