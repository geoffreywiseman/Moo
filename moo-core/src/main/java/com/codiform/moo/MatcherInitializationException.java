package com.codiform.moo;


public class MatcherInitializationException extends TranslationException {

	private static final long serialVersionUID = 6930063736579469352L;

	private Class<?> matcherType;

	public MatcherInitializationException(
			Class<?> matcherType,
			Exception cause) {
		super( "Cannot initialize matcher: " + matcherType.getName(), cause );
		this.matcherType = matcherType;
	}

	public Class<?> getMatcherType() {
		return matcherType;
	}

}
