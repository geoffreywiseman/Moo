package com.codiform.moo.cache;

import com.codiform.moo.translator.CollectionTranslator;
import com.codiform.moo.translator.Translator;

/**
 * A source from which translators may be retrieved.
 */
public interface TranslatorCache {

	<T> Translator<T> getTranslator(Class<T> destinationClass);

	CollectionTranslator getCollectionTranslator();

}
