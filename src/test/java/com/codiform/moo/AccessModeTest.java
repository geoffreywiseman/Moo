package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.codiform.moo.annotation.Property;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.curry.Translate;

public class AccessModeTest {

	@Test
	public void testFieldAccess() {
		Source source = new Source("One",1);
		FieldDestination dest = Translate.to(FieldDestination.class).from(source);
		assertEquals(source.getString(), dest.getStringValue());
		assertEquals(source.getInteger(), dest.getIntegerValue());
	}
	
	@Test
	public void testPropertyAccess() {
		Source source = new Source("One",1);
		PropertyDestination dest = Translate.to(PropertyDestination.class).from(source);
		assertEquals(source.getString(), dest.getString());
		assertEquals(source.getInteger(), dest.getInteger());
	}
	
	@Test
	public void testMixedAccess() {
		Source source = new Source("One",1);
		MixedDestination dest = Translate.to(MixedDestination.class).from(source);
		assertEquals(source.getString(), dest.getString());
		assertEquals(source.getInteger(), dest.getInteger());
	}

	public static class Source {
		private String string;
		private Integer integer;
		
		public Source( String string, Integer integer ) {
			this.string = string;
			this.integer = integer;
		}
		
		public String getString() {
			return string;
		}
		
		public Integer getInteger() {
			return integer;
		}
	}

	@Access(AccessMode.FIELD)
	public static class FieldDestination {
		private String string;
		private Integer integer;
		
		public String getStringValue() {
			return string;
		}
		
		public Integer getIntegerValue() {
			return integer;
		}
	}

	@Access(AccessMode.METHOD)
	public static class PropertyDestination {
		private String stringValue;
		private Integer integerValue;
		
		public String getString() {
			return stringValue;
		}
		
		public Integer getInteger() {
			return integerValue;
		}
		
		public void setString( String string ) {
			this.stringValue = string;
		}
		
		public void setInteger( Integer integer ) {
			this.integerValue = integer;
		}
	}

	public static class MixedDestination {
		private String string;
		private Integer integerValue;
		
		public String getString() {
			return string;
		}
		
		public Integer getInteger() {
			return integerValue;
		}
		
		@Property
		public void setInteger( Integer integer ) {
			this.integerValue = integer;
		}
	}
}
