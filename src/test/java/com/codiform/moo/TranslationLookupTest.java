package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;

/**
 * A test showing how a translation/update might invoke a lookup based on
 * information provided in the source rather than using the source directly.
 */
public class TranslationLookupTest {

	private UserDao userDao;

	@Before
	public void setUp() {
		userDao = new UserDao( new User( 38, "Penelope Williams-Ward", "pwilliams", 7 ) );
	}

	@Test
	public void testCanLookupPropertyOnTranslate() {
		PostDto postDto = new PostDto(
				"Saving Money through Extreme Couponing", new AuthorDto( 38,
						"Penelope Williams", 6 ) );
		Post post = Translate.to( Post.class ).withVariable(
				"userDao", userDao ).from( postDto );
		assertEquals( postDto.getTitle(), post.getTitle() );
		assertEquals( Integer.valueOf( 38 ), post.getAuthor().getId() );
		assertEquals( 7, post.getAuthor().getVersion() );
		assertEquals( "pwilliams", post.getAuthor().getUsername() );
	}

	@Test
	@Ignore("TBI")
	public void testCanLookupPropertyOnUpdate() {

	}

	@Test
	@Ignore("TBI")
	public void testCanThrowExceptionOnLookupFailure() {

	}

	public static class PostDto {
		private String title;
		private AuthorDto author;
		
		public PostDto(String title, AuthorDto author) {
			this.title = title;
			this.author = author;
		}

		public String getTitle() {
			return title;
		}
		
		public AuthorDto getAuthor() {
			return author;
		}
	}

	public static class AuthorDto {
		public AuthorDto(Integer id, String displayName, int version) {
			this.id = id;
			this.displayName = displayName;
			this.version = version;
		}

		private Integer id;
		private String displayName;
		private int version;
		
		public Integer getId() {
			return id;
		}
	}

	public static class Post {
		private String title;

		@Property(translation = "userDao.getUser(38)")
		private User author;

		public String getTitle() {
			return title;
		}

		public User getAuthor() {
			return author;
		}
	}

	public static class User {
		private Integer id;
		private String displayName;
		private String username;
		private int version;

		public User(Integer id, String displayName, String username, int version) {
			this.id = id;
			this.displayName = displayName;
			this.username = username;
			this.version = version;
		}

		public String getUsername() {
			return username;
		}

		public int getVersion() {
			return version;
		}

		public Integer getId() {
			return id;
		}
	}

	public static class UserDao {
		Map<Integer, User> usersByKey;

		public UserDao(User... users) {
			usersByKey = new HashMap<Integer, User>();
			for( User item : users ) {
				usersByKey.put( item.getId(), item );
			}
		}

		public User getUser(int id) {
			if( usersByKey.containsKey( id ) ) {
				return usersByKey.get( id );
			} else {
				throw new IllegalArgumentException(
						"Cannot find user correspond to key " + id );
			}
		}
	}

}
