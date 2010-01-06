package com.codiform.moo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.codiform.moo.annotation.Property;

public class InvalidPropertyTest {

	Moo moo = new Moo();

	@Test
	public void testFinalFieldCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					FinalFieldDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains("final field"));
		}
	}

	@Test
	public void testStaticFieldCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					StaticFieldDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains("static field"));
		}
	}

	@Test
	public void testStaticMethodCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					StaticMethodDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains("static method"));
		}
	}

	@Test
	public void testNonSetterMethodCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					NonSetterMethodDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains("'set<Name>' pattern"));
		}
	}

	@Test
	public void testMultiArgumentMethodCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					MultiArgumentMethodDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains(
					"not a single-parameter method"));
		}
	}

	@Test
	public void testNoArgumentMethodCannotBeProperty() {
		try {
			moo.translate(new Source("updatedValue"),
					NoArgumentMethodDestination.class);
		} catch (InvalidPropertyException exception) {
			assertTrue(exception.getMessage().contains(
					"not a single-parameter method"));
		}
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

	public static class FinalFieldDestination {
		@SuppressWarnings("unused")
		@Property
		private final String value = "originalValue";
	}

	public static class StaticFieldDestination {
		@SuppressWarnings("unused")
		@Property
		private static String value = "originalValue";
	}

	public static class StaticMethodDestination {
		@Property
		public static void setValue(String value) {
		}
	}

	public static class NonSetterMethodDestination {
		@Property
		public void applyValue(String value) {
		}
	}

	public static class MultiArgumentMethodDestination {
		@Property
		public void setValue(String firstHalf, String secondHalf) {
		}
	}

	public static class NoArgumentMethodDestination {
		@Property
		public void setValue() {
		}
	}
}
