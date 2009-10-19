package com.codiform.moo.translator;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.cache.TranslationCache;
import com.codiform.moo.cache.TranslatorCache;

@RunWith(MockitoJUnitRunner.class)
public class ArrayTranslatorTest {

	@Mock
	private TranslatorCache translatorCache;

	@Mock
	private TranslationCache translationCache;

	@Test
	public void testCreatesDefensiveCopies() {
		String[] source = new String[] { "The", "Quick", "Brown", "Fox" };
		String[] destination = new ArrayTranslator(translatorCache).defensiveCopy(source);
		Assert.assertNotNull(destination);
		Assert.assertNotSame(source, destination);
		Assert.assertTrue(Arrays.equals(source, destination));
	}

	@Test
	public void testCreatesWidenedArrayCopy() {
		String[] source = new String[] { "The", "Quick", "Brown", "Fox" };
		CharSequence[] destination = new ArrayTranslator(translatorCache).copyTo(source,
				CharSequence.class);
		Assert.assertNotNull(destination);
		Assert.assertNotSame(source, destination);
		Assert.assertTrue(Arrays.equals(source, destination));
		Assert.assertEquals(CharSequence.class, destination.getClass()
				.getComponentType());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreatesTranslatedArray() {
		String[] source = new String[] { "One", "Two" };

		Translator<Integer> translator = Mockito.mock(Translator.class);
		Mockito.when(translatorCache.getTranslator(Integer.class))
				.thenReturn(translator);
		Mockito.when(translator.getTranslation("One", translationCache)).thenReturn(1);
		Mockito.when(translator.getTranslation("Two", translationCache)).thenReturn(2);

		Integer[] destination = new ArrayTranslator(translatorCache).translate(source, Integer.class, translationCache);
		
		Assert.assertNotNull(destination);
		Assert.assertEquals( 2, destination.length );
		Assert.assertEquals( Integer.valueOf(1), destination[0] );
		Assert.assertEquals( Integer.valueOf(2), destination[1] );
	}
}
