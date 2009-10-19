package com.codiform.moo.source;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A source for object translations, either new, or cached.
 */
public interface TranslationSource {
	
	/**
	 * Retrieves the translation of a source to a particular destination class, either a 
	 * cached instance or a newly-translated version.
	 * 
	 * @param source
	 * @param destination
	 * 
	 * @param <T>
	 * @return
	 */
	public <T> T getTranslation( Object source, Class<T> destination );
	
	public <T> List<T> getEachTranslation( List<?> source, Class<T> destinationClass );
	
	public <T> Set<T> getEachTranslation( Set<?> source, Class<T> destinationClass );
	
	public <T> Collection<T> getEachTranslation( Collection<?> source, Class<T> destinationClass );
	
}
