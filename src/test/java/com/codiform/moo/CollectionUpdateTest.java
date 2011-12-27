package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.codiform.moo.annotation.Property;
import com.codiform.moo.curry.Update;

public class CollectionUpdateTest {

	@Test
	public void testUpdateCollectionPropertyUpdatesObjectInsideCollection() {
		ValueDto dto = new ValueDto( 1, "Updated" );
		ValueDtoList dtoList = new ValueDtoList( dto );

		Value value = new Value( 1, "Original" );
		ValueList valueList = new ValueList( value );
		Update.from( dtoList ).to( valueList );

		Value updated = valueList.get( 0 );
		assertSame( value, updated );
		assertEquals( dto.getId(), updated.getId() );
		assertEquals( dto.getName(), updated.getName() );
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

		Value updated = valueList.get( 0 );
		assertSame( firstValue, updated );
		assertEquals( firstDto.getId(), updated.getId() );
		assertEquals( firstDto.getName(), updated.getName() );

		updated = valueList.get( 1 );
		assertSame( secondValue, updated );
		assertEquals( secondDto.getId(), updated.getId() );
		assertEquals( secondDto.getName(), updated.getName() );
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

		Value updated = valueMap.get( "TKK" );
		assertSame( tkk, updated );
		assertEquals( tkkDto.getId(), updated.getId() );
		assertEquals( tkkDto.getName(), updated.getName() );

		updated = valueMap.get( "FLA" );
		assertSame( fla, updated );
		assertEquals( flaDto.getId(), updated.getId() );
		assertEquals( flaDto.getName(), updated.getName() );
	}

	@Test
	public void testUpdateListWillRemoveItemNotPresentInSource() {
		ValueDto firstDto = new ValueDto( 1, "Still First" );
		ValueDto secondDto = new ValueDto( 3, "Was Third, Now Second" );
		ValueDtoList dtoList = new ValueDtoList( firstDto, secondDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		Value thirdValue = new Value( 3, "Third" );
		ValueList valueList = new ValueList( firstValue, secondValue, thirdValue );
		Update.from( dtoList ).to( valueList );

		assertEquals( "Update should have removed a value from the destination list.", 2, valueList.size() );
		
		Value updated = valueList.get( 0 );
		assertSame( firstValue, updated );
		assertEquals( firstDto.getId(), updated.getId() );
		assertEquals( firstDto.getName(), updated.getName() );

		updated = valueList.get( 1 );
		assertSame( secondValue, updated );
		assertEquals( secondDto.getId(), updated.getId() );
		assertEquals( secondDto.getName(), updated.getName() );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateMapWillRemoveItemWhoseKeyIsNotPresentInSourceMap() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateListWillInsertItemNotPresentInDestination() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateMapWillInsertItemWhoseKeyIsNotPresentInDestinationMap() {
		fail( "Not yet implemented" );
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
		@Property(update = true)
		private Map<String, Value> values;

		public ValueMap() {
			values = new HashMap<String, Value>();
		}

		public void put(String index, Value value) {
			values.put( index, value );
		}

		public Value get(String index) {
			return values.get( index );
		}

	}
}
