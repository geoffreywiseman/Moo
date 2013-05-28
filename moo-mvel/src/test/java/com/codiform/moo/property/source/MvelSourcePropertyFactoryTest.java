package com.codiform.moo.property.source;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.codiform.moo.MissingSourcePropertyException;

public class MvelSourcePropertyFactoryTest {

	private SourcePropertyFactory factory = new MvelSourcePropertyFactory();
	
	@Test
	public void testGetSourcePropertyForInvalidUnprefixedExpressionReturnsNull() {
		assertNull( factory.getSourceProperty( "this." ) );
	}

	@Test( expected=MissingSourcePropertyException.class )
	public void testGetSourcePropertyForInvalidPrefixedExpression1ThrowsMissingSourcePropertyException() {
		factory.getSourceProperty( "mvel", "this." );
	}

	@Test( expected=MissingSourcePropertyException.class )
	public void testGetSourcePropertyForInvalidPrefixedExpression2ThrowsMissingSourcePropertyException() {
		factory.getSourceProperty( "mvel", "testMethod(" );
	}
}
