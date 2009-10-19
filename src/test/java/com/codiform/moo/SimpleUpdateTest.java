package com.codiform.moo;

import junit.framework.Assert;

import org.junit.Test;

import com.codiform.moo.curry.Update;

public class SimpleUpdateTest {

	@Test
	public void testCanUpdateInstance() {
		Ordinal ordinal = new Ordinal(1, "First");
		Assert.assertEquals(1, ordinal.getRank());
		Assert.assertEquals("First", ordinal.getName());

		OrdinalDto dto = new OrdinalDto(2, "Second");
		Update.from(dto).to(ordinal);
		Assert.assertEquals(2, ordinal.getRank());
		Assert.assertEquals("Second", ordinal.getName());
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

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}
	}
}
