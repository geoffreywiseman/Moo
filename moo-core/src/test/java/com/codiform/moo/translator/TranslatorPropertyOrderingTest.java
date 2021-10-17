package com.codiform.moo.translator;

import java.util.List;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.property.Property;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Make sure the properties are ordered from superclass to subclass.
 */
public class TranslatorPropertyOrderingTest extends TestWithTranslatorFactory {

	@Test
	public void testPropertiesOrderedSuperToSub() {
		List<Property> properties = getTranslator( Bottom.class ).getProperties( Bottom.class );
		assertEquals( 3, properties.size() );
		assertEquals( "foo", properties.get( 0 ).getName() );
		assertEquals( "bar", properties.get( 1 ).getName() );
		assertEquals( "baz", properties.get( 2 ).getName() );
	}
	
	@Access(AccessMode.METHOD)
	private static class Top {
		@SuppressWarnings( "unused" )
		public void setFoo( String value ) {
		}
	}
	
	@Access(AccessMode.METHOD)
	private static class Middle extends Top {
		@SuppressWarnings( "unused" )
		public void setBar( String value ) {
		}
	}
	
	@Access(AccessMode.METHOD)
	private static class Bottom extends Middle {
		@SuppressWarnings( "unused" )
		public void setBaz( String value ) {
		}
	}
}
