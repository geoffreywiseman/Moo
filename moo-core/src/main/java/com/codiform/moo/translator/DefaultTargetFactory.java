package com.codiform.moo.translator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.codiform.moo.TranslationInitializationException;

/**
 * The default target factory for properties annotated with @Property, this simply
 * instantiates whatever class was defined in the destination method or field.
 */
public class DefaultTargetFactory implements TranslationTargetFactory {
	
	@Override
	public <T> T getTranslationTargetInstance( Object source, Class<T> targetType ) {
		try {
			Constructor<T> constructor = targetType.getDeclaredConstructor();
			constructor.setAccessible( true );
			return constructor.newInstance();
		} catch ( NoSuchMethodException exception ) {
			throw new TranslationInitializationException( "No no-argument constructor in class " + targetType.getName(), exception );
		} catch ( InstantiationException exception ) {
			throw new TranslationInitializationException( String.format( "Error while instantiating %s", targetType ), exception );
		} catch ( IllegalAccessException exception ) {
			throw new TranslationInitializationException( String.format( "Not allowed to instantiate %s", targetType ), exception );
		} catch ( IllegalArgumentException exception ) {
			throw new TranslationInitializationException( String.format( "Error while instantiating %s", targetType ), exception );
		} catch ( InvocationTargetException exception ) {
			throw new TranslationInitializationException( String.format( "Error thrown by constructor of %s", targetType ), exception );
		}
	}

	
}
