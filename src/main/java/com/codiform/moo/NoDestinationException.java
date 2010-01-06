package com.codiform.moo;

public class NoDestinationException extends TranslationException {

	private static final long serialVersionUID = -5852561146079545953L;

	public NoDestinationException() {
		super("Cannot perform an update to a null destination.");
	}

}
