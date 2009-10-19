package com.codiform.moo.cache;

import com.codiform.moo.translator.ArrayTranslator;
import com.codiform.moo.translator.CollectionTranslator;
import com.codiform.moo.translator.Translator;

/**
 * A source from which translators may be retrieved.
 */
public interface TranslatorCache {

	<T> Translator<T> getTranslator(Class<T> destinationClass);

	CollectionTranslator getCollectionTranslator();

	/**
	 * Performs defensive copies and translations on arrays.
	 * 
	 * @return the array translator
	 */
	ArrayTranslator getArrayTranslator();

}
