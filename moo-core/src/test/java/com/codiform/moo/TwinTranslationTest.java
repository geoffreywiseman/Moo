package com.codiform.moo;

import com.codiform.moo.annotation.Property;
import org.junit.Assert;
import org.junit.Test;

/**
 * Make sure that translating the same domain object to two different client-side representations
 * doesn't cause Moo to blow a gasket.
 */
public class TwinTranslationTest {

	@Test
	public void testTwinTranslation() {
		Person person = new Person( "Geoffrey Wiseman", "Toronto, ON" );
		PersonViews views = new Moo().translate( PersonViews.class ).from( person );
		Assert.assertEquals( person.getName(), views.getSummary().getName() );
		Assert.assertEquals( person.getName(), views.getDetails().getName() );
		Assert.assertEquals( person.getLocation(), views.getDetails().getLocation() );
		Assert.assertEquals( views.getSummary().getDetails(), views.getDetails() );
		Assert.assertEquals( views.getDetails().getSummary(), views.getSummary() );
	}

	public static class Person {
		private String name;
		private String location;

		public Person( String name, String location ) {
			this.name = name;
			this.location = location;
		}

		public String getName() {
			return name;
		}

		public String getLocation() {
			return location;
		}

		public Person getSelf() {
			return this;
		}
	}

	public static class PersonViews {

		@Property( source = "self", translate = true )
		private PersonDetails details;

		@Property( source = "self", translate = true )
		private PersonSummary summary;

		public PersonDetails getDetails() {
			return details;
		}

		public PersonSummary getSummary() {
			return summary;
		}

	}

	public static class PersonSummary {

		private String name;

		@Property( source = "self", translate = true )
		private PersonDetails details;

		public String getName() {
			return name;
		}

		public PersonDetails getDetails() {
			return details;
		}
	}

	public static class PersonDetails {

		@Property( source = "self", translate = true )
		private PersonSummary summary;

		private String name;

		private String location;

		public String getName() {
			return name;
		}

		public String getLocation() {
			return location;
		}

		public PersonSummary getSummary() {
			return summary;
		}
	}
}
