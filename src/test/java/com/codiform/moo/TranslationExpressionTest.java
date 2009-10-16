package com.codiform.moo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.annotation.Translation;

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
				OwnerLastNameLength.class, new Task(new User("Jane", "Doe")))
				.size());
		Assert.assertEquals(null, new Moo().translate(
				OwnerLastNameLength.class, new Task(new User("John",null))).size());
		Assert.assertEquals(Integer.valueOf(3), new Moo().translate(
				OwnerLastNameLength.class, new Task(new User("Jane", "Doe")))
				.size());
	}

	@Test
	public void testTranslationCanAccessListMembersByIndex() {
		Assert.assertEquals("The", new Moo().translate(
				StringSetFirstItem.class,
				new StringSet("The", "quick", "brown", "fox")).getFirst());
		Assert.assertEquals("Too", new Moo().translate(
				StringSetFirstItem.class, new StringSet("Too","late","to","think"))
				.getFirst());
		Assert.assertEquals("Twas", new Moo().translate(
				StringSetFirstItem.class, new StringSet("Twas", "brillig"))
				.getFirst());
	}
	
	@Test
	public void testTranslationCanAccessArrayByIndex() {
		UserFirstRole ufr = new Moo().translate( UserFirstRole.class, new User("Geoffrey", "Wiseman", "ADMIN", "NEWBIE") );
		Assert.assertEquals( "ADMIN", ufr.getFirstRole() );
	}
	
	@Test
	public void testTranslationCanAccessSubProperty() {
		Task task = new Task( new User( "Geoffrey", "Wiseman"));
		OwnerLastName oln = new Moo().translate(OwnerLastName.class, task);
		Assert.assertEquals("Wiseman",oln.getOwnerLastName());
	}
	
	@Test
	public void testTranslationCanAccessMapByKey() {
		Task task = new Task(new User("Geoffrey","Wiseman"));
		Date now = new Date();
		task.addAttribute( "createdDate", now);
		TaskCreated tc = new Moo().translate(TaskCreated.class, task);
		Assert.assertEquals( now, tc.getCreatedDate());
	}

	private void assertStringSetSize(int expectedSize, String... strings) {
		StringSet domain = new StringSet(strings);
		StringSetSize size = new Moo().translate(StringSetSize.class, domain);
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

		@Translation("strings.size()")
		private int size;

		public int getSize() {
			return size;
		}
	}

	public static class StringSetFirstItem {

		@Translation("strings[0]")
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
		@Translation("owner.?lastName.length()")
		private Integer size;

		public Integer size() {
			return size;
		}
	}
	
	public static class OwnerLastName {
		@Translation("owner.lastName")
		private String ownerLastName;
		
		public String getOwnerLastName() {
			return ownerLastName;
		}
	}
	
	public static class UserFirstRole {
		@Translation("roles[0]")
		public String firstRole;
		
		public String getFirstRole() {
			return firstRole;
		}
	}
	
	public static class TaskCreated {
		@Translation("attributes['createdDate']")
		private Date createdDate;
		
		public Date getCreatedDate() {
			return createdDate;
		}
	}

}
