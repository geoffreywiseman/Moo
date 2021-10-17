package com.codiform.moo.mvel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.mvel2.MVEL;

import static org.junit.Assert.assertEquals;

/**
 * Test for accessing Set elements by index using MVEL.
 * 
 * This doesn't test any Moo code, just MVEL.
 */
public class SetByIndex {

	@Test
	public void testEvalSetByIndex() {
		assertEquals( "A", MVEL.eval( "strings[0]", new ContextObject() ) );
		assertEquals( "B", MVEL.eval( "strings[1]", new ContextObject() ) );
		assertEquals( "C", MVEL.eval( "strings[2]", new ContextObject() ) );
	}
	
	@Ignore( "HashSet cannot be cast to java.lang.Class" )
	@Test
	public void testCompiledSetByIndex() {
		Serializable compiled = MVEL.compileExpression( "strings[0]" );
		assertEquals( "A", MVEL.executeExpression( compiled, new ContextObject() ) );
	}

	public static class ContextObject {
		public Set<String> getStrings() {
			Set<String> set = new HashSet<String>();
			set.add( "A" );
			set.add( "B" );
			set.add( "C" );
			return set;
		}
	}
}
