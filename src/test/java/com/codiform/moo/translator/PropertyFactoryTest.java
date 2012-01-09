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
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mvel2.sh.Command;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.InvalidAnnotationException;

public class PropertyFactoryTest {

	@SuppressWarnings("unused")
	private static class CollectionFieldPropertyContainer {
		@com.codiform.moo.annotation.CollectionProperty
		private Collection<String> explicitCollectionField;

		private Set<Double> implicitField;

		private static List<Object> implicitStaticField;

		@com.codiform.moo.annotation.CollectionProperty
		private static Collection<Integer> explicitStaticField;

		@com.codiform.moo.annotation.CollectionProperty(itemTranslation = Double.class)
		private Set<Float> translatable;

		@com.codiform.moo.annotation.CollectionProperty(update = true)
		private Set<Float> updatable;

		private List<Integer> copyMe;

		@com.codiform.moo.annotation.CollectionProperty(matcher = HashCodeMatcher.class)
		private Collection<Boolean> matchable;
	}

	@SuppressWarnings("unused")
	private static class CollectionMethodPropertyContainer {
		@com.codiform.moo.annotation.Property
		public static void setExplicitStaticMethod(Collection<String> collection) {
			// do nothing
		}

		public static void setImplicitStaticMethod(Collection<String> collection) {
			// do nothing
		}

		@com.codiform.moo.annotation.CollectionProperty
		public void execute(List<Runnable> runnable) {
			// do nothing
		}

		private Collection<Object> getGettable() {
			return null;
		}

		@com.codiform.moo.annotation.CollectionProperty
		private void setExplicitMethod(Set<String> explicitCollectionField) {
			// do nothing
		}

		private void setGettable(Collection<Object> collection) {
			// do nothing
		}

		private void setImplicitMethod(List<Integer> collection) {
			// do nothing
		}

		@com.codiform.moo.annotation.CollectionProperty(update = true)
		private void setUpdatable(List<String> strings) {
			// do nothing
		}

		private List<String> getUpdateable() {
			return null;
		}

		@com.codiform.moo.annotation.CollectionProperty(itemTranslation = Date.class)
		private void setTranslatable(List<Date> dates) {
			// do nothing
		}

		@com.codiform.moo.annotation.CollectionProperty(matcher = HashCodeMatcher.class)
		private void setMatchable(Collection<Date> dates) {
			// do nothing
		}
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

		@com.codiform.moo.annotation.Property(translate = true)
		private Integer translatable;

		@com.codiform.moo.annotation.Property(update = true)
		private Integer updateable;

		private Integer copyMe;
	}

	public static class HashCodeMatcher implements
			CollectionMatcher<Object, Object> {

		@Override
		public Object getTarget(Object source) {
			// ignore
			return null;
		}

		@Override
		public void setTargets(Collection<Object> targets) {
			// ignore
		}

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

		@com.codiform.moo.annotation.Property(update = true)
		public void setUpdatable(Date date) {
			// do nothing
		}

		public Date getUpdatable() {
			return null;
		}

		@com.codiform.moo.annotation.Property(translate = true)
		public void setTranslatable(Float value) {
			// do nothing
		}

		public void setUntranslatable(Float value) {
			// do nothing
		}
	}
	
	@Access(AccessMode.FIELD)
	public static class StringWithCollectionPropertyAnnotation {
		@SuppressWarnings("unused")
		@com.codiform.moo.annotation.CollectionProperty
		private String invalid;
	}

	@Access(AccessMode.FIELD)
	public static class ListWithPropertyAnnotation {
		@SuppressWarnings("unused")
		@com.codiform.moo.annotation.Property
		private List<String> invalid;
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

	private Method getCollectionMethod(String methodName,
			Class<?>... parameters)
			throws NoSuchMethodException {
		return CollectionMethodPropertyContainer.class.getDeclaredMethod(
				methodName,
				parameters );
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
	public void testCollectionFieldPropertyDetectsLackOfMatcher()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.hasMatcher() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsLackOfTranslation()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldBeTranslated() );
		assertFalse( property.shouldItemsBeTranslated() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsLackOfUpdating()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldUpdate() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsMatcher()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "matchable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "matchable", property.getName() );
		assertTrue( property.hasMatcher() );
		assertEquals( HashCodeMatcher.class, property.getMatcherType() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsTranslation()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "translatable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertFalse( property.shouldBeTranslated() );
		assertTrue( property.shouldItemsBeTranslated() );
		assertEquals( Double.class, property.getItemTranslationType() );
	}

	@Test
	public void testCollectionFieldPropertyDetectsUpdatability()
			throws NoSuchFieldException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionField( "updatable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "updatable", property.getName() );
		assertTrue( property.shouldUpdate() );
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
	public void testExplicitCollectionMethodCreatesPropertyIfAccessModeIsField()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.FIELD );
		assertCollectionProperty( "explicitMethod", true, false, Set.class,
				false,
				property );
	}

	@Test
	public void testExplicitCollectionMethodCreatesPropertyIfAccessModeIsMethod()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.METHOD );
		assertCollectionProperty( "explicitMethod", true, false, Set.class,
				false,
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
	public void testFieldPropertyCanDetectLackOfTranslation()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldBeTranslated() );
	}

	@Test
	public void testFieldPropertyCanDetectLackOfUpdating()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "copyMe" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "copyMe", property.getName() );
		assertFalse( property.shouldUpdate() );
	}

	@Test
	public void testFieldPropertyCanDetectTranslation()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "translatable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertTrue( property.shouldBeTranslated() );
	}

	@Test
	public void testFieldPropertyCanDetectUpdatability()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "updateable" ),
				AccessMode.FIELD );
		assertNotNull( property );
		assertEquals( "updateable", property.getName() );
		assertTrue( property.shouldUpdate() );
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
	public void testImplicitCollectionMethodCreatesCollectionFieldPropertyIfAccessModeIsMethod()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setImplicitMethod", List.class ),
				AccessMode.METHOD );
		assertCollectionProperty( "implicitMethod", false, false, List.class,
				false,
				property );
	}

	@Test
	public void testImplicitCollectionMethodIgnoredIfAccessModeIsField()
			throws NoSuchMethodException {
		Method field = getCollectionMethod( "setImplicitMethod", List.class );
		assertNotNull( field );
		Property property = PropertyFactory.createProperty(
				field, AccessMode.FIELD );
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
	public void testInvalidPropertyExceptionIfCollectionMethodDoesntBeginWithSet()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getCollectionMethod( "execute", List.class ),
					AccessMode.METHOD );
			fail( "Should not have created a property for a method not starting with 'set': "
					+ property );
		} catch( InvalidPropertyException ipe ) {
			assertEquals( "execute", ipe.getPropertyName() );
			assertThat( ipe.getMessage(),
					org.hamcrest.Matchers.containsString( "'set<Name>'" ) );
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
	public void testInvalidPropertyExceptionIfExplicitCollectionMethodIsStatic()
			throws NoSuchMethodException {
		try {
			Property property = PropertyFactory.createProperty(
					getCollectionMethod( "setExplicitStaticMethod",
							Collection.class ),
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
	public void testNoPropertyOrExceptionIfImplicitCollectionFieldIsStatic()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getCollectionField( "implicitStaticField" ), AccessMode.FIELD );
		assertNull( property );
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitCollectionMethodIsStatic()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setImplicitStaticMethod",
						Collection.class ),
				AccessMode.METHOD );
		assertNull( property );
	}

	@Test
	public void testNoPropertyOrExceptionIfImplicitFieldIsStatic()
			throws NoSuchFieldException {
		Property property = PropertyFactory.createProperty(
				getField( "implicitStaticField" ), AccessMode.FIELD );
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
	public void testSetterGetterPairCreatesGettableCollectionMethodProperty()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setGettable", Collection.class ),
				AccessMode.METHOD );
		assertCollectionProperty( "gettable", false, false, Collection.class,
				true, property );
	}

	@Test
	public void testSetterGetterPairCreatesGettableMethodProperty()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setGettable", Float.class ), AccessMode.METHOD );
		assertProperty( "gettable", false, false, Float.class, true, property );
	}

	@Test
	public void testSetterWithoutGetterCreatesUngettableCollectionMethodProperty()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.METHOD );
		assertFalse( property.canGetValue() );
	}

	@Test
	public void testMethodPropertyCanDetectLackOfUpdating()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setExplicitMethod", String.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertFalse( property.shouldUpdate() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsLackOfUpdating()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertFalse( property.shouldUpdate() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsUpdatability()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setUpdatable", List.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertTrue( "Collection method property should be updatable.",
				property.shouldUpdate() );
	}

	@Test
	public void testMethodPropertyCanDetectUpdatability()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setUpdatable", Date.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertTrue( property.shouldUpdate() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsLackOfTranslation()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertFalse( property.shouldBeTranslated() );
		assertFalse( property.shouldItemsBeTranslated() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsTranslation()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setTranslatable", List.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertFalse( property.shouldBeTranslated() );
		assertTrue( property.shouldItemsBeTranslated() );
		assertEquals( Date.class, property.getItemTranslationType() );
	}

	@Test
	public void testMethodPropertyCanDetectLackOfTranslation()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setUntranslatable", Float.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertEquals( "untranslatable", property.getName() );
		assertFalse( property.shouldBeTranslated() );
	}

	@Test
	public void testMethodPropertyCanDetectTranslation()
			throws NoSuchMethodException {
		Property property = PropertyFactory.createProperty(
				getMethod( "setTranslatable", Float.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertEquals( "translatable", property.getName() );
		assertTrue( property.shouldBeTranslated() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsLackOfMatcher()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setExplicitMethod", Set.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertEquals( "explicitMethod", property.getName() );
		assertFalse( property.hasMatcher() );
	}

	@Test
	public void testCollectionMethodPropertyDetectsMatcher()
			throws NoSuchMethodException {
		CollectionProperty property = (CollectionProperty) PropertyFactory.createProperty(
				getCollectionMethod( "setMatchable", Collection.class ),
				AccessMode.METHOD );
		assertNotNull( property );
		assertEquals( "matchable", property.getName() );
		assertTrue( property.hasMatcher() );
		assertEquals( HashCodeMatcher.class, property.getMatcherType() );
	}
	
	@Test(expected=InvalidAnnotationException.class)
	public void testCollectionWithPropertyAnnotationThrowsInvalidAnnotationException() throws SecurityException, NoSuchFieldException {
		PropertyFactory.createProperty( ListWithPropertyAnnotation.class.getDeclaredField( "invalid" ), AccessMode.FIELD );
	}

	@Test(expected=InvalidAnnotationException.class)
	public void testNonCollectionWithCollectionPropertyAnnotationThrowsInvalidAnnotationException() throws SecurityException, NoSuchFieldException {
		PropertyFactory.createProperty( StringWithCollectionPropertyAnnotation.class.getDeclaredField( "invalid" ), AccessMode.FIELD );
	}

}
