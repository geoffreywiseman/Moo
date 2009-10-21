package com.codiform.moo;

/**
 * Runtime exception that acts as the root exception for errors that might
 * take place while working with Moo.  There are a number of possible sources
 * for this error.  It is not currently expected that you'll try and recover
 * from these. 
 */
public class TranslationException extends RuntimeException {
	
	/**
	 * Create a translation exception with a message and an exception that caused the translation error.
	 * 
	 * @param message a message indicating what the error was
	 * @param cause the exception that caused this one
	 */
	public TranslationException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Create a translation exception with a message.
	 * 
	 * @param message a message indicating what the error was
	 */
	public TranslationException( String message ) {
		super( message );
	}

	private static final long serialVersionUID = 1L;
}
