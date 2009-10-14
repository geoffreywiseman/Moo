package com.codiform.moo.cache;

/**
 * A source for object translations, either new, or cached.
 */
public interface TranslationCache {
	
	/**
	 * Retrieves the translation of a source to a particular destination class.
	 * 
	 * @param <T>
	 * @param destination
	 * @param source
	 * @return
	 */
	public <T> T getTranslation( Class<T> destination, Object source );
	
	/**
	 * Stores the translation source to a particular destination class.
	 * 
	 * @param source
	 * @param destination
	 */
	public void putTranslation( Object source, Object destination );

}
