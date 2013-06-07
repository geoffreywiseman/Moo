package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.codiform.moo.annotation.CollectionProperty;
import com.codiform.moo.curry.Translate;

public class EnumCollectionPropertyTranslationTest {

	@Test
	public void testTranslateContainerWithCustomTranslationForContents() {
		NatoLetterContainer source = new NatoLetterContainer( NatoLetter.JULIA,
				NatoLetter.SIERRA );
		ShallowCopyDto dest = Translate.to( ShallowCopyDto.class ).from(
				source );

		assertEquals( 2, dest.getLetters().size() );
		assertTrue( dest.getLetters().contains( NatoLetter.JULIA ) );
		assertTrue( dest.getLetters().contains( NatoLetter.SIERRA ) );
	}

	@Test
	public void testTranslateContainerToStringContainer() {
		NatoLetterContainer source = new NatoLetterContainer(
				NatoLetter.NOVEMBER,
				NatoLetter.ALFA, NatoLetter.TANGO, NatoLetter.OSCAR );
		StringTranslationDto dest = Translate.to( StringTranslationDto.class ).from(
				source );

		assertEquals( 4, dest.getLetters().size() );
		assertTrue( dest.getLetters().contains( "NOVEMBER" ) );
		assertTrue( dest.getLetters().contains( "ALFA" ) );
		assertTrue( dest.getLetters().contains( "TANGO" ) );
		assertTrue( dest.getLetters().contains( "OSCAR" ) );
	}

	public static class NatoLetterContainer {

		private Set<NatoLetter> letters;

		public NatoLetterContainer() {
			letters = EnumSet.noneOf( NatoLetter.class );
		}

		public NatoLetterContainer(NatoLetter one) {
			letters = EnumSet.of( one );
		}

		public NatoLetterContainer(NatoLetter one, NatoLetter two) {
			letters = EnumSet.of( one, two );
		}

		public NatoLetterContainer(NatoLetter one, NatoLetter two,
				NatoLetter three) {
			letters = EnumSet.of( one, two, three );
		}

		public NatoLetterContainer(NatoLetter one, NatoLetter two,
				NatoLetter three, NatoLetter four) {
			letters = EnumSet.of( one, two, three, four );
		}

		public Set<NatoLetter> getLetters() {
			return letters;
		}

	}

	@SuppressWarnings("unused")
	private static class ShallowCopyDto {

		@CollectionProperty(source = "java.util.EnumSet.copyOf(this.letters)")
		private Set<NatoLetter> letters;

		public ShallowCopyDto() {
			letters = EnumSet.noneOf( NatoLetter.class );
		}

		public Set<NatoLetter> getLetters() {
			return letters;
		}
	}

	@SuppressWarnings("unused")
	private static class StringTranslationDto {

		@CollectionProperty(source = "com.codiform.moo.EnumCollectionPropertyTranslationTest$EnumUtil.toString(this.letters)")
		private Set<String> letters;

		public StringTranslationDto() {
			letters = new HashSet<String>();
		}

		public Set<String> getLetters() {
			return letters;
		}
	}

	private static enum NatoLetter {
		ALFA, BRAVO, CHARLIE, DELTA, ECHO, FOXTROT, GOLF, HOTEL, INDIA, JULIA, KILO, LIMA, MIKE, NOVEMBER, OSCAR, PAPA, QUEBEC, ROMEO, SIERRA, TANGO, UNIFORM, VICTOR, WHISKEY, XRAY, YANKEE, ZULU;
	}

	public static class EnumUtil {
		public static <E extends Enum<E>> Set<String> toString(Set<E> input) {
			HashSet<String> output = new HashSet<String>();
			for( E item : input ) {
				output.add( item.name() );
			}
			return output;
		}
	}
}
