package com.codiform.moo.translator;

import java.lang.reflect.Array;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.session.TranslationSource;

/**
 * For copying and/or translating arrays from one object instance to another.
 */
public class ArrayTranslator {

	private Configuration configuration;

	/**
	 * Creates an array translator based on a known configuration.
	 * 
	 * @param configuration the configuration settings for array translation
	 * @see Configuration#isPerformingDefensiveCopies()
	 */
	public ArrayTranslator(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Translate a source array to a destination class.  The translations of the objects
	 * come through the translation source, but the array creation and population is 
	 * performed by the array translator.
	 * 
	 * @param <T> the type linking the destination class to the component type of the array
	 * @param sourceArray the source array from which the instances to be translated will be retrieved
	 * @param destinationClass the destination class to which all instances should be translated
	 * @param translationSource the translation source used to retrieve translations
	 * @return an array of translated instances of the destination class
	 */
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

	/**
	 * Copy all the object instances from the source array to a new array of
	 * the destination class.  This is typically used for a widening conversion
	 * of the array from a type to one of its supertypes.
	 * 
	 * @param <T> the type linking the component type of the return array to the destination class
	 * @param sourceArray an array of objects to be copied
	 * @param destinationClass the type of the destination array
	 * @return an array of the destination type
	 */
	public <T> T[] copyTo(Object[] sourceArray, Class<T> destinationClass) {
		T[] destinationArray = createDestinationArray(destinationClass,
				sourceArray.length);
		copy(sourceArray, destinationArray);
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

}
