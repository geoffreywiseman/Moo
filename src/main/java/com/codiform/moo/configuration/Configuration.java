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
	private boolean performingDefensiveCopies = true;
	
	/**
	 * Creates a default configuration.
	 */
	public Configuration() {
		collectionTranslator = new CollectionTranslator(this);
		arrayTranslator = new ArrayTranslator(this);
	}
	
	/**
	 * Used to control whether or not arrays and collections should be copied as a defensive
	 * measure. 
	 * 
	 * <p>This defaults to true.  If you know that you will not modify the array
	 * or collection (e.g. if you're going to serialize it right away) this might
	 * introduce a small amount of additional performance overhead which you
	 * can remove by disabling.</p>
	 * 
	 * @param performingDefensiveCopies
	 * @see #isPerformingDefensiveCopies()
	 */
	public void setPerformingDefensiveCopies(boolean performingDefensiveCopies ) {
		this.performingDefensiveCopies = performingDefensiveCopies;
	}
	
	/**
	 * Indicates if arrays and collections should be copied as a defensive
	 * measure to ensure that the source arrays and collections aren't accidentally
	 * modified by modifying the arrays and collections in the translation.
	 * 
	 * @see #setPerformingDefensiveCopies(boolean)
	 */
	public boolean isPerformingDefensiveCopies() {
		return this.performingDefensiveCopies;
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
