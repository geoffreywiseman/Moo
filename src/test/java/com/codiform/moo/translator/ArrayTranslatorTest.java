package com.codiform.moo.translator;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codiform.moo.source.TranslationSource;

@RunWith(MockitoJUnitRunner.class)
public class ArrayTranslatorTest {

	@Mock
	private TranslationSource translationSource;

	@Test
	public void testCreatesDefensiveCopies() {
		String[] source = new String[] { "The", "Quick", "Brown", "Fox" };
		String[] destination = new ArrayTranslator().defensiveCopy(source);
		Assert.assertNotNull(destination);
		Assert.assertNotSame(source, destination);
		Assert.assertTrue(Arrays.equals(source, destination));
	}

	@Test
	public void testCreatesWidenedArrayCopy() {
		String[] source = new String[] { "The", "Quick", "Brown", "Fox" };
		CharSequence[] destination = new ArrayTranslator().copyTo(source,
				CharSequence.class);
		Assert.assertNotNull(destination);
		Assert.assertNotSame(source, destination);
		Assert.assertTrue(Arrays.equals(source, destination));
		Assert.assertEquals(CharSequence.class, destination.getClass()
				.getComponentType());
	}

	@Test
	public void testCreatesTranslatedArray() {
		String[] source = new String[] { "One", "Two" };

		Mockito.when(translationSource.getTranslation("One",Integer.class)).thenReturn(1);
		Mockito.when(translationSource.getTranslation("Two",Integer.class)).thenReturn(2);

		Integer[] destination = new ArrayTranslator().translate(source, Integer.class, translationSource);
		
		Assert.assertNotNull(destination);
		Assert.assertEquals( 2, destination.length );
		Assert.assertEquals( Integer.valueOf(1), destination[0] );
		Assert.assertEquals( Integer.valueOf(2), destination[1] );
	}
}
