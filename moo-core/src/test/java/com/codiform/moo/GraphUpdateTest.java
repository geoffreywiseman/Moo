package com.codiform.moo;

import com.codiform.moo.curry.Update;
import org.junit.Assert;
import org.junit.Test;

/**
 * A test for the simple cases of update.
 * 
 * <p>
 * There are lots of ugly edge cases in update; it might be best to simply
 * suggest that you stay away from update except in simple cases, rather than
 * attempt to handle all the ugly possibilities.
 * <p>
 */
public class GraphUpdateTest {

	@Test
	public void testPropertyValuesAreDirectlyReplaced() {
		Child deb = new Child( "Debrah Morgan" );
		Child dex = new Child( "Dexter Morgan" );
		Parent parent = new Parent( "Harry Morgan", deb );
		ParentDto parentDto = new ParentDto( "Harry Morgan", dex );
		Update.from(parentDto).to(parent);
		
		// string interning
		Assert.assertSame( "Harry Morgan", parent.getName() );

		Assert.assertNotSame( deb, parent.getChild() );
		Assert.assertSame( dex, parent.getChild() );
	}

	public static class Parent {
		private String name;
		private Child child;

		public Parent(String name, Child child) {
			this.name = name;
			this.child = child;
		}

		public String getName() {
			return name;
		}

		public Child getChild() {
			return child;
		}
	}

	public static class ParentDto {
		private String name;

		private Child child;

		public ParentDto() {
			this(null, null);
		}

		public ParentDto(String name, Child child) {
			this.name = name;
			this.child = child;
		}

		public String getName() {
			return name;
		}

		public Child getChild() {
			return child;
		}
	}

	public static class Child {
		private String name;

		public Child(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
