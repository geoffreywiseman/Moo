package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;

public class ValueTypeTranslationTest {

	@Test
	public void testObjectCanBeTranslatedToString() {
		SubstitutionContainer container = new SubstitutionContainer( new Substitution( "The Cloud", "Your Pocket" ) );
		SubstitutionContainerDto dto = Translate.to( SubstitutionContainerDto.class ).from( container );
		assertEquals( "The Cloud->Your Pocket", dto.getSubstitution() );
	}

	public static class SubstitutionContainer {
		private Substitution substitution;
		public SubstitutionContainer( Substitution substitution ) {
			this.substitution = substitution;
		}
		public Substitution getSubstition() {
			return substitution;
		}
	}
	
	private static class SubstitutionContainerDto {
		@Property(translate=true)
		private String substitution;
		
		public String getSubstitution() {
			return substitution;
		}
	}

	public static class Substitution {
		private String match, replacement;

		private Substitution( String match, String replacement ) {
			this.match = match;
			this.replacement = replacement;
		}

		public String toString() {
			return match + "->" + replacement;
		}
	}
}
