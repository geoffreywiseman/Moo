package com.codiform.moo.property.source;

import com.codiform.moo.MissingSourcePropertyValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectionSourcePropertyTest {

	ReflectionSourceProperty rsp = new ReflectionSourceProperty( "value" );

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPublicField() {
		PublicFieldSource source = new PublicFieldSource();
		source.value = "Public Field Value";
		assertEquals( "Public Field Value", rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPrivateField() {
		PrivateFieldSource source = new PrivateFieldSource( "Private Value" );
		assertEquals( "Private Value", rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPublicGetGetter() {
		PublicGetGetterSource source = new PublicGetGetterSource( 242 );
		assertEquals( 242, rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPublicIsGetter() {
		PublicIsGetterSource source = new PublicIsGetterSource( true );
		assertEquals( true, rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPrivateGetGetter() {
		PrivateGetGetterSource source = new PrivateGetGetterSource( 123.456f );
		assertEquals( 123.456f, rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromPrivateIsGetter() {
		PrivateIsGetterSource source = new PrivateIsGetterSource( false );
		assertEquals( false, rsp.getValue( source ) );
	}

	@Test( expected = MissingSourcePropertyValueException.class )
	public void testGetValueThrowsMissingSourcePropertyExceptionIfNoPropertyFound() {
		rsp.getValue( new Object( ) );
	}

	private static class PublicFieldSource {
		@SuppressWarnings( "unused" )
		public String value;
	}

	private static class PrivateFieldSource {
		public PrivateFieldSource( String value ) {
			this.value = value;
		}

		@SuppressWarnings( "unused" )
		private String value;
	}

	private static class PublicGetGetterSource {
		public PublicGetGetterSource( int value ) {
			this.hidden = value;
		}

		private int hidden;

		@SuppressWarnings( "unused" )
		public int getValue() {
			return hidden;
		}
	}

	private static class PublicIsGetterSource {
		public PublicIsGetterSource( boolean value ) {
			this.hidden = value;
		}

		private boolean hidden;

		@SuppressWarnings( "unused" )
		public boolean isValue() {
			return hidden;
		}
	}

	private static class PrivateGetGetterSource {
		public PrivateGetGetterSource( float value ) {
			this.hidden = value;
		}

		private float hidden;

		@SuppressWarnings( "unused" )
		private float getValue() {
			return hidden;
		}
	}

	private static class PrivateIsGetterSource {
		public PrivateIsGetterSource( boolean value ) {
			this.hidden = value;
		}

		private boolean hidden;

		@SuppressWarnings( "unused" )
		private boolean isValue() {
			return hidden;
		}
	}
}
