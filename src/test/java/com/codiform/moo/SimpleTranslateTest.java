package com.codiform.moo;

import junit.framework.Assert;

import org.junit.Test;

public class SimpleTranslateTest {

	@Test
	public void testTranslateCreatesDestinationClass() {
		StringWrapper source = new StringWrapper("Test");
		StringWrapper destination = new Moo().translate(StringWrapper.class,
				source);
		Assert.assertNotSame(source, destination);
		Assert.assertEquals(source.getValue(), destination.getValue());
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
