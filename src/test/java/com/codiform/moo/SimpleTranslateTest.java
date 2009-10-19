package com.codiform.moo;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.codiform.moo.curry.Translate;

public class SimpleTranslateTest {

	@Test
	public void testTranslateCreatesSingleTranslation() {
		StringWrapper source = new StringWrapper("Test");
		StringWrapper destination = Translate.to(StringWrapper.class).from(source);
		Assert.assertNotSame(source, destination);
		Assert.assertEquals(source.getValue(), destination.getValue());
	}
	
	@Test
	public void testTranslateEachCreatesMultipleTranslations() {
		List<StringWrapper> sources = Arrays.asList( new StringWrapper("The"), new StringWrapper("Quick"), new StringWrapper("Brown"), new StringWrapper("Fox"));
		List<StringWrapper> destinations = Translate.to(StringWrapper.class).fromEach(sources);
		Assert.assertEquals( sources.size(), destinations.size() );
		for( int index = 0; index < sources.size(); index++ ) {
			Assert.assertNotSame(sources.get(index), destinations.get(index) );
			Assert.assertEquals(sources.get(index).getValue(), destinations.get(index).getValue());
		}
	}

	public static class StringWrapper {
		private String value;
		
		public StringWrapper() {
		}

		public StringWrapper(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
