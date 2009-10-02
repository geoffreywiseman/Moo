package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.codiform.moo.model.TestDomain;
import com.codiform.moo.model.TestDto;

public class TranslateTest {

	TestDomain domain = new TestDomain( "Test", 123, 456.789f );

	@Test
	public void testTranslateCreatesDestinationClass() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertNotNull( dto );
	}
	
	@Test
	public void testTranslateCopiesValuesFromSourceToDestination() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertEquals( domain.getString(), dto.getString() );
		assertEquals( domain.getInteger(), dto.getInteger() );
		assertEquals( domain.getFloatingPoint(), dto.getFloatingPoint(), 0.001 );
	}

	@Test
	public void testTranslateUsesTranslationExpressions() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertEquals( domain.getString().length(), dto.getStringLength() );
	}
	
	@Test
	public void testTranslateCopiesCollections() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertNotSame( domain.getValues(), dto.getValues() );
		assertEquals( domain.getValues(), dto.getValues() );
	}
	
	@Test
	public void testTranslateTranslatesProperties() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertNotNull( dto.getSelf() );
		assertEquals( dto.getString(), dto.getSelf().getString() );
		assertEquals( dto.getInteger(), dto.getSelf().getInteger() );
		assertEquals( dto.getFloatingPoint(), dto.getSelf().getFloatingPoint(), 0.001 );
		assertEquals( dto.getValues(), dto.getSelf().getValues() );
	}
	
	@Test
	public void testRecursiveTranslationDoesNotDuplicateTranslations() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertSame( dto, dto.getSelf() );
	}

	@Test
	public void testTranslatesCollectionsOfTranslations() {
		TestDto dto = Translate.to( TestDto.class ).from( domain );
		assertNotNull( dto.getSelves() );
		for( TestDto item : dto.getSelves() ) {
			assertSame( dto, item );
		}
	}
}
