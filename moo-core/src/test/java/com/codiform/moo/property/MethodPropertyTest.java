package com.codiform.moo.property;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

import com.codiform.moo.annotation.AccessMode;

public class MethodPropertyTest {

	@SuppressWarnings( "unused" )
	private void setValue( Object object ) {
		// ignore value
	}
	
	@Test
	public void testToString() throws SecurityException, NoSuchMethodException {
		Method method = getClass().getDeclaredMethod( "setValue", new Class[] { Object.class } );
		Property property = PropertyFactory.createProperty( method, AccessMode.METHOD );
		assertEquals( "MethodProperty<value>", property.toString() );
	}

}
