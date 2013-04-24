package com.codiform.moo;

/**
 * Indicates a problem when attempting to instantiate a class as a destination
 * for translation. This is usually one of the usual suspects, like exception in
 * initializer, illegal access, etc.
 */
public class TranslationInitializationException extends TranslationException {

	private static final long serialVersionUID = 3310855072065163211L;

	public TranslationInitializationException(String message, Throwable cause) {
		super( message, cause );
	}

}
