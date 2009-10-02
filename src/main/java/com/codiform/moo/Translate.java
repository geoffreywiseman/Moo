package com.codiform.moo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mvel2.MVEL;

import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.annotation.Translation;

public class Translate<T> {

	private Class<T> destinationClass;
	private IdentityHashMap<Object, Object> translationCache;

	public Translate( Class<T> destination, IdentityHashMap<Object, Object> translationCache ) {
		this.destinationClass = destination;
		this.translationCache = translationCache;
	}

	public T from( Object source ) {
		return translate( source );
	}

	public List<T> fromEach( List<?> sources ) {
		List<T> results = new ArrayList<T>();
		for ( Object source : sources ) {
			results.add( translate( source ) );
		}
		return results;
	}

	private Collection<T> fromEach( Collection<?> sources ) {
		List<T> results = new ArrayList<T>();
		for ( Object source : sources ) {
			results.add( translate( source ) );
		}
		return results;
	}

	public Set<T> fromEach( Set<?> sources ) {
		Set<T> results = new HashSet<T>();
		for ( Object source : sources ) {
			results.add( translate( source ) );
		}
		return results;
	}

	private T translate( Object source ) {
		if ( translationCache.containsKey( source ) ) {
			return destinationClass.cast( translationCache.get( source ) );
		} else {
			try {
				T destination = destinationClass.newInstance();
				translationCache.put( source, destination );
				translate( source, destination );
				return destination;
			} catch ( InstantiationException exception ) {
				throw new TranslationException( String.format( "Error while instantiating %s", destinationClass ), exception.getCause() );
			} catch ( IllegalAccessException exception ) {
				throw new TranslationException( String.format( "Not allowed to instantiate %s", destinationClass ), exception );
			}
		}
	}

	private void translate( Object source, T destination ) {
		Field[] fields = getFieldsToTranslate();
		for ( Field item : fields ) {
			String expression = getTranslationExpression( item );
			Object value = getValue( source, expression );
			value = transform( value, item );
			setValue( destination, item, value );
		}
	}

	@SuppressWarnings( "unchecked" )
	private Object transform( Object value, Field item ) {
		com.codiform.moo.annotation.Translate annotation = item
				.getAnnotation( com.codiform.moo.annotation.Translate.class );
		if ( value instanceof Collection ) {
			if ( annotation != null ) {
				throw new TranslationException( "Cannot use @Translate on a collection, use @TranslateCollection instead." );
			} else {
				return copyCollection( value, item.getAnnotation( TranslateCollection.class ) );
			}
		} else if ( annotation != null ) {
			return Translate.to( item.getType(), translationCache ).from( value );
		} else {
			return value;
		}
	}

	@SuppressWarnings( "unchecked" )
	private Object copyCollection( Object value, TranslateCollection annotation ) {
		if ( value instanceof SortedSet<?> ) {
			return copySortedSet( (SortedSet<?>)value, annotation );
		} else if ( value instanceof Set<?> ) {
			return copySet( (Set<?>)value, annotation );
		} else if ( value instanceof SortedMap ) {
			return copySortedMap( (SortedMap)value, annotation );
		} else if ( value instanceof Map ) {
			return copyMap( (Map)value, annotation );
		} else if ( value instanceof List ) {
			return (List)copyList( (List)value, annotation );
		} else {
			return copyCollectionAsList( (Collection)value, annotation );
		}
	}

	@SuppressWarnings( "unchecked" )
	private Collection copyCollectionAsList( Collection value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			return new ArrayList( value );
		} else {
			return Translate.to( annotation.value(), translationCache ).fromEach( value );
		}
	}

	@SuppressWarnings( "unchecked" )
	private List copyList( List value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			return new ArrayList( value );
		} else {
			return Translate.to( annotation.value(), translationCache ).fromEach( value );
		}
	}

	@SuppressWarnings( "unchecked" )
	private Map copyMap( Map value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			return new HashMap( value );
		} else {
			throw new TranslationException( "Support for translated maps not yet built." );
		}
	}

	@SuppressWarnings( "unchecked" )
	private SortedMap copySortedMap( SortedMap value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			SortedMap map = new TreeMap<Object, Object>( value.comparator() );
			map.putAll( value );
			return map;
		} else {
			throw new TranslationException( "Support for translated sorted maps not yet built" );
		}
	}

	private Set<?> copySet( Set<?> value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			return new HashSet<Object>( value );
		} else {
			return Translate.to( annotation.value(), translationCache ).fromEach( value );
		}
	}

	@SuppressWarnings( "unchecked" )
	private SortedSet<Object> copySortedSet( SortedSet<?> value, TranslateCollection annotation ) {
		if ( annotation == null ) {
			SortedSet result = new TreeSet( value.comparator() );
			result.addAll( value );
			return result;
		} else {
			throw new TranslationException( "Support for translated sorted sets not yet built" );
		}
	}

	private void setValue( T destination, Field field, Object value ) {
		try {
			field.setAccessible( true );
			field.set( destination, value );
		} catch ( IllegalArgumentException exception ) {
			throw new TranslationException( String.format( "Could not set field: %s", field.getName() ), exception );
		} catch ( IllegalAccessException exception ) {
			throw new TranslationException( String.format( "Could not set field: %s", field.getName() ), exception );
		}
	}

	private Object getValue( Object source, String expression ) {
		return MVEL.eval( expression, source );
	}

	private String getTranslationExpression( Field item ) {
		Translation translation = item.getAnnotation( Translation.class );
		if ( translation == null || translation.value() == null )
			return item.getName();
		else
			return translation.value();
	}

	private Field[] getFieldsToTranslate() {
		// for now -- superclass, exclusions, cache, etc.
		Field[] fields = destinationClass.getDeclaredFields();
		return fields;
	}

	public static <U> Translate<U> to( Class<U> destination ) {
		return new Translate<U>( destination, new IdentityHashMap<Object, Object>() );
	}

	public static <U> Translate<U> to( Class<U> destination, IdentityHashMap<Object, Object> translationCache ) {
		return new Translate<U>( destination, translationCache );
	}

}
