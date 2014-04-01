package com.codiform.moo.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;

import org.junit.Test;

import com.codiform.moo.annotation.AccessMode;

public class FieldPropertyTest {

	@SuppressWarnings( "unused" )
	private Integer objectField;
	
	@SuppressWarnings( "unused" )
	private int primitiveField;
	
	@Test
	public void testToString() throws SecurityException, NoSuchFieldException {
		Field field = getClass().getDeclaredField( "objectField" );
		Property property = PropertyFactory.createProperty( field, AccessMode.FIELD );
		assertEquals( "FieldProperty<objectField>", property.toString() );
	}
	
	@Test
	public void testObjectFieldPropertySupportsNull() throws NoSuchFieldException, SecurityException {
		Field field = getClass().getDeclaredField( "objectField" );
		Property property = PropertyFactory.createProperty( field, AccessMode.FIELD );
		assertTrue( property.canSupportNull() );
	}
	
	@Test
	public void testPrimitiveFieldPropertyDoesNotSupportNull() throws NoSuchFieldException, SecurityException {
		Field field = getClass().getDeclaredField( "primitiveField" );
		Property property = PropertyFactory.createProperty( field, AccessMode.FIELD );
		assertFalse( property.canSupportNull() );
	}

}
