package com.codiform.moo;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.curry.Translate;

public class PropertyExclusionTest {

	@Test
	public void testDoesNotPopulateStaticFields() {
		Source source = new Source( "Alpha", "No Static" );
		DestinationWithStatic destination = Translate.to(DestinationWithStatic.class).from(source);
		
		Assert.assertEquals( "Alpha",  destination.getAlpha() );
		Assert.assertNull( "Translator should not have populated static field.", DestinationWithStatic.getBeta() );
	}

	@Test
	public void testDoesNotPopulateFinalFields() {
		Source source = new Source( "Alpha", "No Static" );
		DestinationWithFinal destination = Translate.to(DestinationWithFinal.class).from(source);
		
		Assert.assertEquals( "Alpha",  destination.getAlpha() );
		Assert.assertNull( "Translator should not have populated final field.", destination.getBeta() );

	}

	public static class Source {
		private String alpha;
		private String beta;

		public Source(String alpha, String beta) {
			this.alpha = alpha;
			this.beta = beta;
		}

		public String getAlpha() {
			return alpha;
		}
		
		public String getBeta() {
			return beta;
		}
	}
	
	public static class DestinationWithStatic {
		private static String beta;
		private String alpha;
		
		public String getAlpha() {
			return alpha;
		}
		
		public static String getBeta() {
			return beta;
		}
	}

	public static class DestinationWithFinal {
		private final String beta = null;
		private String alpha;
		
		public String getAlpha() {
			return alpha;
		}
		
		public String getBeta() {
			return beta;
		}
	}

}
