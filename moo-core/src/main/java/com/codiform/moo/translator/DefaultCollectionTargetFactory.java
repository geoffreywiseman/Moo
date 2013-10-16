package com.codiform.moo.translator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.codiform.moo.TranslationException;
import com.codiform.moo.TranslationInitializationException;

/**
 * The default target factory for properties annotated with @Property, this simply instantiates
 * whatever class was defined in the destination method or field.
 */
public class DefaultCollectionTargetFactory implements TranslationTargetFactory {

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
			if ( SortedSet.class.equals( target ) )
				return (Class<? extends T>)TreeSet.class;
			else if ( SortedMap.class.equals( target ) )
				return (Class<? extends T>)TreeMap.class;
			else if ( Set.class.equals( target ) )
				return (Class<? extends T>)HashSet.class;
			else if ( Map.class.equals( target ) )
				return (Class<? extends T>)HashMap.class;
			else if ( List.class.equals( target ) )
				return (Class<? extends T>)ArrayList.class;
			else if ( Collection.class.equals( target ) )
				return (Class<? extends T>)ArrayList.class;
			else
				return null;
		} else if ( Collection.class.isAssignableFrom( target ) || Map.class.isAssignableFrom( target ) ) {
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
		} catch ( InstantiationException exception ) {
			throw new TranslationInitializationException( String.format( "Error while instantiating %s", type ), exception );
		} catch ( IllegalAccessException exception ) {
			throw new TranslationInitializationException( String.format( "Not allowed to instantiate %s", type ), exception );
		} catch ( IllegalArgumentException exception ) {
			throw new TranslationInitializationException( String.format( "Error while instantiating %s", type ), exception );
		} catch ( InvocationTargetException exception ) {
			throw new TranslationInitializationException( String.format( "Error thrown by constructor of %s", type ), exception );
		}
	}

}
