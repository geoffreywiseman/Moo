package com.codiform.moo;

public class TranslationException extends RuntimeException {
	
	public TranslationException( String message, Throwable cause ) {
		super( message, cause );
	}

	public TranslationException( String message ) {
		super( message );
	}

	private static final long serialVersionUID = 1L;
}
