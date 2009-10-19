package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class CollectionPropertyTranslationTest {

	/**
	 * Defensively copy collections.
	 */
	@Test
	public void testTranslateCopiesCollections() {
		List<String> before = Arrays.asList("a", "b", "c");
		TestDto dto = new Moo().translate(new StringsDomain(
				before), TestDto.class);
		Assert.assertEquals(before, dto.getStrings());
		assertNotSame(before, dto.getStrings());
		assertEquals(before, dto.getStrings());
	}

	/**
	 * Safely copy nulls inside collections.
	 */
	@Test
	public void testCopiesNullsInsideCollections() {
		List<String> before = Arrays.asList("a", "b", null, "c");
		TestDto dto = new Moo().translate(new StringsDomain(
				before), TestDto.class);
		Assert.assertEquals(before, dto.getStrings());
	}

	@Test
	public void testTranslatesCollectionsOfTranslations() {
		Ordinals ordinals = new Ordinals(new Ordinal(1, "first"), new Ordinal(
				2, "second"), new Ordinal(3, "third"));
		OrdinalsDto ordinalsDto = new Moo().translate(ordinals,
				OrdinalsDto.class);
		Assert.assertNotNull(ordinalsDto.getOrdinals());
		Assert.assertEquals(ordinals.getOrdinals().size(), ordinalsDto
				.getOrdinals().size());
		for (int index = 0; index < ordinals.getOrdinals().size(); index++) {
			Ordinal ordinal = ordinals.getOrdinals().get(index);
			Ordinal ordinalDto = ordinalsDto.getOrdinals().get(index);
			Assert.assertEquals(ordinal.getRank(), ordinalDto.getRank());
			Assert.assertEquals(ordinal.getName(), ordinalDto.getName());
		}
	}

	public static class Ordinals {
		private List<Ordinal> ordinals;

		public Ordinals(Ordinal... ordinals) {
			this.ordinals = Arrays.asList(ordinals);
		}

		public List<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class Ordinal {
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
	}

	public static class OrdinalsDto {
		private List<Ordinal> ordinals;

		public List<Ordinal> getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalDto {
		private int rank;
		private String name;

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
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
}
