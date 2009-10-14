package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.codiform.moo.model.TestDomain;
import com.codiform.moo.model.TestDto;

public class TranslateTest {

	TestDomain domain = new TestDomain( "Test", 123, 456.789f );

	@Test
	public void testTranslateCreatesDestinationClass() {
		TestDto dto = new Moo().translate( TestDto.class, domain );
		assertNotNull( dto );
	}
	
	@Test
	public void testTranslateUsesTranslationExpressions() {
		TestDto dto = new Moo().translate( TestDto.class, domain );
		assertEquals( domain.getString().length(), dto.getStringLength() );
	}
		
	@Test
	public void testRecursiveTranslationDoesNotDuplicateTranslations() {
		TestDto dto = new Moo().translate( TestDto.class, domain );
		assertSame( dto, dto.getSelf() );
	}

}
