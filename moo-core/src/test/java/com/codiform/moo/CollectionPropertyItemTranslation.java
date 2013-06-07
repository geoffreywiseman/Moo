package com.codiform.moo;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.domain.LicenseType;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class CollectionPropertyItemTranslation {

	@Test
	public void testTranslateEnumSetIntoStringSetWithoutExpression() {
		LicenseClassDto dto = Translate.to( LicenseClassDto.class ).from( LicenseType.E );
		assertEquals( "E", dto.getName() );
		assertEquals( 2, dto.getIncludedClasses().size() );
		assertThat( dto.getIncludedClasses(), contains( "F", "G" ) );
	}

	/**
	 * Testing translation by item class alone.
	 */
	private static class LicenseClassDto {
		
		@Property
		private String name;
		
		@CollectionProperty( source="includedTypes", itemClass = String.class )
		Set<String> includedClasses;
		
		public String getName() {
			return name;
		}
		
		public Set<String> getIncludedClasses() {
			return includedClasses;
		}
	}

	@Test
	public void testTranslateEnumSetIntoStringSetWithExpression() {
		LicenseDescriptionDto dto = Translate.to( LicenseDescriptionDto.class ).from( LicenseType.E );
		assertEquals( "Class E", dto.getDescription() );
		assertEquals( 2, dto.getIncludedDescriptions().size() );
		assertThat( dto.getIncludedDescriptions(), contains( "Class F", "Class G" ) );
	}

	/**
	 * Testing translation by item class alone.
	 */
	private static class LicenseDescriptionDto {
		
		@Property
		private String description;
		
		@CollectionProperty( source="includedTypes", itemClass = String.class, itemExpression="description" )
		Set<String> includedDescriptions;
		
		public String getDescription() {
			return description;
		}
		
		public Set<String> getIncludedDescriptions() {
			return includedDescriptions;
		}
	}
}
