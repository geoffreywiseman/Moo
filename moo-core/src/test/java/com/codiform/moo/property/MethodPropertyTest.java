package com.codiform.moo.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import com.codiform.moo.annotation.AccessMode;

public class MethodPropertyTest {

	@SuppressWarnings( "unused" )
	private void setObjectValue( Integer object ) {
		// ignore value
	}
	
	@SuppressWarnings( "unused" )
	private void setPrimitiveValue( int object ) {
		// ignore value
	}

	@Test
	public void testToString() throws SecurityException, NoSuchMethodException {
		Method method = getClass().getDeclaredMethod( "setObjectValue", new Class[] { Integer.class } );
		Property property = PropertyFactory.createProperty( method, AccessMode.METHOD );
		assertEquals( "MethodProperty<objectValue>", property.toString() );
	}

	@Test
	public void testObjectFieldPropertySupportsNull() throws NoSuchMethodException, SecurityException {
		Method field = getClass().getDeclaredMethod( "setObjectValue", new Class[] { Integer.class } );
		Property property = PropertyFactory.createProperty( field, AccessMode.METHOD );
		assertNotNull( property );
		assertTrue( property.canSupportNull() );
	}
	
	@Test
	public void testPrimitiveFieldPropertyDoesNotSupportNull() throws NoSuchMethodException, SecurityException {
		Method field = getClass().getDeclaredMethod( "setPrimitiveValue", new Class[] { int.class } );
		Property property = PropertyFactory.createProperty( field, AccessMode.METHOD );
		assertNotNull( property );
		assertFalse( property.canSupportNull() );
	}
}
