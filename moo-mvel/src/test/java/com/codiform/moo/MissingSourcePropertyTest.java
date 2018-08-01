package com.codiform.moo;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.annotation.Ignore;
import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.annotation.Property;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.curry.Update;

/**
 * Tests surrounding what happens when a property in a destination class cannot
 * be found in the source class.
 * 
 * There are tests in here to deal with source field required/optional in Moo
 * configuration, but the impact of the {@link Ignore} annotation can be seen in
 * IgnoreAnnotationTest.
 */
public class MissingSourcePropertyTest {

	@Test(expected = MissingSourcePropertyValueException.class )
	public void testMooThrowsExceptionIfDestinationFieldNotFoundInSourceOnTranslation() {
		Source source = new Source(
				1,
				"Moo throws an exception if the destination field isn't found in the source on translation." );
		Translate.to( MissingSourceFieldDestination.class ).from( source );
		fail( "Moo should have thrown an error due to a missing source property." );
	}

	@Test(expected = MissingSourcePropertyValueException.class )
	public void testMooThrowsExceptionIfDestinationFieldNotFoundInSourceOnUpdate() {
		Source source = new Source(
				2,
				"Moo throws an exception if the destination field isn't found in the source on update." );
		MissingSourceFieldDestination destination = new MissingSourceFieldDestination(
				242, "To Be Updated", new Date() );
		Update.from( source ).to( destination );
		fail( "Moo should have thrown an error due to a missing source property." );
	}

	@Test(expected = MissingSourcePropertyValueException.class )
	public void testMooThrowsExceptionIfDestinationMethodNotFoundInSourceOnTranslation() {
		Source source = new Source(
				3,
				"Moo throws an exception if the destination method isn't found in the source on translation." );
		Translate.to( MissingSourceMethodDestination.class ).from( source );
		fail( "Moo should have thrown an error due to a missing source property." );
	}

	@Test(expected = MissingSourcePropertyValueException.class )
	public void testMooThrowsExceptionIfDestinationMethodNotFoundInSourceOnUpdate() {
		Source source = new Source(
				4,
				"Moo throws an exception if the destination method isn't found in the source on update." );
		MissingSourceMethodDestination destination = new MissingSourceMethodDestination(
				242, "To Be Updated", new Date() );
		Update.from( source ).to( destination );
		fail( "Moo should have thrown an error due to a missing source property." );
	}

	@Test
	public void testMooIgnoresMissingSourceForFieldOnTranslateIfSourcePropertiesNotRequired() {
		Moo moo = getMooWithOptionalSources();
		Source source = new Source(
				1,
				"Moo throws an exception if the destination field isn't found in the source on translation." );
		moo.translate( MissingSourceFieldDestination.class ).from( source );
	}

	private Moo getMooWithOptionalSources() {
		Configuration configuration = new Configuration();
		configuration.setSourcePropertiesRequired( false );
		return new Moo( configuration );
	}

	@Test
	public void testMooIgnoresMissingSourceForFieldOnUpdateIfSourcePropertiesNotRequired() {
		Moo moo = getMooWithOptionalSources();
		Source source = new Source(
				2,
				"Moo throws an exception if the destination field isn't found in the source on update." );
		MissingSourceFieldDestination destination = new MissingSourceFieldDestination(
				242, "To Be Updated", new Date() );
		moo.update( source, destination );
	}

	@Test
	public void testMooIgnoresMissingSourceForMethodOnTranslateIfSourcePropertiesNotRequired() {
		Moo moo = getMooWithOptionalSources();
		Source source = new Source(
				3,
				"Moo throws an exception if the destination method isn't found in the source on translation." );
		moo.translate( MissingSourceMethodDestination.class ).from( source );
	}

	@Test
	public void testMooIgnoresMissingSourceForMethodOnUpdateIfSourcePropertiesNotRequired() {
		Moo moo = getMooWithOptionalSources();
		Source source = new Source(
				4,
				"Moo throws an exception if the destination method isn't found in the source on update." );
		MissingSourceMethodDestination destination = new MissingSourceMethodDestination(
				242, "To Be Updated", new Date() );
		moo.update( source, destination );
	}

	@Test
	public void testMooIgnoresMissingSourceForMethodMarkedOptionalOnTranslate() {
		Source source = new Source(
				3,
				"Moo throws an exception if the destination method isn't found in the source on translation." );
		Translate.to( OptionalMethodDestination.class ).from( source );
	}

	@Test
	public void testMooIgnoresMissingSourceForMethodMarkedOptionalOnUpdateIfSourcePropertiesNotRequired() {
		Source source = new Source(
				4,
				"Moo throws an exception if the destination method isn't found in the source on update." );
		OptionalMethodDestination destination = new OptionalMethodDestination(
				242, "To Be Updated", new Date() );
		Update.from( source ).to( destination );
	}

	public static class Source {

		private Integer id;

		private String name;

		public Source(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

	}

	@Access(AccessMode.FIELD)
	public static class MissingSourceFieldDestination {

		private Integer id;

		private String name;

		private Date creationDate;

		public MissingSourceFieldDestination() {
			// no-arg constructor for moo
		}

		public MissingSourceFieldDestination(int id, String name,
				Date creationDate) {
			this.id = id;
			this.name = name;
			this.creationDate = creationDate;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		public Date getCreationDate() {
			return creationDate;
		}

	}

	@Access(AccessMode.METHOD)
	public static class MissingSourceMethodDestination {

		private Integer id;

		private String name;

		private Date creationDate;

		public MissingSourceMethodDestination() {
			// no-arg constructor for moo
		}

		public MissingSourceMethodDestination(int id, String name,
				Date creationDate) {
			this.id = id;
			this.name = name;
			this.creationDate = creationDate;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		public Date getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@Access(AccessMode.METHOD)
	public static class OptionalMethodDestination {

		private Integer id;

		private String name;

		private Date creationDate;

		private List<Date> accessDates;

		public OptionalMethodDestination() {
			// no-arg constructor for moo
			this.accessDates = new ArrayList<Date>();
		}

		public OptionalMethodDestination(int id, String name,
				Date creationDate) {
			this();
			this.id = id;
			this.name = name;
			this.creationDate = creationDate;
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		public Date getCreationDate() {
			return creationDate;
		}

		@Property(optionality = Optionality.OPTIONAL)
		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}

		@CollectionProperty(optionality = Optionality.OPTIONAL)
		public void setAccessDates(List<Date> accessDates) {
			this.accessDates = accessDates;
		}
		
		public List<Date> getAccessDates() {
			return accessDates;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
