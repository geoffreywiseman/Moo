package com.codiform.moo.translator;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import com.codiform.moo.session.TranslationSource;

@RunWith( MockitoJUnitRunner.class )
public class TranslatorTest {

	@Mock
	private Configuration configuration;
	
	@Mock
	private SourcePropertyFactory spf;
	
	@Mock
	private TranslatorFactory tf;

	@Mock
	private SourceProperty sourceProperty;
	
	@Mock
	private TranslationSource translationSource;

	private ObjectTranslator<Destination> translator;

	@Before
	public void setUp() {
		when( configuration.getDefaultAccessMode() ).thenReturn( AccessMode.FIELD );
		when( spf.getSourceProperty( Mockito.any( String.class ) ) ).thenReturn( sourceProperty );
		when( sourceProperty.getValue( Mockito.any(Source.class) ) ).thenReturn( "TranslatorTest" );
		translator = new ObjectTranslator<Destination>( Destination.class, configuration, tf, spf );
	}

	@Test
	public void testUpdate() {
		Assert.assertNotNull( configuration );
		Source source = new Source( "TranslatorTest" );
		Destination destination = new Destination();
		translator.update( source, destination, translationSource, null );

		Assert.assertEquals( "TranslatorTest", destination.getValue() );
	}

	@Test
	public void testCastAndUpdate() {
		Source source = new Source( "TranslatorTest" );
		Destination destination = new Destination();
		translator.update( source, destination, translationSource, null );

		Assert.assertEquals( "TranslatorTest", destination.getValue() );
	}

	public static class Source {
		private String value;

		public Source( String value ) {
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
