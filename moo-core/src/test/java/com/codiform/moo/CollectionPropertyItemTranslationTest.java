package com.codiform.moo;

import java.util.HashSet;
import java.util.Set;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.domain.LicenseType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing how properties that contain collection classes are translated.
 */
public class CollectionPropertyItemTranslationTest {

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
		assertThat( dto.getIncludedDescriptions(), containsInAnyOrder( "Class F", "Class G" ) );
	}

	/**
	 * Testing translation by item class and item expression.
	 */
	private static class LicenseDescriptionDto {
		
		@Property
		private String description;
		
		@CollectionProperty( source="includedTypes", itemClass = String.class, itemSource="description" )
		Set<String> includedDescriptions;
		
		public String getDescription() {
			return description;
		}
		
		public Set<String> getIncludedDescriptions() {
			return includedDescriptions;
		}
	}
	
	@Test
	public void testTranslateWithExpression() {
		Classroom cr = new Classroom( "Kindergarden", 
			new Person( "Lil Joey", new Person( "Joe Smith" ), new Person( "Mary Smith" ) ),
			new Person( "Kimmy Nakomoto", new Person( "Satoshi Nakamoto" ), new Person( "Kirin Nakamoto" ) ) );
		ClassFathers cf = Translate.to( ClassFathers.class ).from( cr );
		assertEquals( cr.getClassName(), cf.getClassName() );
		assertEquals( 2, cf.size() );
		assertTrue( cf.contains( new Person( "Joe Smith" ) ) );
		assertTrue( cf.contains( new Person( "Satoshi Nakamoto" ) ) );
	}
	
	private static class Classroom {
		private String className;
		private Set<Person> students;
		
		public Classroom( String className, Person... students ) {
			this.className = className;
			this.students = new HashSet<>();
			for( Person student : students ) {
				this.students.add( student );
			}
		}
		
		public String getClassName() {
			return className;
		}
	}
	
	private static class ClassFathers { 
		private String className;
		
		@CollectionProperty( source="students", itemSource="father" )
		private Set<Person> fathers;
		
		@SuppressWarnings( "unused" )
		public ClassFathers() {
			fathers = new HashSet<>();
		}
		
		public String getClassName() {
			return className;
		}

		public boolean contains( Person person ) {
			return fathers.contains( person );
		}

		public int size() {
			return fathers.size();
		}
	}
	
	@SuppressWarnings( "unused" )
	private static class Person {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
			return result;
		}

		@Override
		public boolean equals( Object obj ) {
			if ( this == obj )
				return true;
			if ( obj == null )
				return false;
			if ( getClass() != obj.getClass() )
				return false;
			Person other = (Person)obj;
			if ( name == null ) {
				if ( other.name != null )
					return false;
			} else if ( !name.equals( other.name ) )
				return false;
			return true;
		}

		private String name;
		private Person father;
		private Person mother;
		
		public Person( String name ) {
			this.name = name;
		}
		
		public Person( String name, Person father, Person mother ) {
			this.name = name;
			this.father = father;
			this.mother = mother;
		}
		
		public Person getFather() {
			return father;
		}
		
		public Person getMother() {
			return mother;
		}
		
		public String getName() {
			return name;
		}
	}
}
