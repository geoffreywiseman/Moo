package com.codiform.moo.translator;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.Property;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.session.TranslationSource;

@RunWith( MockitoJUnitRunner.class )
public class TranslatorTest {

	@Mock
	private Configuration configuration;

	@Mock
	private SourceProperty origin;
	
	@Mock
	private TranslationSource translationSource;

	private Translator<Destination> translator;

	@Before
	public void setUp() {
		when( configuration.getDefaultAccessMode() ).thenReturn( AccessMode.FIELD );
		when( configuration.getOrigin( Mockito.any( Property.class ) ) ).thenReturn( origin );
		when( origin.getValue( Mockito.any(Source.class) ) ).thenReturn( "TranslatorTest" );
		translator = new Translator<Destination>( Destination.class, configuration );
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

	@Test
	public void testCreate() {
		Assert.assertNotNull( translator.create() );
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
