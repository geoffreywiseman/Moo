package com.codiform.moo.curry;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSession;

public class Update {

	private Object source;
	private Configuration configuration;
	
	public Update( Configuration configuration, Object source ) {
		this.configuration = configuration;
		this.source = source;
	}
	
	public void to( Object destination ) {
		new TranslationSession( configuration ).update(source, destination);
	}
	
	public static Update from( Object source ) {
		return new Update( new Configuration(), source );
	}

}
