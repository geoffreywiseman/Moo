package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.codiform.moo.annotation.MapProperty;
import com.codiform.moo.curry.Translate;

/**
 * Tests for the translation of properties that are maps, including copying
 * and translation of both keys and values.
 */
public class MapTranslationTest {

	@Test
	public void testCopyKeyAndValueCreateEqualMaps() {
		IntStringOrdinals domain = new IntStringOrdinals();
		domain.setName( 1, "first" );
		domain.setName( 2, "second" );
		domain.setName( 3, "third" );
		
		IntStringOrdinalsDto dto = Translate.to( IntStringOrdinalsDto.class ).from( domain );
		assertEquals( 3, dto.ordinals.size() );
		
		assertEquals( "third", dto.getName(3) );
		assertEquals( "second", dto.getName(2) );
		assertEquals( "first", dto.getName(1) );
		
		assertEquals( domain.ordinals, dto.ordinals );
		assertNotSame( domain.ordinals, dto.ordinals );
	}
	
	private static class IntStringOrdinals {
		@MapProperty
		Map<Integer,String> ordinals = new HashMap<Integer,String>();
		
		public void setName( Integer number, String name ) {
			ordinals.put( number, name );
		}
	}
	
	private static class IntStringOrdinalsDto {
		@MapProperty
		Map<Integer,String> ordinals = new HashMap<Integer,String>();
		
		public String getName( Integer number ) {
			return ordinals.get( number );
		}
	}

}
