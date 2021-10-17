package com.codiform.moo.property.source;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ReflectionSourcePropertyFactoryTest {

	ReflectionSourcePropertyFactory factory = new ReflectionSourcePropertyFactory();
	
	@Test
	public void testReflectionSourcePropertyDoesNotSupportPrefixReflect() {
		assertFalse( factory.supportsPrefix( "reflect" ) );
	}

	@Test
	public void testGetSourcePropertyThrowsUnsupportedOperationExceptionWhenPrefixEmployed() {
		try {
			factory.getSourceProperty( "foo", "foo" );
		} catch( UnsupportedOperationException exception ) {
			assertThat( exception.getMessage(), containsString( "doesn't support prefixes." ) );
		}
	}
	
	@Test
	public void testGetSourcePropertyWithKeywordReturnsNull() {
		assertNull( factory.getSourceProperty( "abstract" ) );
	}
	
	@Test
	public void testGetSourcePropertyWithLiteralReturnsNull() {
		assertNull( factory.getSourceProperty( "true" ) );
	}
	
	@Test
	public void testGetSourcePropertyWithStringNotMatchingJavaIdentifierReturnsNull() {
		assertNull( factory.getSourceProperty( "23skidoo" ) );
	}
	
	@Test
	public void testGetSourcePropertyWithStringMatchingJavaIdentifierReturnsSourceProperty() {
		SourceProperty prop = factory.getSourceProperty( "blackjack21" );
		assertNotNull( prop );
		assertEquals( "blackjack21", prop.getExpression() );
	}
	
}
