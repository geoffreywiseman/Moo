package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

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
	@Ignore("TODO")
	public void testUpdateMapPropertyUpdatesObjectsInMapByKey() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetPropertyWithMatcherUpdatesObjectsByMatcher() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateListWillRemoveItemNotPresentInSource() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateMapWillRemoveItemWhoseKeyIsNotPresentInSourceMap() {
		fail( "Not yet implemented" );
	}

	@Test
	@Ignore("TODO")
	public void testUpdateSetWithMatcherWillRemoveItemNotPresentInSource() {
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
	public void testUpdateSetWithMatcherWillInsertItemNotFoundByMatcher() {
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
		@Property( update=true )
		private List<Value> values;

		public ValueList(Value... items) {
			values = new ArrayList<Value>();
			for( Value item : items ) {
				values.add( item );
			}
		}

		public Value get(int index) {
			return values.get( index );
		}

	}
}
