package com.codiform.moo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.annotation.Property;

public class TranslationExpressionTest {

	@Test
	public void testTranslationCanInvokeMethod() {
		assertStringSetSize(1, "foo");
		assertStringSetSize(2, "first", "second");
		assertStringSetSize(3, "alpha", "beta", "gamma");
		assertStringSetSize(4, "one", "two", "three", "four");
		assertStringSetSize(5, "genesis", "exodus", "leviticus", "numbers",
				"deutoronomy");
	}

	@Test
	public void testTranslationCanPerformNullsafeTraversal() {
		Assert.assertEquals(Integer.valueOf(3), new Moo().translate(
				new Task(new User("Jane", "Doe")), OwnerLastNameLength.class)
				.size());
		Assert.assertEquals(null, new Moo().translate(
				new Task(new User("John",null)), OwnerLastNameLength.class).size());
		Assert.assertEquals(Integer.valueOf(3), new Moo().translate(
				new Task(new User("Jane", "Doe")), OwnerLastNameLength.class)
				.size());
	}

	@Test
	public void testTranslationCanAccessListMembersByIndex() {
		Assert.assertEquals("The", new Moo().translate(
				new StringSet("The", "quick", "brown", "fox"),
				StringSetFirstItem.class).getFirst());
		Assert.assertEquals("Too", new Moo().translate(
				new StringSet("Too","late","to","think"), StringSetFirstItem.class)
				.getFirst());
		Assert.assertEquals("Twas", new Moo().translate(
				new StringSet("Twas", "brillig"), StringSetFirstItem.class)
				.getFirst());
	}
	
	@Test
	public void testTranslationCanAccessArrayByIndex() {
		UserFirstRole ufr = new Moo().translate( new User("Geoffrey", "Wiseman", "ADMIN", "NEWBIE"), UserFirstRole.class );
		Assert.assertEquals( "ADMIN", ufr.getFirstRole() );
	}
	
	@Test
	public void testTranslationCanAccessSubProperty() {
		Task task = new Task( new User( "Geoffrey", "Wiseman"));
		OwnerLastName oln = new Moo().translate(task, OwnerLastName.class);
		Assert.assertEquals("Wiseman",oln.getOwnerLastName());
	}
	
	@Test
	public void testTranslationCanAccessMapByKey() {
		Task task = new Task(new User("Geoffrey","Wiseman"));
		Date now = new Date();
		task.addAttribute( "createdDate", now);
		TaskCreated tc = new Moo().translate(task, TaskCreated.class);
		Assert.assertEquals( now, tc.getCreatedDate());
	}

	private void assertStringSetSize(int expectedSize, String... strings) {
		StringSet domain = new StringSet(strings);
		StringSetSize size = new Moo().translate(domain, StringSetSize.class);
		Assert.assertEquals(expectedSize, size.getSize());
	}

	public static class StringSet {
		private SortedSet<String> strings;

		public StringSet(String... strings) {
			this.strings = new TreeSet<String>();
			for (String item : strings) {
				this.strings.add(item);
			}
		}

		public Set<String> getStrings() {
			return strings;
		}
	}

	public static class StringSetSize {

		@Property(translation="strings.size()")
		private int size;

		public int getSize() {
			return size;
		}
	}

	public static class StringSetFirstItem {

		@Property(translation="strings[0]")
		private String first;

		public String getFirst() {
			return first;
		}
	}

	public static class Task {
		private User owner;
		private Map<String,Object> attributes;

		public Task(User owner) {
			this.owner = owner;
			this.attributes = new HashMap<String,Object>();
		}
		
		public void addAttribute( String key, Object value ) {
			attributes.put( key, value );
		}

		public User getOwner() {
			return owner;
		}
		
		public Map<String,Object> getAttributes() {
			return attributes;
		}
	}

	public static class User {
		private String firstName;
		private String lastName;
		private String roles[];

		public User(String firstName, String lastName, String... roles) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.roles = roles;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
		
		public String[] getRoles() {
			return roles;
		}
	}

	public static class OwnerLastNameLength {
		@Property(translation="owner.?lastName.length()")
		private Integer size;

		public Integer size() {
			return size;
		}
	}
	
	public static class OwnerLastName {
		@Property(translation="owner.lastName")
		private String ownerLastName;
		
		public String getOwnerLastName() {
			return ownerLastName;
		}
	}
	
	public static class UserFirstRole {
		@Property(translation="roles[0]")
		public String firstRole;
		
		public String getFirstRole() {
			return firstRole;
		}
	}
	
	public static class TaskCreated {
		@Property(translation="attributes['createdDate']")
		private Date createdDate;
		
		public Date getCreatedDate() {
			return createdDate;
		}
	}

}
