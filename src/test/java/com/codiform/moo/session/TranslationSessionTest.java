package com.codiform.moo.session;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.translator.Translator;


@RunWith(MockitoJUnitRunner.class)
public class TranslationSessionTest {

	@Mock
	private Configuration configuration;
	
	@Mock
	private TranslationCache cache;
	
	private TranslationSession session;
		
	@Before 
	public void setUpSession() {
		session = new TranslationSession(configuration);
		session.setTranslationCache( cache );
	}
	
	@Test
	public void testGetTranslationChecksCacheThenTranslates() {
		Translator<Integer> translator = mockTranslator(Integer.class);
		Mockito.when(configuration.getTranslator(Integer.class)).thenReturn(translator);
		
		session.getTranslation( "One", Integer.class );
		Mockito.verify(cache).getTranslation("One",Integer.class);
		Mockito.verify(translator).create();
	}

	@Test
	public void testStoresMissingTranslationInCache() {
		Integer translation = Integer.valueOf(1);
		Translator<Integer> translator = mockTranslator(Integer.class);
		Mockito.when(configuration.getTranslator(Integer.class)).thenReturn(translator);
		Mockito.when(translator.create()).thenReturn(translation);
		
		session.getTranslation( "One", Integer.class );
		Mockito.verify(cache).getTranslation("One",Integer.class);
		Mockito.verify(translator).create();
		Mockito.verify(cache).putTranslation("One", translation);
	}

	public void testGetTranslationReturnsTranslatedValue() {
		Integer translation = Integer.valueOf(1);
		Translator<Integer> translator = mockTranslator(Integer.class);
		Mockito.when(configuration.getTranslator(Integer.class)).thenReturn(translator);
		Mockito.when(translator.create()).thenReturn(translation);
		
		Integer result = session.getTranslation( "One", Integer.class );
		Assert.assertSame( translation, result );
	}

	@SuppressWarnings("unchecked")
	private <T> Translator<T> mockTranslator(Class<T> translationType) {
		return Mockito.mock(Translator.class);
	}
	
	@Test
	public void testGetTranslationDoesntTranslateIfFoundInCache() {
		Mockito.when(cache.getTranslation("One", Integer.class)).thenReturn(Integer.valueOf(1));
		
		session.getTranslation( "One", Integer.class );
		Mockito.verify(cache).getTranslation("One",Integer.class);
		Mockito.verifyZeroInteractions(configuration);
	}

	@Test
	public void testGetTranslationReturnsCachedValue() {
		Integer translation = Integer.valueOf(1);
		Mockito.when(cache.getTranslation("One", Integer.class)).thenReturn(translation);
		
		Integer result = session.getTranslation( "One", Integer.class );
		Assert.assertSame( translation, result );
	}
}
