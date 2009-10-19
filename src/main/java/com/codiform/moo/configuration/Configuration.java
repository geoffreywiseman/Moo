package com.codiform.moo.configuration;

import com.codiform.moo.source.TranslatorSource;
import com.codiform.moo.translator.ArrayTranslator;
import com.codiform.moo.translator.CollectionTranslator;
import com.codiform.moo.translator.Translator;

/**
 * Represents a configuration of Moo; this can contain no information, at which
 * point Moo work from convention and use reflection, or it can use outside
 * configuration, which is of particular merit when you'd like to do complex
 * object mapping without marking up the objects you intend to map.
 */
public class Configuration implements TranslatorSource {

	private CollectionTranslator collectionTranslator;
	private ArrayTranslator arrayTranslator;

	public Configuration() {
		collectionTranslator = new CollectionTranslator();
		arrayTranslator = new ArrayTranslator();
	}

	@Override
	public <T> Translator<T> getTranslator(Class<T> destinationClass) {
		return new Translator<T>(destinationClass, arrayTranslator,
				collectionTranslator);
	}

	@Override
	public CollectionTranslator getCollectionTranslator() {
		return collectionTranslator;
	}

	@Override
	public ArrayTranslator getArrayTranslator() {
		return arrayTranslator;
	}

}
