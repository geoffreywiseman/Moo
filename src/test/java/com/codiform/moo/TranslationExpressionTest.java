package com.codiform.moo;

import java.util.HashSet;
import java.util.Set;

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
	
	//nullsafe
	//array access
	//list access
	//subproperty access
	//map access

	private void assertStringSetSize(int expectedSize, String... strings) {
		StringSet domain = new StringSet(strings);
		StringSetSize size = new Moo().translate(StringSetSize.class,domain);
		Assert.assertEquals(expectedSize, size.getSize());
	}

	public static class StringSet {
		private Set<String> strings;

		public StringSet(String... strings) {
			this.strings = new HashSet<String>();
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

}
