package com.codiform.moo.property.source;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SingleCharacterReflectionSourcePropertyTest {

	ReflectionSourceProperty rsp = new ReflectionSourceProperty( "v" );

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromField() {
		PublicFieldSource source = new PublicFieldSource();
		source.v = "Public Field Value";
		assertEquals( "Public Field Value", rsp.getValue( source ) );
	}

	@Test
	public void testReflectionSourcePropertyCanRetrieveValueFromGetter() {
		PublicGetGetterSource source = new PublicGetGetterSource( 242 );
		assertEquals( 242, rsp.getValue( source ) );
	}

	private static class PublicFieldSource {
		@SuppressWarnings( "unused" )
		public String v;
	}

	private static class PublicGetGetterSource {
		public PublicGetGetterSource( int value ) {
			this.hidden = value;
		}

		private int hidden;

		@SuppressWarnings( "unused" )
		public int getV() {
			return hidden;
		}
	}
}
