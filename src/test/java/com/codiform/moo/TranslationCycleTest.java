package com.codiform.moo;

import org.junit.Assert;
import org.junit.Test;

import com.codiform.moo.annotation.Property;

public class TranslationCycleTest {

	@Test
	public void testRecursiveTranslationAvoidsDuplication() {
		SourceLoopItem source = new SourceLoopItem();
		source.close();
		Assert.assertSame(source, source.getSibling());

		DestinationLoopItem destination = new Moo().translate(
				source, DestinationLoopItem.class);
		Assert.assertSame(destination, destination.getSibling());
	}

	@Test
	public void testTranslationCycleAvoidsDuplication() {
		SourceLoopItem source = new SourceLoopItem(new SourceLoopItem());
		source.close();
		Assert.assertNotSame(source, source.getSibling());
		Assert.assertSame(source, source.getSibling().getSibling());

		DestinationLoopItem destination = new Moo().translate(
				source, DestinationLoopItem.class);
		Assert.assertNotNull( destination.getSibling() );
		Assert.assertNotSame(destination, destination.getSibling());
		Assert.assertSame(destination, destination.getSibling().getSibling());

	}

	private static class SourceLoopItem {
		private SourceLoopItem sibling;

		public SourceLoopItem() {
			// tail node
		}

		public SourceLoopItem(SourceLoopItem sibling) {
			this.sibling = sibling;
		}

		public void close() {
			SourceLoopItem current = this;
			while (current.sibling != null)
				current = current.sibling;
			current.sibling = this;
		}
		
		public SourceLoopItem getSibling() {
			return sibling;
		}
	}

	public static class DestinationLoopItem {

		@Property(translate=true)
		private DestinationLoopItem sibling;

		public DestinationLoopItem() {
			// tail node
		}

		public DestinationLoopItem getSibling() {
			return sibling;
		}
	}
}
