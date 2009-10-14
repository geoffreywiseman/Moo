package com.codiform.moo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSession;

public class Moo {

	private Configuration configuration;

	public Moo( ) {
		this( new Configuration() );
	}
	
	public Moo( Configuration configuration ) {
		this.configuration = configuration;
	}

	private TranslationSession newSession() {
		return new TranslationSession(configuration);
	}

	public <T> T translate( Class<T> destinationClass, Object source ) {
		return newSession().translate( destinationClass, source );
	}

	public <T> List<T> translateEach( Class<T> destinationClass, List<?> sources ) {
		return newSession().translateEach( destinationClass, sources );
	}

	public <T> Collection<T> translateEach( Class<T> destinationClass, Collection<?> sources ) {
		return newSession().translateEach( destinationClass, sources );
	}

	public <T> Set<T> translateEach( Class<T> destinationClass, Set<?> sources ) {
		return newSession().translateEach( destinationClass, sources );
	}

}
