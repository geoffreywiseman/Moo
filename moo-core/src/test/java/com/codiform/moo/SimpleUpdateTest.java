package com.codiform.moo;

import org.junit.Assert;

import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Update;

/**
 * A test for the simple cases of update.
 * 
 * <p>
 * There are lots of ugly edge cases in update; it might be best to simply
 * suggest that you stay away from update except in simple cases, rather than
 * attempt to handle all the ugly possibilities.
 * <p>
 */
public class SimpleUpdateTest {

	/**
	 * Can update instance values on an instance; this is pure reflection, not
	 * taking Translation into account yet.
	 */
	@Test
	public void testCanUpdateInstanceValues() {
		Ordinal ordinal = new Ordinal( 1, "First" );
		Assert.assertEquals( 1, ordinal.getRank() );
		Assert.assertEquals( "First", ordinal.getName() );

		OrdinalDto dto = new OrdinalDto( 2, "Second" );
		Update.from( dto ).to( ordinal );
		Assert.assertEquals( 2, ordinal.getRank() );
		Assert.assertEquals( "Second", ordinal.getName() );
	}

	/**
	 * Can update instance values on an instance including translation.
	 */
	@Test
	public void testCanUpdateInstanceValuesWithTranslation() {
		Ordinal ordinal = new Ordinal( 1, "First" );
		Assert.assertEquals( 1, ordinal.getRank() );
		Assert.assertEquals( "First", ordinal.getName() );

		Level level = new Level( 2, "Second" );
		Assert.assertEquals( 2, level.getLevel() );
		Assert.assertEquals( "Second", level.getName() );

		Update.from( ordinal ).to( level );
		Assert.assertEquals( 1, level.getLevel() );
		Assert.assertEquals( "First", level.getName() );
	}

	@Test(expected = NoSourceException.class)
	public void testExceptionIfUpdateFromNullObject() {
		Update.from( null ).to( new Ordinal() );
	}

	@Test(expected = NoDestinationException.class)
	public void testExceptionIfUpdateToNullObject() {
		Update.from( new Ordinal( 1, "First" ) ).to( null );
	}

	public static class OrdinalDto {
		private int rank;
		private String name;

		public OrdinalDto(int rank, String name) {
			this.rank = rank;
			this.name = name;
		}

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}
	}

	public static class Ordinal {
		private int rank;
		private String name;

		public Ordinal(int rank, String name) {
			this.rank = rank;
			this.name = name;
		}

		public Ordinal() {
			// TODO Auto-generated constructor stub
		}

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}
	}

	public static class Level {
		@Property(source = "rank")
		private int level;
		private String name;

		public Level(int level, String name) {
			this.level = level;
			this.name = name;
		}

		@SuppressWarnings("unused")
		private Level() {
			// For Translation Only
		}

		public int getLevel() {
			return level;
		}

		public String getName() {
			return name;
		}

	}
}
