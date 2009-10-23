package com.codiform.moo;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;

public class MissingSourcePropertyTest {

	@Test
	public void testTranslatedPropertyNotFoundInSourceSimplyDoesntPopulate() {
		Destination destination = Translate.to(Destination.class).from(new Object());
		Assert.assertNull( destination.getValue() );
	}

	@Test(expected=TranslationException.class)
	public void testTranslatedPropertyNotFoundInSourceThrowsErrorIfConfiguredToDoSo() {
		Configuration configuration = new Configuration();
		configuration.setSourcePropertiesRequired(true);
		Moo moo = new Moo( configuration );
		Destination destination = moo.translate(Destination.class).from(new Object());
		Assert.assertNull( destination.getValue() );
	}
	
	public static class Destination {
		private String value;

		public String getValue() {
			return value;
		}
	}
	
}
