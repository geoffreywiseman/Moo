package com.codiform.moo;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.curry.Translate;

public class MismatchedTranslationTest {

	/**
	 * Should fail, but should fail with an error message that makes the
	 * solution somewhat more obvious.
	 */
	@Test
	public void testTranslationOfArrayToStringFails() {
		try {
			ArrayValue source = new ArrayValue("one", "two");
			Translate.to(StringValue.class).from(source);
			Assert.fail("Expected a translation exception which didn't occur.");
		} catch (TranslationException exception) {
			String expectedMessage = "Cannot translate from source array type class java.lang.String[] to destination type java.lang.String";
			Assert.assertEquals(expectedMessage, exception.getMessage());
		}
	}

	/**
	 * Results in a Translation Exception containing an Illegal Argument
	 * Exception.
	 */
	@Test()
	public void testTranslationOfListToStringFails() {
		try {
			ListValue source = new ListValue("one", "two");
			Translate.to(StringValue.class).from(source);
		} catch (TranslationException exception) {
			Assert.assertNotNull(exception.getCause());
			Assert.assertEquals(IllegalArgumentException.class, exception
					.getCause().getClass());
		}
	}

	/**
	 * Results in a Translation Exception containing an Illegal Argument
	 * Exception.
	 */
	@Test()
	public void testTranslationOfIntegerToStringFails() {
		try {
			IntegerValue source = new IntegerValue(1);
			Translate.to(StringValue.class).from(source);
		} catch (TranslationException exception) {
			Assert.assertNotNull(exception.getCause());
			Assert.assertEquals(IllegalArgumentException.class, exception
					.getCause().getClass());
		}
	}

	public static class ArrayValue {
		private String[] value;

		public ArrayValue(String... value) {
			this.value = value;
		}

		public String[] getValue() {
			return value;
		}
	}

	public static class ListValue {
		private List<String> value;

		public ListValue(String... value) {
			this.value = Arrays.asList(value);
		}

		public List<String> getValue() {
			return value;
		}
	}

	public static class StringValue {
		private String value;

		public String getValue() {
			return value;
		}
	}

	public static class IntegerValue {
		private Integer value;

		public IntegerValue(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

}
