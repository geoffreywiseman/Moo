package com.codiform.moo.translator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.codiform.moo.TranslationException;
import com.codiform.moo.TranslationInitializationException;

/**
 * The default target factory for properties annotated with @Property, this simply instantiates
 * whatever class was defined in the destination method or field.
 */
public class DefaultMapTargetFactory implements TranslationTargetFactory {

	@Override
	public <T> T getTranslationTargetInstance( Object source, Class<T> targetType ) {
		Class<? extends T> type = getDefaultTypeForTarget( targetType );
		if ( type == null ) {
			throw new TranslationException( "Cannot determine default collection type for type: " + targetType );
		} else {
			return construct( type );
		}
	}

	@SuppressWarnings( "unchecked" )
	private <T> Class<? extends T> getDefaultTypeForTarget( Class<T> target ) {
		if ( target.isInterface() ) {
			if ( SortedMap.class.equals( target ) )
				return (Class<? extends T>)TreeMap.class;
			else if ( Map.class.equals( target ) )
				return (Class<? extends T>)HashMap.class;
			else
				return null;
		} else if ( Map.class.isAssignableFrom( target ) ) {
			return target;
		} else {
			return null;
		}
	}

	private <T> T construct( Class<T> type ) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor();
			constructor.setAccessible( true );
			return constructor.newInstance();
		} catch ( NoSuchMethodException exception ) {
			throw new TranslationInitializationException( "No no-argument constructor in class " + type.getName(), exception );
		} catch ( InstantiationException | IllegalArgumentException exception ) {
			throw new TranslationInitializationException( String.format( "Error while instantiating %s", type ), exception );
		} catch ( IllegalAccessException exception ) {
			throw new TranslationInitializationException( String.format( "Not allowed to instantiate %s", type ), exception );
		} catch ( InvocationTargetException exception ) {
			throw new TranslationInitializationException( String.format( "Error thrown by constructor of %s", type ), exception );
		}
	}

}
