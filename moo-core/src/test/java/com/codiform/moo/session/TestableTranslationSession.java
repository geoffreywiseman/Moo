package com.codiform.moo.session;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.TranslationTargetFactory;

/**
 * Making some of the internals of TranslationSession available for testing,
 * since they allow some kinds of tests that would be much harder otherwise,
 * but we don't want to expose these in the public API.
 */
public class TestableTranslationSession extends TranslationSession {
	
	public TestableTranslationSession( Configuration configuration ) {
		super( configuration );
		// TODO Auto-generated constructor stub
	}

	public <T extends TranslationTargetFactory> void cacheTranslationTargetFactory( Class<T> factoryClass, T factoryInstance ) {
		translationTargetFactoryCache.put( factoryClass, factoryInstance );
	}

	public void setTranslationCache( TranslationCache cache ) {
		this.translationCache = cache;
	}


}
