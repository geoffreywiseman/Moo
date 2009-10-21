package com.codiform.moo.session;

import org.junit.Assert;
import org.junit.Test;


public class TranslationCacheTest {
	
	@Test
	public void testNewCacheDoesNotContainTranslations() {
		TranslationCache cache = new TranslationCache();
		cache.getTranslation("One", Integer.class);
	}

	@Test
	public void testCacheRetrievesStoredTranslations() {
		TranslationCache cache = new TranslationCache();
		String source = "One";
		cache.putTranslation(source, Integer.valueOf(1));
		Assert.assertEquals( Integer.valueOf(1), cache.getTranslation(source, Integer.class) );
	}
}
