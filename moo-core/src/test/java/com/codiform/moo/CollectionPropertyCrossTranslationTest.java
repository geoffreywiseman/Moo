package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Ignore;
import org.junit.Test;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.curry.Translate;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class CollectionPropertyCrossTranslationTest {

	@Test
	@Ignore( "This doesn't work yet. See issue #33: https://github.com/geoffreywiseman/Moo/issues/33" )
	public void testTranslateLinkedHashSetToList() {
		OrdinalSet source = new OrdinalSet( new Ordinal( 2, "second" ),
				new Ordinal( 1, "first" ), new Ordinal( 3, "third" ) );
		OrdinalListDto destination = Translate.to( OrdinalListDto.class ).from(
				source );
		assertEquals( source.getOrdinals().size(), destination.getOrdinals().size() );
	}

	public static class OrdinalSet {
		private Set<Ordinal> ordinals;

		public OrdinalSet(Ordinal... ordinals) {
			this.ordinals = new LinkedHashSet<Ordinal>();
			for( Ordinal item : ordinals )
				this.ordinals.add( item );
		}

		public Set<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class Ordinal implements Comparable<Ordinal> {
		private int rank;
		private String name;

		public Ordinal(int rank, String name) {
			this.rank = rank;
			this.name = name;
		}

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}

		public int compareTo(Ordinal o) {
			return rank - o.rank;
		}
	}

	public static class OrdinalListDto {
		@CollectionProperty(itemClass = OrdinalDto.class)
		private List<OrdinalDto> ordinals;

		public List<OrdinalDto> getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalSetDto {
		@CollectionProperty(itemClass = OrdinalDto.class)
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

		public int compareTo(OrdinalDto other) {
			return rank - other.rank;
		}
	}

	public static class StringsDomain {
		private List<String> strings;

		public StringsDomain(List<String> strings) {
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

	public static class StockPrices {
		private Map<String, Double> prices = new HashMap<String, Double>();

		public void setPrice(String symbol, double price) {
			prices.put( symbol, price );
		}

		public Map<String, Double> getPrices() {
			return prices;
		}

	}

	public static class StockPricesDto {
		private Map<String, Double> prices = new HashMap<String, Double>();

		public Map<String, Double> getPrices() {
			return prices;
		}

	}
}
