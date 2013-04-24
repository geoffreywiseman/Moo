package com.codiform.moo.translator;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.FieldProperty;
import com.codiform.moo.property.MethodProperty;
import com.codiform.moo.property.Property;

public class TranslatorPropertyResolutionTest {

	@Test
	public void resolvesFieldProperties() {
		Set<Property> properties = getProperties(Alpha.class);
		assertThat(properties, Matchers.hasItem(propertyWithName("alpha")));
	}

	@Test
	public void resolvesMethodProperties() {
		Set<Property> properties = getProperties(Beta.class);
		assertThat(properties, hasItem(propertyWithName("beta")));
	}

	@Test
	public void resolvesFieldPropertiesInSuperclass() {
		Set<Property> properties = getProperties(Charlie.class);
		assertThat(properties, Matchers.hasItem(propertyWithName("alpha")));
		assertThat(properties, Matchers.hasItem(propertyWithName("charlie")));
	}

	@Test
	public void resolvesMethodPropertiesInSuperclass() {
		Set<Property> properties = getProperties(Delta.class);
		assertThat(properties, hasItem(propertyWithName("beta")));
		assertThat(properties, hasItem(propertyWithName("delta")));
	}

	@Test
	public void resolvesFieldAndMethodPropertyByAccessMode() {
		Set<Property> properties = getProperties(Echo.class);
		assertThat(new HashSet<Object>(properties), hasItem(allOf(
				isA(MethodProperty.class), propertyWithName("echo"))));

		properties = getProperties(Foxtrot.class);
		assertThat(new HashSet<Object>(properties), hasItem(allOf(
				isA(FieldProperty.class), propertyWithName("foxtrot"))));
	}

	public void resolvesFieldAndMethodPropertyByExplicitAnnotationOverAccessMode() {
		Set<Property> properties = getProperties(Golf.class);
		assertThat(new HashSet<Object>(properties), hasItem(allOf(
				isA(FieldProperty.class), propertyWithName("golf"))));

		properties = getProperties(Hotel.class);
		assertThat(new HashSet<Object>(properties), hasItem(allOf(
				isA(MethodProperty.class), propertyWithName("hotel"))));
	}

	@Test
	public void resolvesPropertyInFavorOfSubclass() {
		Set<Property> properties = getProperties(India.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(India.class),
				propertyWithName("alpha"))));

		properties = getProperties(Juliet.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(Juliet.class),
				propertyWithName("alpha"))));

		properties = getProperties(Kilo.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(Kilo.class),
				propertyWithName("beta"))));

		properties = getProperties(Lima.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(Lima.class),
				propertyWithName("beta"))));
	}
	
	@Test
	public void prefersExplicitAnnotationToSubclass() {
		Set<Property> properties = getProperties(Mike.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(Golf.class),
				propertyWithName("golf"))));

		properties = getProperties(November.class);
		assertThat(properties, hasItem(allOf(propertyFromClass(Hotel.class),
				propertyWithName("hotel"))));
}

	// resolves overridden property in favor of explicitness (even if in super)

	private Matcher<Property> propertyFromClass(final Class<?> fromClass) {
		return new TypeSafeMatcher<Property>() {
			Class<?> expectedDeclaringClass = fromClass;

			@Override
			public boolean matchesSafely(Property property) {
				return expectedDeclaringClass.equals(property
						.getDeclaringClass());
			}

			public void describeTo(Description description) {
				description.appendText("Property declared in "
						+ expectedDeclaringClass.getSimpleName());
			}
		};
	}

	private <T> Set<Property> getProperties(Class<T> classForProperties) {
		Translator<T> translator = new Translator<T>(classForProperties,
				new Configuration());
		return translator.getProperties(classForProperties);
	}

	private Matcher<Property> propertyWithName(final String propertyName) {
		return new TypeSafeMatcher<Property>() {
			String name = propertyName;

			@Override
			public boolean matchesSafely(Property property) {
				return name.equals(property.getName());
			}

			public void describeTo(Description description) {
				description.appendText("Property named " + name);
			}
		};
	}

	public static class Alpha {
		protected String alpha;
	}

	@Access(AccessMode.METHOD)
	public static class Beta {
		public void setBeta(String beta) {
		}
	}

	public static class Charlie extends Alpha {
		@SuppressWarnings("unused")
		private String charlie;
	}

	@Access(AccessMode.METHOD)
	public static class Delta extends Beta {
		public void setDelta(String delta) {
		}
	}

	@Access(AccessMode.METHOD)
	public static class Echo {
		@SuppressWarnings("unused")
		private String echo;

		public void setEcho(String echo) {
			this.echo = echo;
		}
	}

	@Access(AccessMode.FIELD)
	public static class Foxtrot {
		@SuppressWarnings("unused")
		private String foxtrot;

		public void setFoxtrot(String foxtrot) {
			this.foxtrot = foxtrot;
		}
	}

	@Access(AccessMode.METHOD)
	public static class Golf {
		@com.codiform.moo.annotation.Property
		private String golf;

		public void setGolf(String golf) {
			this.golf = golf;
		}
	}

	@Access(AccessMode.FIELD)
	public static class Hotel {
		@SuppressWarnings("unused")
		private String hotel;

		@com.codiform.moo.annotation.Property
		public void setHotel(String hotel) {
			this.hotel = hotel;
		}
	}

	public static class India extends Alpha {
		@SuppressWarnings("unused")
		private String alpha;
	}

	@Access(AccessMode.METHOD)
	public static class Juliet extends Alpha {
		public void setAlpha(String alpha) {
			this.alpha = alpha;
		}
	}

	public static class Kilo extends Beta {
		@SuppressWarnings("unused")
		private String beta;
	}

	@Access(AccessMode.METHOD)
	public static class Lima extends Beta {
		public void setBeta(String beta) {
			super.setBeta(beta);
		}
	}
	
	@Access(AccessMode.METHOD)
	public static class Mike extends Golf {
		public void setGolf( String golf ) {
			super.setGolf( golf );
		}
	}
	
	@Access(AccessMode.FIELD)
	public static class November extends Hotel {
		@SuppressWarnings("unused")
		private String hotel;
	}
}
