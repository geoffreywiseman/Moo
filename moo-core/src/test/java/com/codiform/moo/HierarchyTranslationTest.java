package com.codiform.moo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HierarchyTranslationTest {

	@Test
	public void testTranslateIncludesSuperclassFields() {
		AthleteSource source = new AthleteSource("Geoffrey","Wiseman","Cycling");
		AthleteDestination destination = new Moo().translate(source, AthleteDestination.class);
		assertEquals( "Geoffrey", destination.getFirstName());
		assertEquals( "Wiseman", destination.getLastName());
		assertEquals( "Cycling", destination.getSport());
	}

	public static class AthleteDestination extends PersonDestination {
		private String sport;

		public String getSport() {
			return sport;
		}
	}

	public static class PersonDestination {
		private String firstName;
		private String lastName;

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
	}

	public static class AthleteSource extends PersonSource {
		private String sport;

		public AthleteSource(String firstName, String lastName, String sport) {
			super(firstName, lastName);
			this.sport = sport;
		}

		public String getSport() {
			return sport;
		}
	}

	public static class PersonSource {
		private String firstName;
		private String lastName;

		public PersonSource(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
	}
}
