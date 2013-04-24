package com.codiform.moo;

import org.junit.Assert;
import org.junit.Test;
import org.mvel2.MVEL;

public class MvelNullSafetyTest {

	@Test
	public void testMvelExpression() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", "Zappa", null ) );
		Assert.assertEquals( "Frank", MVEL.eval( "parent.firstName", person ) );
	}

	@Test
	public void testMvelNullSafeInitialExpressionWithNull() {
		Person person = new Person( "Moonunit", "Zappa", null );
		Assert.assertNull( MVEL.eval( "?parent.firstName", person ) );
	}

	@Test
	public void testMvelNullSafeInitialExpressionWithoutNull() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", "Zappa", null ) );
		Assert.assertEquals( "Frank", MVEL.eval( "?parent.firstName", person ) );
	}

	@Test
	public void testMvelNullSafeSubExpressionWithNull() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", "Zappa", null ) );
		Assert.assertEquals( null, MVEL.eval( "parent.?parent.firstName", person ) );
	}

	@Test
	public void testMvelNullSafeSubExpressionWithoutNull() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", "Zappa", new Person( "Francis", "Zappa", null ) ) );
		Assert.assertEquals( "Francis", MVEL.eval( "parent.?parent.firstName", person ) );
	}

	@Test
	public void testMvelNullSafeSubExpressionAndMethodWithNull() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", null, null ) );
		Assert.assertEquals( null, MVEL.eval( "parent.?lastName.length()", person ) );
	}

	@Test
	public void testMvelNullSafeSubExpressionAndMethodWithNull2() {
		Person person = new Person( "Moonunit", "Zappa", null );
		Assert.assertEquals( null, MVEL.eval( "?parent.?lastName.length()", person ) );
	}

	@Test
	public void testMvelNullSafeSubExpressionAndMethodWithoutNull() {
		Person person = new Person( "Moonunit", "Zappa", new Person( "Frank", "Zappa", null ) );
		Assert.assertEquals( 5, MVEL.eval( "parent.?lastName.length()", person ) );
	}

	public static class Person {
		private String firstName, lastName;
		private Person parent;

		public Person( String firstName, String lastName, Person parent ) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.parent = parent;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public Person getParent() {
			return parent;
		}
	}

}
