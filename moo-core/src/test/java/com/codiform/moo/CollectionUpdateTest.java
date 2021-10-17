package com.codiform.moo;

import java.util.*;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.curry.Update;
import com.codiform.moo.translator.CollectionMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class CollectionUpdateTest {

	public static class FieldValueSet implements Iterable<Value> {
		@CollectionProperty(itemClass = Value.class, matcher = ValueIdMatcher.class, update = true)
		private Set<Value> values;

		public FieldValueSet() {
			values = new HashSet<>();
		}

		public FieldValueSet(Value... values) {
			this();
			for( Value item : values ) {
				add( item );
			}
		}

		public void add(Value value) {
			values.add( value );
		}

		@Override
		public Iterator<Value> iterator() {
			return values.iterator();
		}

		public int size() {
			return values.size();
		}
	}

	public static class MethodValueSet implements Iterable<Value> {
		private Set<Value> values;

		public MethodValueSet() {
			values = new HashSet<>();
		}

		public MethodValueSet(Value... values) {
			this();
			for( Value item : values ) {
				add( item );
			}
		}

		public void add(Value value) {
			values.add( value );
		}

		public Set<Value> getValues() {
			return values;
		}

		@Override
		public Iterator<Value> iterator() {
			return values.iterator();
		}

		@CollectionProperty(itemClass = Value.class, matcher = ValueIdMatcher.class, update = true)
		public void setValues(Set<Value> values) {
			this.values = values;
		}

		public int size() {
			return values.size();
		}
	}

	public static class RetainValueSet extends MethodValueSet {
		public RetainValueSet() {
			super();
		}

		public RetainValueSet(Value... values) {
			super( values );
		}

		@Override
		@CollectionProperty(itemClass = Value.class, matcher = ValueIdMatcher.class, update = true, removeOrphans = false)
		public void setValues(Set<Value> values) {
			super.setValues( values );
		}
	}

	public static class Value {
		private Integer id;
		private String name;

		@SuppressWarnings("unused")
		private Value() {
			// for translation only
		}

		public Value(int id, String name) {
			this.id = id;
			this.name = name;
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

		@SuppressWarnings("unused")
		private ValueDto() {
			// for translation only
		}

		public ValueDto(int id, String name) {
			this.id = id;
			this.name = name;
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
			values = new ArrayList<>();
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

	public static class ValueIdMatcher implements
			CollectionMatcher<ValueDto, Value> {

		private Map<Integer, Value> valueById;

		public ValueIdMatcher() {
			valueById = new HashMap<>();
		}

		@Override
		public Value getTarget(ValueDto source) {
			return valueById.get( source.getId() );
		}

		@Override
		public void setTargets(Collection<Value> targets) {
			valueById.clear();
			for( Value item : targets ) {
				valueById.put( item.getId(), item );
			}
		}

	}

	public static class ValueList {
		@CollectionProperty(itemClass = Value.class, update = true)
		private List<Value> values;

		public ValueList(Value... items) {
			values = new ArrayList<>();
			for( Value item : items ) {
				values.add( item );
			}
		}

		public Value get(int index) {
			return values.get( index );
		}

		public int size() {
			return values.size();
		}

	}

	public static class RetainValueList {
		@CollectionProperty(itemClass = Value.class, update = true, removeOrphans = false)
		private List<Value> values;

		public RetainValueList(Value... items) {
			values = new ArrayList<>();
			for( Value item : items ) {
				values.add( item );
			}
		}

		public Value get(int index) {
			return values.get( index );
		}

		public int size() {
			return values.size();
		}

	}

	private void assertEqualValues(ValueDto expectedValues, Value actual) {
		assertEquals( expectedValues.getId(), actual.getId() );
		assertEquals( expectedValues.getName(), actual.getName() );
	}

	private void assertIdentityAndValues(Value expectedIdentity,
			ValueDto expectedValues,
			Value actual) {
		assertSame( expectedIdentity, actual );
		assertEqualValues( expectedValues, actual );
	}

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
	public void testUpdateCollectionViaMethodWithMatcherUpdatesObjectsByMatcher() {
		ValueDto secondDto = new ValueDto( 2, "Updated Second" );
		ValueDto firstDto = new ValueDto( 1, "Updated First" );
		ValueDtoList dtoList = new ValueDtoList( secondDto, firstDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		MethodValueSet valueSet = new MethodValueSet( firstValue, secondValue );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 2, valueSet.size() );
		for( Value item : valueSet ) {
			if( item.getId() == 1 )
				assertIdentityAndValues( firstValue, firstDto, item );
			else
				assertIdentityAndValues( secondValue, secondDto, item );
		}
	}

	@Test
	public void testUpdateCollectionViaMethodWithMatcherWillRemoveItemNotPresentInSource() {
		ValueDto firstDto = new ValueDto( 1, "Updated First" );
		ValueDtoList dtoList = new ValueDtoList( firstDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		MethodValueSet valueSet = new MethodValueSet( secondValue, firstValue );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 1, valueSet.size() );
		Iterator<Value> updated = valueSet.iterator();
		assertIdentityAndValues( firstValue, firstDto, updated.next() );
	}

	@Test
	public void testUpdateCollectionWithMatcherUpdatesObjectsByMatcher() {
		ValueDto secondDto = new ValueDto( 2, "Updated Second" );
		ValueDto firstDto = new ValueDto( 1, "Updated First" );
		ValueDtoList dtoList = new ValueDtoList( secondDto, firstDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		FieldValueSet valueSet = new FieldValueSet( firstValue, secondValue );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 2, valueSet.size() );
		for( Value item : valueSet ) {
			if( item.getId() == 1 )
				assertIdentityAndValues( firstValue, firstDto, item );
			else
				assertIdentityAndValues( secondValue, secondDto, item );
		}
	}

	@Test
	public void testUpdateCollectionWithMatcherWillInsertItemNotFoundByMatcher() {
		ValueDto secondDto = new ValueDto( 2, "New Second" );
		ValueDto firstDto = new ValueDto( 1, "Updated First" );
		ValueDtoList dtoList = new ValueDtoList( secondDto, firstDto );

		Value firstValue = new Value( 1, "First" );
		FieldValueSet valueSet = new FieldValueSet( firstValue );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 2, valueSet.size() );
		for( Value item : valueSet ) {
			if( item.getId() == 1 )
				assertIdentityAndValues( firstValue, firstDto, item );
			else
				assertEqualValues( secondDto, item );
		}
	}

	@Test
	public void testUpdateCollectionWithMatcherWillRemoveItemNotPresentInSource() {
		ValueDto firstDto = new ValueDto( 1, "Updated First" );
		ValueDtoList dtoList = new ValueDtoList( firstDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		FieldValueSet valueSet = new FieldValueSet( secondValue, firstValue );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 1, valueSet.size() );
		Iterator<Value> updated = valueSet.iterator();
		assertIdentityAndValues( firstValue, firstDto, updated.next() );
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

	@Test
	public void testUpdateListWillNotRemoveItemsNotPresentInSourceIfRemoveOrphansIsFalse() {
		ValueDto firstDto = new ValueDto( 1, "Still First" );
		ValueDto secondDto = new ValueDto( 3, "Was Third, Now Second" );
		ValueDtoList dtoList = new ValueDtoList( firstDto, secondDto );

		Value firstValue = new Value( 1, "First" );
		Value secondValue = new Value( 2, "Second" );
		RetainValueList valueList = new RetainValueList( firstValue,
				secondValue,
				new Value( 3, "Third" ) );
		Update.from( dtoList ).to( valueList );

		assertEquals(
				"Update should not have removed a value from the destination list.",
				3, valueList.size() );

		assertIdentityAndValues( firstValue, firstDto, valueList.get( 0 ) );
		assertIdentityAndValues( secondValue, secondDto, valueList.get( 1 ) );
	}

	@Test
	public void testUpdateCollectionWithMatcherWillNotRemoveItemNotPresentInSourceIfRemoveOrphansIsFalse() {
		ValueDtoList dtoList = new ValueDtoList( new ValueDto( 1,
				"Updated First" ), new ValueDto( 3, "New Third" ) );
		RetainValueSet valueSet = new RetainValueSet( new Value( 2, "Second" ),
				new Value( 1, "First" ) );
		Update.from( dtoList ).to( valueSet );

		assertEquals( 3, valueSet.size() );
	}

}
