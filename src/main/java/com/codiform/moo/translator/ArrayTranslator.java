package com.codiform.moo.translator;

import java.lang.reflect.Array;

import com.codiform.moo.source.TranslationSource;

public class ArrayTranslator {

	public <T> T[] translate(Object[] sourceArray, Class<T> destinationClass,
			TranslationSource translationSource) {
		T[] destinationArray = createDestinationArray(destinationClass,
				sourceArray.length);
		for (int index = 0; index < sourceArray.length; index++) {
			destinationArray[index] = translationSource.getTranslation(
					sourceArray[index], destinationClass);
		}
		return destinationArray;
	}

	private void copy(Object[] sourceArray, Object[] destinationArray) {
		for (int index = 0; index < sourceArray.length; index++) {
			destinationArray[index] = sourceArray[index];
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T[] createDestinationArray(Class<T> destinationClass, int length) {
		return (T[]) Array.newInstance(destinationClass, length);
	}

	public <T> T[] defensiveCopy(T[] value) {
		return value.clone();
	}

	public <T> T[] copyTo(Object[] sourceArray, Class<T> destinationClass) {
		T[] destinationArray = createDestinationArray(destinationClass,
				sourceArray.length);
		copy(sourceArray, destinationArray);
		return destinationArray;
	}
}
