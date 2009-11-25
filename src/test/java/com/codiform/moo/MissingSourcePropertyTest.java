package com.codiform.moo;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;

public class MissingSourcePropertyTest {

	@Test
	public void testTranslatedPropertyNotFoundInSourceSimplyDoesntPopulate() {
		Destination destination = Translate.to(Destination.class).from(
				new Source("value"));
		Assert.assertEquals("value", destination.getFirstValue());
		Assert.assertNull(destination.getSecondValue());
	}

	@Test(expected = TranslationException.class)
	public void testTranslatedPropertyNotFoundInSourceThrowsErrorIfConfiguredToDoSo() {
		Configuration configuration = new Configuration();
		configuration.setSourcePropertiesRequired(true);
		Moo moo = new Moo(configuration);
		moo.translate(Destination.class).from(
				new Object());
		fail("Should have thrown an exception when it realized there was no secondValue in source.");
	}

	public static class Source {
		private String firstValue;

		public Source(String firstValue) {
			this.firstValue = firstValue;
		}

		public String getFirstValue() {
			return firstValue;
		}

	}

	public static class Destination {
		private String firstValue;
		private String secondValue;

		public String getFirstValue() {
			return firstValue;
		}

		public String getSecondValue() {
			return secondValue;
		}
	}

}
