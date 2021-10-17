package com.codiform.moo;

import java.util.*;

import com.codiform.moo.annotation.CollectionProperty;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class CollectionPropertyTranslationTest {

	/**
	 * Defensively copy collections.
	 */
	@Test
	public void testTranslateCopiesCollections() {
		List<String> before = Arrays.asList( "a", "b", "c" );
		TestDto dto = new Moo().translate( new StringsDomain( before ), TestDto.class );
		Assert.assertEquals( before, dto.getStrings() );
		assertNotSame( before, dto.getStrings() );
		assertEquals( before, dto.getStrings() );
	}

	/**
	 * Use the collection's source when translating collection properties.
	 */
	@Test
	public void testUseCollectionSourceWhenTranslatingCollection() {
		OrdinalList ordinals = new OrdinalList( new Ordinal( 1, "first" ), new Ordinal( 2, "second" ), new Ordinal( 3, "third" ) );
		NumberedListDto ordinalsDto = new Moo().translate( ordinals, NumberedListDto.class );
		Assert.assertNotNull( ordinalsDto.getNumbers() );
		Assert.assertEquals( ordinals.getOrdinals().size(), ordinalsDto.getNumbers().size() );
		for ( int index = 0; index < ordinals.getOrdinals().size(); index++ ) {
			Ordinal ordinal = ordinals.getOrdinals().get( index );
			OrdinalDto ordinalDto = ordinalsDto.getNumbers().get( index );
			Assert.assertEquals( ordinal.getRank(), ordinalDto.getRank() );
			Assert.assertEquals( ordinal.getName(), ordinalDto.getName() );
		}
	}

	/**
	 * Safely copy nulls inside collections.
	 */
	@Test
	public void testCopiesNullsInsideCollections() {
		List<String> before = Arrays.asList( "a", "b", null, "c" );
		TestDto dto = new Moo().translate( new StringsDomain( before ), TestDto.class );
		Assert.assertEquals( before, dto.getStrings() );
	}

	@Test
	public void testTranslatesCollectionsOfTranslations() {
		OrdinalList ordinals = new OrdinalList( new Ordinal( 1, "first" ), new Ordinal( 2, "second" ), new Ordinal( 3, "third" ) );
		OrdinalListDto ordinalsDto = new Moo().translate( ordinals, OrdinalListDto.class );
		Assert.assertNotNull( ordinalsDto.getOrdinals() );
		Assert.assertEquals( ordinals.getOrdinals().size(), ordinalsDto.getOrdinals().size() );
		for ( int index = 0; index < ordinals.getOrdinals().size(); index++ ) {
			Ordinal ordinal = ordinals.getOrdinals().get( index );
			OrdinalDto ordinalDto = ordinalsDto.getOrdinals().get( index );
			Assert.assertEquals( ordinal.getRank(), ordinalDto.getRank() );
			Assert.assertEquals( ordinal.getName(), ordinalDto.getName() );
		}
	}

	public static class OrdinalList {
		private List<Ordinal> ordinals;

		public OrdinalList( Ordinal... ordinals ) {
			this.ordinals = Arrays.asList( ordinals );
		}

		public List<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalSortedSet {
		private SortedSet<Ordinal> ordinals;

		public OrdinalSortedSet( Ordinal... ordinals ) {
			this.ordinals = new TreeSet<>();
			for ( Ordinal item : ordinals )
				this.ordinals.add( item );
		}

		public SortedSet<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalSet {
		private Set<Ordinal> ordinals;

		public OrdinalSet( Ordinal... ordinals ) {
			this.ordinals = new LinkedHashSet<>();
			for ( Ordinal item : ordinals )
				this.ordinals.add( item );
		}

		public Set<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class Ordinal implements Comparable<Ordinal> {
		private int rank;
		private String name;

		public Ordinal( int rank, String name ) {
			this.rank = rank;
			this.name = name;
		}

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}

		public int compareTo( Ordinal o ) {
			return rank - o.rank;
		}
	}

	public static class OrdinalListDto {
		@CollectionProperty( itemClass = OrdinalDto.class )
		private List<OrdinalDto> ordinals;

		public List<OrdinalDto> getOrdinals() {
			return ordinals;
		}
	}

	public static class NumberedListDto {
		@CollectionProperty( source = "ordinals", itemClass = OrdinalDto.class )
		private List<OrdinalDto> numbers;

		public List<OrdinalDto> getNumbers() {
			return numbers;
		}
	}

	public static class OrdinalSetDto {
		@CollectionProperty( itemClass = OrdinalDto.class )
		private SortedSet<OrdinalDto> ordinals;

		public SortedSet<OrdinalDto> getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalDto implements Comparable<OrdinalDto> {
		private int rank;
		private String name;

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}

		public int compareTo( OrdinalDto other ) {
			return rank - other.rank;
		}
	}

	public static class StringsDomain {
		private List<String> strings;

		public StringsDomain( List<String> strings ) {
			this.strings = strings;
		}

		public List<String> getStrings() {
			return strings;
		}
	}

	public static class TestDto {
		private List<String> strings;

		public List<String> getStrings() {
			return strings;
		}
	}

}
