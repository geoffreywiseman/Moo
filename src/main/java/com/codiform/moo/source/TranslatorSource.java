package com.codiform.moo.source;

import com.codiform.moo.translator.ArrayTranslator;
import com.codiform.moo.translator.CollectionTranslator;
import com.codiform.moo.translator.Translator;

/**
 * A source from which translators may be retrieved.
 */
public interface TranslatorSource {

	<T> Translator<T> getTranslator(Class<T> destinationClass);

	CollectionTranslator getCollectionTranslator();

	/**
	 * Performs defensive copies and translations on arrays.
	 * 
	 * @return the array translator
	 */
	ArrayTranslator getArrayTranslator();

}
