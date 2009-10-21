package com.codiform.moo.translator;

import java.lang.reflect.Array;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.source.TranslationSource;

public class ArrayTranslator {

	private Configuration configuration;

	public ArrayTranslator(Configuration configuration) {
		this.configuration = configuration;
	}

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

	/**
	 * Perform a defensive copy (if configured to do so).
	 * 
	 * @param <T>
	 *            a type to link the resulting array to the supplied array
	 * @param value
	 *            the original array
	 * @return the copy, or the original array, depending on the configuration
	 * @see Configuration#setPerformingDefensiveCopies(boolean)
	 * @see Configuration#isPerformingDefensiveCopies()
	 */
	public <T> T[] defensiveCopy(T[] value) {
		if (configuration.isPerformingDefensiveCopies()) {
			return value.clone();
		} else {
			return value;
		}
	}

	public <T> T[] copyTo(Object[] sourceArray, Class<T> destinationClass) {
		T[] destinationArray = createDestinationArray(destinationClass,
				sourceArray.length);
		copy(sourceArray, destinationArray);
		return destinationArray;
	}
}
