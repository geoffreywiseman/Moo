package com.codiform.moo;

import com.codiform.moo.curry.Translate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Check that the presence of moo-mvel doesn't cause regressions for Moo core.
 */
public class RegressionTest {

	/**
	 * Although the separation of moo-mvel from moo-core had been planned, the trigger for making 
	 * it move from planned to in-progress was support for having private source property fields.
	 * 
	 * The presence of moo-mvel should still make it possible to access source property fields.
	 */
	@Test
	public void testPrivateFieldSourcePropertyViaReflection() {
		PrivateFieldSource source = new PrivateFieldSource( "No regression here." );
		Destination destination = Translate.to( Destination.class ).from( source );
		assertEquals( "No regression here.", destination.getValue() );
	}
	
	
	private static class Destination {
		private String value;
		public String getValue() {
			return value;
		}
	}
	
	private static class PrivateFieldSource {
		@SuppressWarnings( "unused" )
		private String value;
		public PrivateFieldSource( String value ) {
			this.value = value;
		}
	}
}
