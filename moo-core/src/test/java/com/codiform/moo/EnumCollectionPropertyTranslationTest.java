package com.codiform.moo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.codiform.moo.curry.Translate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnumCollectionPropertyTranslationTest {

	@Test
	public void testTranslateContainerWithoutTranslatingContents() {
		NatoLetterContainer source = new NatoLetterContainer( NatoLetter.GOLF,
				NatoLetter.WHISKEY );
		UntranslatedDto dest = Translate.to( UntranslatedDto.class ).from(
				source );

		assertEquals( 2, dest.getLetters().size() );
		assertTrue( dest.getLetters().contains( NatoLetter.GOLF ) );
		assertTrue( dest.getLetters().contains( NatoLetter.WHISKEY ) );
	}

	@SuppressWarnings("unused")
	private static class NatoLetterContainer {

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
	private static class UntranslatedDto {
		private Set<NatoLetter> letters;

		public UntranslatedDto() {
			letters = EnumSet.noneOf( NatoLetter.class );
		}

		public Set<NatoLetter> getLetters() {
			return letters;
		}
	}

	private enum NatoLetter {
		ALFA, BRAVO, CHARLIE, DELTA, ECHO, FOXTROT, GOLF, HOTEL, INDIA, JULIA, KILO, LIMA, MIKE, NOVEMBER, OSCAR, PAPA, QUEBEC, ROMEO, SIERRA, TANGO, UNIFORM, VICTOR, WHISKEY, XRAY, YANKEE, ZULU
	}

	public static class EnumUtil {
		public static <E extends Enum<E>> Set<String> toString(Set<E> input) {
			HashSet<String> output = new HashSet<>();
			for( E item : input ) {
				output.add( item.name() );
			}
			return output;
		}
	}
}
