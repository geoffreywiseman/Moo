package com.codiform.moo;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Update;
import org.junit.Test;

import static org.junit.Assert.*;

public class NestedUpdateTest {

	@Test
	public void testUpdateModifiesButDoesNotReplaceClassIfUpdateFlagSet() {
		OuterSource source = new OuterSource( new InnerSource( "Source" ) );

		InnerDestination dinner = new InnerDestination( "Dest" );
		UpdateDestination destination = new UpdateDestination( dinner );

		Update.from( source ).to( destination );

		assertSame( dinner, destination.getInner() );
		assertEquals( "Source", destination.getInner().getName() );
	}

	@Test
	public void testUpdateReplacesPreExistingClassWithTranslatedClassIfUpdateFlagNotSet() {
		OuterSource source = new OuterSource( new InnerSource( "Source" ) );

		InnerDestination dinner = new InnerDestination( "Dest" );
		ReplaceDestination destination = new ReplaceDestination( dinner );

		Update.from( source ).to( destination );

		assertNotSame( dinner, destination.getInner() );
		assertSame( InnerDestination.class, destination.getInner().getClass() );
		assertEquals( "Source", destination.getInner().getName() );
	}

	public static class OuterSource {
		private InnerSource inner;

		public OuterSource(InnerSource inner) {
			this.inner = inner;
		}

		public InnerSource getInner() {
			return inner;
		}
	}

	public static class InnerSource {
		private String name;

		public InnerSource(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public static class UpdateDestination {
		
		@Property(update=true)
		private InnerDestination inner;

		protected UpdateDestination() {
			// just for translation
		}

		public UpdateDestination(InnerDestination inner) {
			this.inner = inner;
		}

		public InnerDestination getInner() {
			return inner;
		}
	}

	public static class ReplaceDestination {
		
		@Property(translate=true)
		private InnerDestination inner;

		protected ReplaceDestination() {
			// just for translation
		}

		public ReplaceDestination(InnerDestination inner) {
			this.inner = inner;
		}

		public InnerDestination getInner() {
			return inner;
		}
	}

	public static class InnerDestination {
		private String name;

		protected InnerDestination() {
			// just for translation
		}
		
		public InnerDestination(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
