package com.codiform.moo;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

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
	
	@Ignore("This is new, get it working second.")
	@Test
	public void testPropertyAccess() {
		Source source = new Source("One",1);
		PropertyDestination dest = Translate.to(PropertyDestination.class).from(source);
		assertEquals(source.getString(), dest.getString());
		assertEquals(source.getInteger(), dest.getInteger());
	}
	
	@Ignore
	@Test
	public void testMixedAccess() {
		
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
}
