package com.codiform.moo;

import java.util.Calendar;
import java.util.Date;

import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Ignore;
import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Translate;
import com.codiform.moo.curry.Update;
import org.junit.Test;

import static org.junit.Assert.*;

public class IgnoreAnnotationTest {

	@Test
	public void testMooDoesntOverwriteIgnoredDestinationFieldOnTranslate() {
		Source source = new Source( 1,
				"Source doesn't overwrite ignored destination field on translate." );
		IgnoreAvailableFieldDestination destination = Translate.to(
				IgnoreAvailableFieldDestination.class ).from( source );
		assertNull( "Id field should have been ignored during translation.",
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testMooDoesntOverwriteIgnoredDestinationFieldOnUpdate() {
		IgnoreAvailableFieldDestination destination = new IgnoreAvailableFieldDestination(
				242, "To be updated." );
		Source source = new Source( 2,
				"Source doesn't overwrite ignored destination field on update." );
		Update.from( source ).to( destination );
		assertEquals( Integer.valueOf( 242 ), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testMooDoesntCallIgnoredDestinationSetterOnTranslate() {
		Source source = new Source( 1,
				"Source doesn't overwrite ignored destination field on translate." );
		IgnoreAvailableMethodDestination destination = Translate.to(
				IgnoreAvailableMethodDestination.class ).from( source );
		assertNull( "Id field should have been ignored during translation.",
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testMooDoesntCallIgnoredDestinationSetterOnUpdate() {
		IgnoreAvailableMethodDestination destination = new IgnoreAvailableMethodDestination(
				242, "To be updated." );
		Source source = new Source( 2,
				"Source doesn't overwrite ignored destination field on update." );
		Update.from( source ).to( destination );
		assertEquals( Integer.valueOf( 242 ), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testNoExceptionThrownOnTranslateIfDestinationFieldHasIgnoreAnnotationAndNoSource() {
		Source source = new Source(
				7,
				"No source for creation date, but marked with @Ignore." );
		IgnoreMissingSourceFieldDestination destination = Translate.to(
				IgnoreMissingSourceFieldDestination.class ).from( source );
		assertEquals( source.getId(), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
		assertNull( destination.getCreationDate() );
	}

	@Test
	public void testNoExceptionThrownOnUpdateIfDestinationFieldHasIgnoreAnnotationAndNoSource() {
		Source source = new Source(
				8,
				"No source for creationdate, but marked with @Ignore." );
		Calendar cal = Calendar.getInstance();
		cal.set( 2008, Calendar.MARCH, 1 );
		Date date = cal.getTime();
		IgnoreMissingSourceFieldDestination destination = new IgnoreMissingSourceFieldDestination(
				242, "To Be Updated", date );
		Update.from( source ).to( destination );

		assertEquals( source.getId(), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
		assertSame( date, destination.getCreationDate() );
	}

	@Test
	public void testNoExceptionThrownOnTranslateIfDestinationMethodHasIgnoreAnnotationAndNoSource() {
		Source source = new Source(
				9,
				"No source for creation date, but marked with @Ignore." );
		IgnoreMissingSourceMethodDestination destination = Translate.to(
				IgnoreMissingSourceMethodDestination.class ).from( source );
		assertEquals( source.getId(), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
		assertNull( destination.getCreationDate() );
	}

	@Test
	public void testNoExceptionThrownOnUpdateIfDestinationMethodHasIgnoreAnnotationAndNoSource() {
		Source source = new Source(
				10,
				"No source for creationdate, but marked with @Ignore." );
		Calendar cal = Calendar.getInstance();
		cal.set( 2008, Calendar.MARCH, 1 );
		Date date = cal.getTime();
		IgnoreMissingSourceMethodDestination destination = new IgnoreMissingSourceMethodDestination(
				242, "To Be Updated", date );
		Update.from( source ).to( destination );

		assertEquals( source.getId(), destination.getId() );
		assertEquals( source.getName(), destination.getName() );
		assertSame( date, destination.getCreationDate() );
	}

	@Test
	public void testIgnoreAnnotationOnChildOverridesExplicitPropertyOnParent() {
		Source source = new Source( 10,
				"Child @Ignore overrides explicit parent property." );
		ExplicitIgnoreChildForExplicitParent destination = Translate.to(
				ExplicitIgnoreChildForExplicitParent.class ).from( source );
		assertNull( "Id field should have been ignored during translation.",
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testIgnoreAnnotationOnChildOverridesImplicitPropertyOnParent() {
		Source source = new Source( 11,
				"Child @Ignore overrides implicit parent property." );
		ExplicitIgnoreChildForImplicitParent destination = Translate.to(
				ExplicitIgnoreChildForImplicitParent.class ).from( source );
		assertNull( "Id field should have been ignored during translation.",
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testPropertyAnnotationOnChildOverridesIgnoreOnParent() {
		Source source = new Source( 11,
				"Child @Ignore overrides implicit parent property." );
		ExplicitPropertyChildForIgnoreParent destination = Translate.to(
				ExplicitPropertyChildForIgnoreParent.class ).from( source );
		assertEquals( "Id field should have included during translation.",
				source.getId(),
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );
	}

	@Test
	public void testImplicitPropertyOnChildDoesNotOverrideIgnoreOnParent() {
		Source source = new Source( 11,
				"Child @Ignore overrides implicit parent property." );
		ImplicitPropertyChildForIgnoreParent destination = Translate.to(
				ImplicitPropertyChildForIgnoreParent.class ).from( source );
		assertNull( "Id field should have been ignored during translation.",
				destination.getId() );
		assertEquals( source.getName(), destination.getName() );

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

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	@Access(AccessMode.FIELD)
	public static class IgnoreAvailableFieldDestination {

		@Ignore("Don't overwrite the id.")
		private Integer id;

		private String name;

		public IgnoreAvailableFieldDestination() {
			// no-arg constructor for moo
		}

		public IgnoreAvailableFieldDestination(int id, String name) {
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

	@Access(AccessMode.METHOD)
	public static class IgnoreAvailableMethodDestination {
		private Integer id;

		private String name;

		public IgnoreAvailableMethodDestination() {
			// No-Arg for Moo
		}

		public IgnoreAvailableMethodDestination(Integer id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Ignore("Don't overwrite the id.")
		public void setId(Integer id) {
			this.id = id;
		}
	}

	@Access(AccessMode.FIELD)
	public static class IgnoreMissingSourceFieldDestination {

		private Integer id;

		private String name;

		@Ignore("No creation date in source.")
		private Date creationDate;

		public IgnoreMissingSourceFieldDestination() {
			// no-arg constructor for moo
		}

		public IgnoreMissingSourceFieldDestination(int id, String name,
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
	public static class IgnoreMissingSourceMethodDestination {

		private Integer id;

		private String name;

		private Date creationDate;

		public IgnoreMissingSourceMethodDestination() {
			// no-arg constructor for moo
		}

		public IgnoreMissingSourceMethodDestination(int id, String name,
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

		@Ignore("No source for this.")
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

	public static class ImplicitParent {

		private Integer id;

		private String name;

		public ImplicitParent() {
			// for translation
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static class ExplicitParent {

		private Integer id;

		private String name;

		public ExplicitParent() {
			// for translation
		}

		public String getName() {
			return name;
		}

		public Integer getId() {
			return id;
		}

		@Property
		public void setId(Integer id) {
			this.id = id;
		}

		@Property
		public void setName(String name) {
			this.name = name;
		}

	}

	public static class ExplicitIgnoreChildForImplicitParent extends
			ImplicitParent {

		public ExplicitIgnoreChildForImplicitParent() {
			super();
		}

		@Ignore
		@Override
		public void setId(Integer id) {
			super.setId( id );
		}

	}

	public static class ExplicitIgnoreChildForExplicitParent extends
			ExplicitParent {

		public ExplicitIgnoreChildForExplicitParent() {
			super();
		}

		@Ignore
		@Override
		public void setId(Integer id) {
			super.setId( id );
		}

	}

	public static class ExplicitPropertyChildForIgnoreParent extends IgnoreAvailableMethodDestination {
		
		public ExplicitPropertyChildForIgnoreParent() {
			super();
		}

		@Property
		@Override
		public void setId(Integer id) {
			super.setId( id );
		}
		
	}

	public static class ImplicitPropertyChildForIgnoreParent extends IgnoreAvailableMethodDestination {
		
		public ImplicitPropertyChildForIgnoreParent() {
			super();
		}

		@Override
		public void setId(Integer id) {
			super.setId( id );
		}
		
	}
}
