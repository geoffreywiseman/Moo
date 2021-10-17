package com.codiform.moo;

import com.codiform.moo.curry.Translate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TranslationConstructionTest {

	@Test
	public void testCanTranslateToClassesWithProtectedNoArgConstructor() {
		Source source = new Source( "Translate to Class with Protected Constructor" );
		ProtectedDestination destination = Translate.to( ProtectedDestination.class ).from( source );
		assertEquals( "Translate to Class with Protected Constructor", destination.getName() );
	}

	@Test
	public void testCanTranslateToClassesWithPrivateNoArgConstructor() {
		Source source = new Source( "Translate to Class with Private Constructor" );
		PrivateDestination destination = Translate.to( PrivateDestination.class ).from( source );
		assertEquals( "Translate to Class with Private Constructor", destination.getName() );
	}
	
	public static class Source {
		private String name;
		
		public Source( String name ) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public static class ProtectedDestination {
		private String name;
		
		protected ProtectedDestination() {
			// for Moo
		}
		
		public String getName() {
			return name;
		}
	}

	public static class PrivateDestination {
		private String name;
		
		private PrivateDestination() {
			// for Moo
		}
		
		public String getName() {
			return name;
		}
	}

}
