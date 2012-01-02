package com.codiform.moo.translator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Property;

public class MethodPropertyTest {

	@Test
	public void testMethodProperty() throws SecurityException,
			NoSuchMethodException {
		try {
		MethodProperty prop = new MethodProperty(
				MethodPropertyContainer.class.getMethod( "storeName",
						String.class ) );
		assertFalse( prop.isProperty( AccessMode.METHOD ) );
		} catch( InvalidPropertyException exception ) {
			assertTrue( "Exception should reference the 'storeName' method.", exception.getMessage().startsWith( "Method storeName" ) );
		}
	}

	public static class MethodPropertyContainer {

		@SuppressWarnings("unused")
		private String name;

		@Property
		public void storeName(String name) {
			this.name = name;
		}
	}

}
