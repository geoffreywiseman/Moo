package com.codiform.moo.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;

public class PropertyFactoryTest {

	@Test
	public void testCreatesFieldProperty() throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "validField" ), AccessMode.FIELD );
		assertEquals( "validField", property.getName() );
		assertFalse( property.isExplicit() );
		assertFalse( property.isIgnored() );
		assertSame( int.class, property.getType() );
	}

	@Test
	public void testInvalidPropertyExceptionIfFieldIsStatic()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getField( "staticField" ), AccessMode.FIELD );
			fail( "Should not have created a property for a static field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "staticField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "static field" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfFieldIsFinal()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getField( "finalField" ), AccessMode.FIELD );
			fail( "Should not have created a property for a final field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "finalField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "final field" ) );
		}
	}

	private Field getField(String fieldName) throws NoSuchFieldException {
		return PropertyContainer.class.getDeclaredField( fieldName );
	}

	@SuppressWarnings("unused")
	private static class PropertyContainer {

		@com.codiform.moo.annotation.Property
		private static boolean staticField;

		@com.codiform.moo.annotation.Property
		private final String finalField = "final";

		private int validField;
	}
}
