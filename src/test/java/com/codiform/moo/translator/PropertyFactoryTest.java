package com.codiform.moo.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;

public class PropertyFactoryTest {

	@Test
	public void testImplicitFieldCreatesFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitField" ), AccessMode.FIELD );
		assertProperty( "implicitField", false, false, int.class, property );
	}

	@Test
	public void testImplicitFieldIgnoredIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitField" ), AccessMode.METHOD );
		assertNull( property );
	}

	@Test
	public void testExplicitFieldCreatesFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "explicitField" ), AccessMode.FIELD );
		assertProperty( "explicitField", true, false, long.class, property );
	}

	@Test
	public void testExplicitFieldCreatesFieldPropertyIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "explicitField" ), AccessMode.METHOD );
		assertProperty( "explicitField", true, false, long.class, property );
	}

	private void assertProperty(String name, boolean explicit, boolean ignored,
			Class<?> type, Property property) {
		assertEquals( name, property.getName() );
		assertEquals( explicit, property.isExplicit() );
		assertEquals( ignored, property.isIgnored() );
		assertSame( type, property.getType() );

	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitFieldIsStatic()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getField( "explicitStaticField" ), AccessMode.FIELD );
			fail( "Should not have created a property for a static field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "explicitStaticField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "static field" ) );
		}
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitFieldIsStatic()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitStaticField" ), AccessMode.FIELD );
		assertNull( property );
	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitFieldIsFinal()
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
		private static boolean explicitStaticField;

		private static boolean implicitStaticField;

		@com.codiform.moo.annotation.Property
		private final String finalField = "final";

		private int implicitField;
		
		@com.codiform.moo.annotation.Property
		private long explicitField;
	}
}
