package com.codiform.moo;

import com.codiform.moo.domain.Ordinal;
import com.codiform.moo.domain.OrdinalDto;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class ArrayPropertyTranslationTest {

	/**
	 * Defensively copies arrays.
	 */
	@Test
	public void testTranslateCopiesArrays() {
		String[] before = new String[] { "a", "b", "c" };
		StringsDto dto = new Moo().translate(new StringsDomain(
				before), StringsDto.class);
		assertArrayEquals(before, dto.getStrings());
		assertNotSame(before, dto.getStrings());
		assertArrayEquals(before, dto.getStrings());
	}

	/**
	 * Safely copy nulls inside arrays.
	 */
	@Test
	public void testCopiesNullsInsideCollections() {
		String[] before = new String[] { "a", "b", null, "c" };
		StringsDto dto = new Moo().translate(new StringsDomain(
				before), StringsDto.class);
		assertArrayEquals(before, dto.getStrings());
	}

	@Test
	public void testTranslatesArrayContents() {
		Ordinals ordinals = new Ordinals(new Ordinal(1, "first"), new Ordinal(
				2, "second"), new Ordinal(3, "third"));
		OrdinalsDto ordinalsDto = new Moo().translate(ordinals,
				OrdinalsDto.class);
		Assert.assertNotNull(ordinalsDto.getOrdinals());
		Assert.assertEquals(ordinals.getOrdinals().length, ordinalsDto
				.getOrdinals().length);
		for (int index = 0; index < ordinals.getOrdinals().length; index++) {
			Ordinal ordinal = ordinals.getOrdinals()[index];
			OrdinalDto ordinalDto = ordinalsDto.getOrdinals()[index];
			Assert.assertEquals(ordinal.getRank(), ordinalDto.getRank());
			Assert.assertEquals(ordinal.getName(), ordinalDto.getName());
		}
	}

	public static class Ordinals {
		private Ordinal[] ordinals;

		public Ordinals(Ordinal... ordinals) {
			this.ordinals = ordinals;
		}

		public Ordinal[] getOrdinals() {
			return ordinals;
		}
	}

	public static class OrdinalsDto {
		private OrdinalDto[] ordinals;

		public OrdinalDto[] getOrdinals() {
			return ordinals;
		}
	}

	public static class StringsDomain {
		private String[] strings;

		public StringsDomain(String[] strings) {
			this.strings = strings;
		}

		public String[] getStrings() {
			return strings;
		}
	}

	public static class StringsDto {
		private String[] strings;

		public String[] getStrings() {
			return strings;
		}
	}
}
