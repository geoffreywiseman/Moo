package com.codiform.moo.translator;

public interface CollectionProperty extends Property {
	
	/**
	 * Gets the type to which items in this collection should be translated. Without this,
	 * items in the collection will simply be copied into the destination, not translated.
	 * 
	 * @returns a class if the items should be translated indicating what they should be translated to; null if no translation required
	 * @see #shouldTranslateItems()
	 */
	Class<?> getItemTranslationType();
	
	/**
	 * Indicates if this collection property has an update matcher configured, or if items should be
	 * matched based on index.
	 * 
	 * @return true if there is a defined matcher, false if matching by index
	 * @see #shouldUpdate()
	 */
	boolean hasMatcher();

	/**
	 * Indicates if the items in the collection should be translated.
	 * 
	 * @return true if the items should be translated
	 */
	boolean shouldItemsBeTranslated();

	/**
	 * Returns the {@link CollectionMatcher} associated with this property, if there is one.
	 * 
	 * @return the matcher
	 * @see #hasMatcher()
	 */
	Class<CollectionMatcher<Object, Object>> getMatcherClass();
	
}
