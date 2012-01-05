package com.codiform.moo.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mvel2.sh.Command;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.TranslateCollection;

public class PropertyFactoryTest {

	@SuppressWarnings("unused")
	private static class CollectionFieldPropertyContainer {
		@com.codiform.moo.annotation.Property
		private Collection<String> explicitCollectionField;

		private Set<Double> implicitField;

		private static List<Object> implicitStaticField;

		@com.codiform.moo.annotation.Property
		private static Collection<Integer> explicitStaticField;

		@TranslateCollection(Double.class)
		private Set<Float> translatable;
		
		private List<Integer> copyMe;
	}

	@SuppressWarnings("unused")
	private static class FieldPropertyContainer {

		@com.codiform.moo.annotation.Property
		private static boolean explicitStaticField;

		private static boolean implicitStaticField;

		@com.codiform.moo.annotation.Property
		private final String finalField = "final";

		private int implicitField;

		@com.codiform.moo.annotation.Property
		private long explicitField;
		
		@com.codiform.moo.annotation.Property(translate=true)
		private Integer translatable;
		
		private Integer copyMe;
	}

	@SuppressWarnings("unused")
	private static class MethodPropertyContainer {

		@com.codiform.moo.annotation.Property
		public static void setExplicitStaticMethod(Integer parameter) {
			// do nothing
		}

		public static void setImplicitStaticMethod(Boolean parameter) {
			// do nothing
		}

		@com.codiform.moo.annotation.Property
		public void execute(Command command) {
			// do nothing
		}

		public Float getGettable() {
			return null;
		}

		public boolean isBoolean() {
			return true;
		}

		public void setBoolean(boolean value) {

		}

		@com.codiform.moo.annotation.Property
		public void setExplicitMethod(String parameter) {
			// do nothing
		}

		public void setGettable(Float value) {

		}

		public void setImplicitMethod(BigDecimal parameter) {
			// do nothing
		}

		@com.codiform.moo.annotation.Property
		public void setNoValues() {
			// do nothing
		}

		@com.codiform.moo.annotation.Property
		public void setTwoValues(int id, String name) {
			// do nothing
		}
	}

	private void assertCollectionProperty(String name, boolean explicit,
			boolean ignored,
			Class<?> type, boolean canGetValue, Property property) {
		assertTrue( "Property should not be a collection.",
				property instanceof CollectionProperty );
		assertNotNull( property );
		assertEquals( name, property.getName() );
		assertEquals( explicit, property.isExplicit() );
		assertEquals( ignored, property.isIgnored() );
		assertSame( type, property.getType() );
		assertEquals( canGetValue, property.canGetValue() );

	}

	private void assertProperty(String name, boolean explicit, boolean ignored,
			Class<?> type, boolean canGetValue, Property property) {
		assertFalse( "Property should not be a collection.",
				property instanceof CollectionProperty );
		assertNotNull( property );
		assertEquals( name, property.getName() );
		assertEquals( explicit, property.isExplicit() );
		assertEquals( ignored, property.isIgnored() );
		assertSame( type, property.getType() );
		assertEquals( canGetValue, property.canGetValue() );

	}

	private Field getCollectionField(String fieldName)
			throws NoSuchFieldException {
		return CollectionFieldPropertyContainer.class.getDeclaredField( fieldName );
	}

	private Field getField(String fieldName) throws NoSuchFieldException {
		return FieldPropertyContainer.class.getDeclaredField( fieldName );
	}

	private Method getMethod(String methodName, Class<?>... parameters)
			throws NoSuchMethodException {
		return MethodPropertyContainer.class.getDeclaredMethod( methodName,
				parameters );
	}

	@Test
	public void testExplicitCollectionFieldCreatesCollectionFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getCollectionField( "explicitCollectionField" ),
				AccessMode.FIELD );
		assertCollectionProperty( "explicitCollectionField", true, false,
				Collection.class, true,
				property );
	}

	@Test
	public void testExplicitCollectionFieldCreatesCollectionFieldPropertyIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getCollectionField( "explicitCollectionField" ),
				AccessMode.METHOD );
		assertCollectionProperty( "explicitCollectionField", true, false,
				Collection.class, true,
				property );
	}

	@Test
	public void testExplicitFieldCreatesFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "explicitField" ), AccessMode.FIELD );
		assertProperty( "explicitField", true, false, long.class, true,
				property );
	}

	@Test
	public void testExplicitFieldCreatesFieldPropertyIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "explicitField" ), AccessMode.METHOD );
		assertProperty( "explicitField", true, false, long.class, true,
				property );
	}

	@Test
	public void testExplicitMethodCreatesPropertyIfAccessModeIsField()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setExplicitMethod", String.class ),
				AccessMode.FIELD );
		assertProperty( "explicitMethod", true, false, String.class, false,
				property );
	}

	@Test
	public void testExplicitMethodCreatesPropertyIfAccessModeIsMethod()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setExplicitMethod", String.class ),
				AccessMode.METHOD );
		assertProperty( "explicitMethod", true, false, String.class, false,
				property );
	}

	@Test
	public void testImplicitCollectionFieldCreatesCollectionFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getCollectionField( "implicitField" ), AccessMode.FIELD );
		assertCollectionProperty( "implicitField", false, false, Set.class,
				true,
				property );
	}

	@Test
	public void testImplicitCollectionFieldIgnoredIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Field field = getCollectionField( "implicitField" );
		assertNotNull( field );
		Property property = PropertyFactory.createProperty(
				field, AccessMode.METHOD );
		assertNull( property );
	}

	@Test
	public void testImplicitFieldCreatesFieldPropertyIfAccessModeIsField()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitField" ), AccessMode.FIELD );
		assertProperty( "implicitField", false, false, int.class, true,
				property );
	}

	@Test
	public void testImplicitFieldIgnoredIfAccessModeIsMethod()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitField" ), AccessMode.METHOD );
		assertNull( property );
	}

	@Test
	public void testImplicitMethodCreatesPropertyIfAccessModeIsMethod()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setImplicitMethod", BigDecimal.class ),
				AccessMode.METHOD );
		assertProperty( "implicitMethod", false, false, BigDecimal.class,
				false, property );
	}

	@Test
	public void testImplicitMethodIgnoredIfAccessModeIsField()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setImplicitMethod", BigDecimal.class ),
				AccessMode.FIELD );
		assertNull( property );
	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitFieldIsFinal()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getField( "finalField" ), AccessMode.FIELD );
			fail( "Should not have created a property for a final field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "finalField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "final field" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitFieldIsStatic()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getField( "explicitStaticField" ), AccessMode.FIELD );
			fail( "Should not have created a property for a static field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "explicitStaticField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "static field" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitCollectionFieldIsStatic()
			throws NoSuchFieldException {
		try {
			Property property = PropertyFactory.createProperty(
					getCollectionField( "explicitStaticField" ),
					AccessMode.FIELD );
			fail( "Should not have created a property for a static collection field: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "explicitStaticField", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "static field" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfExplicitMethodIsStatic()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getMethod( "setExplicitStaticMethod", Integer.class ),
					AccessMode.METHOD );
			fail( "Should not have created a property for a static method: "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "explicitStaticMethod", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "static method" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfMethodDoesntBeginWithSet()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getMethod( "execute", Command.class ), AccessMode.METHOD );
			fail( "Should not have created a property for a method not starting with 'set': "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "execute", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "'set<Name>'" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfMethodHasLessThanOneParameter()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getMethod( "setNoValues" ), AccessMode.METHOD );
			fail( "Should have thrown exception; property setter has no parameters"
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "noValues", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "single-parameter" ) );
		}
	}

	@Test
	public void testInvalidPropertyExceptionIfMethodHasMoreThanOneParameter()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getMethod( "setTwoValues", int.class, String.class ),
					AccessMode.METHOD );
			fail( "Should have thrown exception; property setter has two parameters"
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "twoValues", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "single-parameter" ) );
		}
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitFieldIsStatic()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitStaticField" ), AccessMode.FIELD );
		assertNull( property );
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitCollectionFieldIsStatic()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getCollectionField( "implicitStaticField" ), AccessMode.FIELD );
		assertNull( property );
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitMethodIsStatic()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setImplicitStaticMethod", Boolean.class ),
				AccessMode.METHOD );
		assertNull( property );
	}

	@Test
	public void testSetIsPairCreatesGettableBooleanMethodProperty()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setBoolean", boolean.class ), AccessMode.METHOD );
		assertProperty( "boolean", false, false, boolean.class, true, property );
	}

	@Test
	public void testSetterGetterPairCreatesGettableMethodProperty()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setGettable", Float.class ), AccessMode.METHOD );
		assertProperty( "gettable", false, false, Float.class, true, property );
	}

	@Test
	public void testFieldPropertyCanDetectTranslation() throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "translatable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertTrue( property.shouldBeTranslated() );
	}

	@Test
	public void testFieldPropertyCanDetectLackOfTranslation() throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldBeTranslated() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsTranslation() throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "translatable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertFalse( property.shouldBeTranslated() );
		assertTrue( property.shouldItemsBeTranslated() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsLackOfTranslation() throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldBeTranslated() );
		assertFalse( property.shouldItemsBeTranslated() );
	}
}
