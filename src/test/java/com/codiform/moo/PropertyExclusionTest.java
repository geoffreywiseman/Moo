package com.codiform.moo;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.annotation.Translation;
import com.codiform.moo.curry.Translate;

public class PropertyExclusionTest {

	@Test
	public void testDoesNotPopulateStaticFields() {
		Source source = new Source( "No Static" );
		DestinationWithStatic destination = Translate.to(DestinationWithStatic.class).from(source);
		
		Assert.assertEquals( "No Static",  destination.getValue() );
		Assert.assertNull( "Translator should not have populated static field.", DestinationWithStatic.getStaticValue() );
	}

	@Test
	public void testDoesNotPopulateFinalFields() {
		Source source = new Source( "No Static" );
		DestinationWithFinal destination = Translate.to(DestinationWithFinal.class).from(source);
		
		Assert.assertEquals( "No Static",  destination.getValue() );
		Assert.assertNull( "Translator should not have populated final field.", destination.getFinalValue() );

	}

	public static class Source {
		private String value;

		public Source(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	public static class DestinationWithStatic {
		@Translation("value")
		private static String staticValue;
		private String value;
		
		public String getValue() {
			return value;
		}
		
		public static String getStaticValue() {
			return staticValue;
		}
	}

	public static class DestinationWithFinal {
		@Translation("value")
		private final String finalValue = null;
		private String value;
		
		public String getValue() {
			return value;
		}
		
		public String getFinalValue() {
			return finalValue;
		}
	}

}
