package com.codiform.moo.session;

import java.util.Map;

import com.codiform.moo.translator.TranslationTargetFactory;
import com.codiform.moo.translator.TranslatorFactory;

/**
 * Making some of the internals of TranslationSession available for testing,
 * since they allow some kinds of tests that would be much harder otherwise,
 * but we don't want to expose these in the public API.
 */
public class TestableTranslationSession extends TranslationSession {
	
	public TestableTranslationSession() {
		super();
	}

	public TestableTranslationSession( TranslatorFactory translatorFactory ) {
		super( translatorFactory );
	}
	
	public TestableTranslationSession( TranslatorFactory translatorFactory, Map<String, Object> variables ) {
		super( translatorFactory, variables );
	}

	public <T extends TranslationTargetFactory> void cacheTranslationTargetFactory( Class<T> factoryClass, T factoryInstance ) {
		translationTargetFactoryCache.put( factoryClass, factoryInstance );
	}

	public void setTranslationCache( TranslationCache cache ) {
		this.translationCache = cache;
	}


}
