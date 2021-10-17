package com.codiform.moo.property;

import java.lang.reflect.Field;

import com.codiform.moo.annotation.AccessMode;
import org.junit.Test;

import static org.junit.Assert.*;

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
