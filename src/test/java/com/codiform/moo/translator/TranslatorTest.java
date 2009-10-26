package com.codiform.moo.translator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.source.TranslationSource;

@RunWith(MockitoJUnitRunner.class)
public class TranslatorTest {

	@Mock
	private Configuration configuration;
	
	@Mock
	private TranslationSource translationSource;

	private Translator<Destination> translator = new Translator<Destination>(
			Destination.class, configuration);

	@Test
	public void testUpdate() {
		Source source = new Source( "TranslatorTest" );
		Destination destination = new Destination();
		translator.update( source, destination, translationSource );
		
		Assert.assertEquals( "TranslatorTest", destination.getValue() );
	}

	@Test
	public void testCastAndUpdate() {
		Source source = new Source( "TranslatorTest" );
		Destination destination = new Destination();
		translator.update( source, destination, translationSource );
		
		Assert.assertEquals( "TranslatorTest", destination.getValue() );
	}

	@Test
	public void testCreate() {
		Assert.assertNotNull(translator.create());
	}

	public static class Source {
		private String value;

		public Source(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static class Destination {
		private String value;

		public String getValue() {
			return value;
		}
	}

}
