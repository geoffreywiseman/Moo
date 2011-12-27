package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.curry.Update;

public class CollectionUpdateTest {

	@Test
	public void testUpdateCollectionPropertyUpdatesObjectInsideCollection() {
		ValueDto dto = new ValueDto( 1, "Updated" );
		ValueDtoList dtoList = new ValueDtoList( dto );

		Value value = new Value( 1, "Original" );
		ValueList valueList = new ValueList( value );
		Update.from( dtoList ).to( valueList );

		Value actual = valueList.get( 0 );
		assertSame( value, actual );
		assertEquals( Integer.valueOf( 1 ), actual.getId() );
		assertEquals( "Updated", actual.getName() );
	}

	@Test
	public void testUpdateListPropertyUpdatesObjectsInListByIndex() {
		ValueDto firstDto = new ValueDto( 2, "Now First" );
		ValueDto secondDto = new ValueDto( 1, "Now Second" );
		ValueDtoList dtoList = new ValueDtoList( firstDto, secondDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		ValueList valueList = new ValueList( firstValue, secondValue );
		Update.from( dtoList ).to( valueList );

		assertIdentityAndValues( firstValue, firstDto, valueList.get( 0 ) );
		assertIdentityAndValues( secondValue, secondDto, valueList.get( 1 ) );
	}

	@Test
	public void testUpdateMapPropertyUpdatesObjectsInMapByKey() {
		ValueDto flaDto = new ValueDto( 242, "Front Line Assembly" );
		ValueDto tkkDto = new ValueDto( 38, "My Life with the Thrill Kill Kult" );
		ValueDtoMap dtoMap = new ValueDtoMap();
		dtoMap.put( "FLA", flaDto );
		dtoMap.put( "TKK", tkkDto );

		Value fla = new Value( 1, "Front Line" );
		Value tkk = new Value( 2, "Thrill Kill" );
		ValueMap valueMap = new ValueMap();
		valueMap.put( "TKK", tkk );
		valueMap.put( "FLA", fla );

		Update.from( dtoMap ).to( valueMap );

		assertIdentityAndValues( tkk, tkkDto, valueMap.get( "TKK" ) );
		assertIdentityAndValues( fla, flaDto, valueMap.get( "FLA" ) );
	}

	@Test
	public void testUpdateListWillRemoveItemNotPresentInSource() {
		ValueDto firstDto = new ValueDto( 1, "Still First" );
		ValueDto secondDto = new ValueDto( 3, "Was Third, Now Second" );
		ValueDtoList dtoList = new ValueDtoList( firstDto, secondDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		ValueList valueList = new ValueList( firstValue, secondValue,
				new Value( 3, "Third" ) );
		Update.from( dtoList ).to( valueList );

		assertEquals(
				"Update should have removed a value from the destination list.",
				2, valueList.size() );

		assertIdentityAndValues( firstValue, firstDto, valueList.get( 0 ) );
		assertIdentityAndValues( secondValue, secondDto, valueList.get( 1 ) );
	}

	private void assertIdentityAndValues(Value expectedIdentity,
			ValueDto expectedValues,
			Value actual) {
		assertSame( expectedIdentity, actual );
		assertEqualValues( expectedValues, actual );
	}

	private void assertEqualValues(ValueDto expectedValues, Value actual) {
		assertEquals( expectedValues.getId(), actual.getId() );
		assertEquals( expectedValues.getName(), actual.getName() );
	}

	@Test
	public void testUpdateMapWillRemoveItemWhoseKeyIsNotPresentInSourceMap() {
		ValueDto iphone4sDto = new ValueDto( 1, "HSPA+" );
		ValueDto nexusDto = new ValueDto( 3, "LTE" );

		ValueDtoMap dtoMap = new ValueDtoMap();
		dtoMap.put( "iPhone 4S", iphone4sDto );
		dtoMap.put( "Galaxy Nexus", nexusDto );

		Value iphone4s = new Value( 1, "4G" );
		Value nexus = new Value( 3, "4G" );

		ValueMap valueMap = new ValueMap();
		valueMap.put( "iPhone 4S", iphone4s );
		valueMap.put( "iPhone 4", new Value( 2, "3G" ) );
		valueMap.put( "Galaxy Nexus", nexus );

		Update.from( dtoMap ).to( valueMap );

		assertEquals( 2, valueMap.size() );
		assertIdentityAndValues( iphone4s, iphone4sDto,
				valueMap.get( "iPhone 4S" ) );
		assertIdentityAndValues( nexus, nexusDto,
				valueMap.get( "Galaxy Nexus" ) );
		assertFalse( valueMap.containsKey( "iPhone 4" ) );
	}

	@Test
	public void testUpdateMapWillNullifyItemWhoseKeyHasNullValueInSourceMap() {
		ValueDto iphone4sDto = new ValueDto( 1, "HSPA+" );
		ValueDto nexusDto = new ValueDto( 3, "LTE" );

		ValueDtoMap dtoMap = new ValueDtoMap();
		dtoMap.put( "iPhone 4S", iphone4sDto );
		dtoMap.put( "Galaxy Nexus", nexusDto );
		dtoMap.put( "iPhone 4", null );

		Value iphone4s = new Value( 1, "4G" );
		Value nexus = new Value( 3, "4G" );

		ValueMap valueMap = new ValueMap();
		valueMap.put( "iPhone 4S", iphone4s );
		valueMap.put( "iPhone 4", new Value( 2, "3G" ) );
		valueMap.put( "Galaxy Nexus", nexus );

		Update.from( dtoMap ).to( valueMap );

		assertEquals( 3, valueMap.size() );
		assertIdentityAndValues( iphone4s, iphone4sDto,
				valueMap.get( "iPhone 4S" ) );
		assertIdentityAndValues( nexus, nexusDto,
				valueMap.get( "Galaxy Nexus" ) );
		assertTrue(
				"Value Map should have retained key, but set to null after update.",
				valueMap.containsKey( "iPhone 4" ) );
		assertNull( valueMap.get( "iPhone 4" ) );
	}

	@Test
	public void testUpdateListWillInsertItemNotPresentInDestination() {
		ValueDto firstDto = new ValueDto( 1, "Updated Original" );
		ValueDto secondDto = new ValueDto( 2, "New to this Update" );
		ValueDtoList dtoList = new ValueDtoList( firstDto, secondDto );

		Value firstValue = new Value( 1, "Original" );
		ValueList valueList = new ValueList( firstValue );

		Update.from( dtoList ).to( valueList );

		assertEquals( "Update should have added a new item to the value list.",
				2, valueList.size() );
		assertIdentityAndValues( firstValue, firstDto, valueList.get( 0 ) );
		assertEqualValues( secondDto, valueList.get( 1 ) );
	}

	@Test
	public void testUpdateMapWillInsertItemWhoseKeyIsNotPresentInDestinationMap() {
		ValueDto tardisDto = new ValueDto( 1,
				"Time and Relative Dimension in Space" );
		ValueDto ssDto = new ValueDto( 2, "Sonic Screwdriver" );
		ValueDtoMap dtoMap = new ValueDtoMap();
		dtoMap.put( "TARDIS", tardisDto );
		dtoMap.put( "SS", ssDto );
		ValueMap valueMap = new ValueMap();
		Value tardis = new Value( 1,
				"Dr. Who's Ship" );
		valueMap.put( "TARDIS", tardis );

		Update.from( dtoMap ).to( valueMap );

		assertEquals( 2, valueMap.size() );
		assertIdentityAndValues( tardis, tardisDto, valueMap.get( "TARDIS" ) );
		assertEqualValues( ssDto, valueMap.get( "SS" ) );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateListWillInsertTranslatedItemNotPresentInDestination() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateMapWillInsertTranslatedItemWhoseKeyIsNotPresentInDestinationMap() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetPropertyWithMatcherUpdatesObjectsByMatcher() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetWithMatcherWillRemoveItemNotPresentInSource() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetWithMatcherWillInsertItemNotFoundByMatcher() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetWithMatcherWillInsertTranslatedItemNotFoundByMatcher() {
		fail( "Not yet implemented" );
	}

	public static class Value {
		private Integer id;
		private String name;

		public Value(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@SuppressWarnings("unused")
		private Value() {
			// for translation only
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class ValueDto {
		private Integer id;
		private String name;

		public ValueDto(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@SuppressWarnings("unused")
		private ValueDto() {
			// for translation only
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	public static class ValueDtoList {
		private List<ValueDto> values;

		public ValueDtoList(ValueDto... dtos) {
			values = new ArrayList<ValueDto>();
			for( ValueDto item : dtos ) {
				values.add( item );
			}
		}

		public ValueDto get(int index) {
			return values.get( index );
		}

		public List<ValueDto> getValues() {
			return values;
		}
	}

	public static class ValueList {
		@TranslateCollection(Value.class)
		@Property(update = true)
		private List<Value> values;

		public ValueList(Value... items) {
			values = new ArrayList<Value>();
			for( Value item : items ) {
				values.add( item );
			}
		}

		public int size() {
			return values.size();
		}

		public Value get(int index) {
			return values.get( index );
		}

	}

	public static class ValueDtoMap {
		private Map<String, ValueDto> values;

		public ValueDtoMap() {
			values = new HashMap<String, ValueDto>();
		}

		public void put(String index, ValueDto dto) {
			values.put( index, dto );
		}

		public ValueDto get(String index) {
			return values.get( index );
		}

		public Map<String, ValueDto> getValues() {
			return values;
		}
	}

	public static class ValueMap {
		@TranslateCollection(Value.class)
		@Property(update = true)
		private Map<String, Value> values;

		public ValueMap() {
			values = new HashMap<String, Value>();
		}

		public boolean containsKey(String key) {
			return values.containsKey( key );
		}

		public int size() {
			return values.size();
		}

		public void put(String index, Value value) {
			values.put( index, value );
		}

		public Value get(String index) {
			return values.get( index );
		}

	}
}
