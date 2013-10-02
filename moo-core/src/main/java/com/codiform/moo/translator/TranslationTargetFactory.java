package com.codiform.moo.translator;

/**
 * A class that creates instances of classes to be used for translation.
 * 
 * TranslationTargetFactory instances may be cached for re-use and should be thread-safe.
 */
public interface TranslationTargetFactory {

	/**
	 * Create an instance of a class that can be used as the destination 
	 * for translation.
	 * 
	 * @param source the source object from which translation will be performed
	 * @param targetType the destination type to which the translation target must conform
	 * @return
	 */
	<T> T getTranslationTargetInstance( Object source, Class<T> targetType );
	
}
