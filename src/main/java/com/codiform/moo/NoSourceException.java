package com.codiform.moo;

public class NoSourceException extends TranslationException {
	
	private static final long serialVersionUID = -6106568300522577681L;

	public NoSourceException() {
		super("Cannot perform a translation from a null source class.");
	}

}
