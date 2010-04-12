package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.curry.Update;

public class VariablesTest {

	@Test
	public void testCanUseVariableDataToTranslate() {
		DestinationWithScore scored = Translate.to(DestinationWithScore.class)
				.withVariable("score", 12).from(
						new Source("variableDataTranslate"));
		assertEquals("variableDataTranslate", scored.getValue());
		assertEquals(12, scored.getScore());
	}

	@Test
	public void testCanUseVariableMethodToTranslate() {
		DestinationWithCount counted = Translate.to(DestinationWithCount.class)
				.withVariable("counter", new VowelCounter()).from(
						new Source("variableMethodTranslate"));
		assertEquals("variableMethodTranslate", counted.getValue());
		assertEquals(9, counted.getCount());
	}

	@Test
	public void testCanUseVariableDataToUpdate() {
		DestinationWithScore scored = new DestinationWithScore("original", 1);
		Source source = new Source("updated");
		Update.from(source).withVariable("score", 2).to(scored);
		assertEquals("updated", scored.getValue());
		assertEquals(2, scored.getScore());
	}

	@Test
	public void testCanUseVariableMethodToUpdate() {
		DestinationWithCount scored = new DestinationWithCount("original", 4);
		Source source = new Source("updated");
		Update.from(source).withVariable("counter", new VowelCounter()).to(
				scored);
		assertEquals("updated", scored.getValue());
		assertEquals(3, scored.getCount());

	}

	public static class Source {
		private String value;

		public Source(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static class DestinationWithScore {
		private String value;

		@Property(translation="score")
		private int score;

		public DestinationWithScore() {

		}

		public DestinationWithScore(String value, int score) {
			this.value = value;
			this.score = score;
		}

		public String getValue() {
			return value;
		}

		public int getScore() {
			return score;
		}
	}

	public static class DestinationWithCount {
		private String value;

		@Property(translation="counter.getCount(value)")
		private int count;

		public DestinationWithCount() {

		}

		public DestinationWithCount(String value, int count) {
			this.value = value;
			this.count = count;
		}

		public String getValue() {
			return value;
		}

		public int getCount() {
			return count;
		}
	}

	public static class VowelCounter {
		public int getCount(String value) {
			int count = 0;
			for (char item : value.toCharArray()) {
				switch (item) {
				case 'a':
				case 'e':
				case 'i':
				case 'o':
				case 'u':
				case 'y':
				case 'A':
				case 'E':
				case 'I':
				case 'O':
				case 'U':
				case 'Y':
					count++;
				}
			}
			return count;
		}
	}

}
