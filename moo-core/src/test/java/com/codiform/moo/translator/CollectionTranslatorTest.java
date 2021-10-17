package com.codiform.moo.translator;

import java.util.*;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.CollectionProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import com.codiform.moo.session.TranslationSession;
import com.codiform.moo.session.TranslationSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class CollectionTranslatorTest {

	@Mock
	private SourcePropertyFactory sourcePropertyFactory;

	@Mock
	private CollectionProperty property;

	private Configuration configuration = new Configuration();
	
	private CollectionTranslator translator = new CollectionTranslator( configuration, sourcePropertyFactory );

	private TranslationSource session = new TranslationSession();

	@Before
	public void setUp() {
		Mockito.doReturn( DefaultCollectionTargetFactory.class ).when( property ).getFactory();
	}

	@SuppressWarnings( "unchecked" )
	@Test
	public void testCollectionTranslatorPerformsDefensiveCopyByDefault() {
		Set<String> rhyme = new HashSet<>();
		rhyme.add( "one" );
		rhyme.add( "two" );
		rhyme.add( "buckle my shoe" );

		Mockito.doReturn( Set.class ).when( property ).getType();

		Set<String> translated = (Set<String>)translator.translate( rhyme, property, session );

		Assert.assertNotNull( translated );
		Assert.assertEquals( rhyme, translated );
		Assert.assertNotSame( rhyme, translated );
	}

	@SuppressWarnings( "unchecked" )
	@Test
	public void testCollectionTranslatorRetainsSortWhenTranslating() {
		SortedSet<String> rhyme = new TreeSet<>();
		rhyme.add( "Zed" );
		rhyme.add( "Ay" );
		rhyme.add( "Pee" );

		assertOrder( rhyme, "Ay", "Pee", "Zed" );

		Mockito.doReturn( SortedSet.class ).when( property ).getType();

		SortedSet<String> translated = (SortedSet<String>)translator.translate( rhyme, property, session );

		Assert.assertNotNull( translated );
		Assert.assertEquals( rhyme, translated );
		Assert.assertNotSame( rhyme, translated );

		assertOrder( translated, "Ay", "Pee", "Zed" );

		translated.add( "Cee" );

		assertOrder( translated, "Ay", "Cee", "Pee", "Zed" );
	}

	private void assertOrder( SortedSet<String> set, String... orderedStrings ) {
		Iterator<String> iterator = set.iterator();
		for ( String item : orderedStrings ) {
			Assert.assertEquals( item, iterator.next() );
		}
	}

	@SuppressWarnings( "unchecked" )
	@Test
	public void testCollectionTranslatorCanBeConfiguredToNotPerformDefensiveCopy() {
		List<String> rhyme = new ArrayList<>();
		rhyme.add( "one" );
		rhyme.add( "two" );
		rhyme.add( "buckle my shoe" );

		configuration.setPerformingDefensiveCopies( false );

		Mockito.doReturn( List.class ).when( property ).getType();

		List<String> translated = (List<String>)translator.translate( rhyme, property, session );

		Assert.assertNotNull( translated );
		Assert.assertEquals( rhyme, translated );
		Assert.assertSame( rhyme, translated );
	}

	@SuppressWarnings( "unchecked" )
	@Test
	public void testCollectionTranslatorTranslatesContentsOfCollection() {
		Collection<Foo> rhyme = new ArrayList<>();
		rhyme.add( new Foo( "this" ) );
		rhyme.add( new Foo( "that" ) );

		when( property.shouldItemsBeTranslated() ).thenReturn( true );
		// Mockito doesn't love Class<?> return types.
		doReturn( Bar.class ).when( property ).getItemClass();
		doReturn( DefaultObjectTargetFactory.class ).when( property ).getItemFactory();
		Mockito.doReturn( Collection.class ).when( property ).getType();

		Collection<Bar> translated = (Collection<Bar>)translator.translate( rhyme, property, session );

		Assert.assertEquals( 2, translated.size() );
		Iterator<Bar> iterator = translated.iterator();
		Assert.assertEquals( "this", iterator.next().getValue() );
		Assert.assertEquals( "that", iterator.next().getValue() );
	}

	public static class Foo {
		private String value;

		public Foo( String value ) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static class Bar {
		private String value;

		public String getValue() {
			return value;
		}
	}

}
