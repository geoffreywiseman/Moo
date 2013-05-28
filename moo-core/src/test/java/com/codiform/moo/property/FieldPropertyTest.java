package com.codiform.moo.property;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import com.codiform.moo.annotation.AccessMode;

public class FieldPropertyTest {

	@SuppressWarnings( "unused" )
	private Object field;
	
	@Test
	public void testToString() throws SecurityException, NoSuchFieldException {
		Field field = getClass().getDeclaredField( "field" );
		Property property = PropertyFactory.createProperty( field, AccessMode.FIELD );
		assertEquals( "FieldProperty<field>", property.toString() );
	}

}
