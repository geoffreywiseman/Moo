package com.codiform.moo.translator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.MissingSourcePropertyException;
import com.codiform.moo.MissingSourcePropertyValueException;
import com.codiform.moo.NoSourceException;
import com.codiform.moo.NothingToTranslateException;
import com.codiform.moo.UnsupportedTranslationException;
import com.codiform.moo.annotation.Access;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.CollectionProperty;
import com.codiform.moo.property.Property;
import com.codiform.moo.property.PropertyFactory;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.session.TranslationSource;

/**
 * The workhorse class of Moo that does the actual work of creating and populating translated
 * instances.
 * 
 * @param <T>
 *            the destination type for the translator, the type to which all source objects will be
 *            translated
 */
public class ObjectTranslator<T> {

	private Class<T> destinationClass;

	private Configuration configuration;

	/**
	 * Create a translator that will translate objects to the specified destination type, using the
	 * specified configuration.
	 * 
	 * @param destination
	 *            the destination type
	 * @param configuration
	 *            the configuration used during translation
	 */
	public ObjectTranslator( Class<T> destination, Configuration configuration ) {
		this.destinationClass = destination;
		this.configuration = configuration;
	}

	/**
	 * Update a destination instance from the source instance. This is the actual transfer of
	 * property values from source to destination.
	 * 
	 * @param source
	 *            the object from which the property values will be retrieved
	 * @param destination
	 *            the object to which the property values will be stored
	 * @param translationSource
	 *            if any of the sub-properties need to be translated, this will provide those
	 *            translations
	 */
	public void update( Object source, T destination, TranslationSource translationSource, Map<String, Object> variables ) {
		assureSource( source );
		boolean updated = false;
		List<Property> properties = getProperties( destinationClass );
		for ( Property item : properties ) {
			if ( !item.isIgnored() ) {
				if ( updateProperty( source, destination, translationSource, item, variables ) ) {
					updated = true;
				}
			}
		}
		if ( updated == false ) {
			throw new NothingToTranslateException( source.getClass(), destination.getClass() );
		}
	}

	private void assureSource( Object source ) {
		if ( source == null ) {
			throw new NoSourceException();
		}
	}

	/**
	 * It seems like this shouldn't be necessary, but ... sometimes generics defeats me. If anyone
	 * can figure out how to remove this method, send me a patch.
	 * 
	 * @see #update
	 */
	public void castAndUpdate( Object source, Object from, TranslationSource translationSource, Map<String, Object> variables ) {
		update( source, destinationClass.cast( from ), translationSource, variables );
	}

	private Object getPropertyValueTranslation( Object value, Property property, TranslationSource translationSource ) {
		if ( value == null ) {
			return null;
		} else if ( property instanceof CollectionProperty ) {
			return transformCollection( value, (CollectionProperty)property, translationSource );
		} else if ( value.getClass().isArray() ) {
			return transformArray( (Object[])value, property, translationSource );
		} else if ( property.shouldBeTranslated() ) {
			return translationSource.getTranslation( value, property.getFactory(), property.getType() );
		} else {
			return value;
		}
	}

	private Object transformArray( Object[] value, Property property, TranslationSource translationSource ) {
		Class<?> fieldType = property.getType();
		Class<?> valueType = value.getClass();

		if ( valueType.isAssignableFrom( fieldType ) ) {
			return configuration.getArrayTranslator().defensiveCopy( value );
		} else if ( fieldType.isArray() ) {
			if ( valueType.isAssignableFrom( fieldType.getComponentType() ) ) {
				return configuration.getArrayTranslator().copyTo( value, fieldType );
			} else {
				return configuration.getArrayTranslator().translate( value, fieldType.getComponentType(), translationSource );
			}
		} else {
			throw new UnsupportedTranslationException( String.format( "Cannot translate from source array type %s[] to destination type %s",
					valueType.getComponentType(), fieldType.getName() ) );
		}
	}

	private Object transformCollection( Object value, CollectionProperty property, TranslationSource translationSource ) {
		return configuration.getCollectionTranslator().translate( value, property, translationSource );
	}

	private Object getValue( Object source, Property property, Map<String, Object> variables ) {
		SourceProperty origin = configuration.getSourceProperty( property );
		if ( variables == null || variables.isEmpty() ) {
			return origin.getValue( source );
		} else {
			return origin.getValue( source, variables );
		}
	}

	/* package */List<Property> getProperties( Class<T> destinationClass ) {
		Map<String, Property> properties = new LinkedHashMap<String, Property>();
		Class<?> current = destinationClass;
		while ( current != null ) {
			if ( !shouldIgnoreClass( current ) ) {
				merge( properties, getPropertiesForClass( current ) );
			}
			current = current.getSuperclass();
		}
		return getOrderedProperties( properties );
	}

	/**
	 * LinkedHashMap will ensure iteration is in insertion order, but because we're reflecting from
	 * bottom up, and we want the properties in superclass to subclass order, we need to reverse
	 * that.
	 * 
	 * @param properties
	 *            the {@link LinkedHashMap} containing the properties in insertion order
	 * @return a list of properties in reversed order
	 */
	private List<Property> getOrderedProperties( Map<String, Property> properties ) {
		List<Property> ordered = new ArrayList<Property>( properties.size() );
		for ( Map.Entry<String, Property> entry : properties.entrySet() ) {
			ordered.add( 0, entry.getValue() );
		}
		return ordered;
	}

	private boolean shouldIgnoreClass( Class<?> current ) {
		return current.getSimpleName().contains( "$$_javassist" );
	}

	private void merge( Map<String, Property> currentProperties, Set<Property> superclassProperties ) {
		for ( Property item : superclassProperties ) {
			if ( currentProperties.containsKey( item.getName() ) ) {
				if ( item.isExplicit() ) {
					if ( !currentProperties.get( item.getName() ).isExplicit() ) {
						// ensure insertion ordering by removing first
						currentProperties.remove( item.getName() );
						currentProperties.put( item.getName(), item );
					}
				}
			} else {
				currentProperties.put( item.getName(), item );
			}
		}
	}

	private Set<Property> getPropertiesForClass( Class<?> clazz ) {
		Map<String, Property> properties = new HashMap<String, Property>();
		Access access = clazz.getAnnotation( Access.class );
		AccessMode mode = access == null ? configuration.getDefaultAccessMode() : access.value();
		for ( Field item : clazz.getDeclaredFields() ) {
			Property property = PropertyFactory.createProperty( item, mode );
			if ( property != null ) {
				properties.put( property.getName(), property );
			}
		}
		for ( Method item : clazz.getDeclaredMethods() ) {
			Property property = PropertyFactory.createProperty( item, mode );
			if ( property != null ) {
				if ( properties.containsKey( property.getName() ) ) {
					Property current = properties.get( property.getName() );
					if ( current.isExplicit() && property.isExplicit() ) {
						throw new InvalidPropertyException(
								property.getName(),
								property.getDeclaringClass(),
								"Property %s (in %s) is explicitly defined with @Property as both field and method properties; Moo expects no more than one annotation per property name per class." );
					} else if ( !current.isExplicit() && property.isExplicit() ) {
						properties.put( property.getName(), property );
					}
				} else {
					properties.put( property.getName(), property );
				}
			}
		}
		return new HashSet<Property>( properties.values() );
	}

	private <V> boolean updateProperty( Object source, T destination, TranslationSource translationSource, Property property,
			Map<String, Object> variables ) {
		try {
			Object sourceValue = getValue( source, property, variables );
			updateOrReplaceProperty( destination, sourceValue, property, translationSource );
			return true;
		} catch ( MissingSourcePropertyValueException exception ) {
			if ( property.isSourceRequired( configuration.isSourcePropertyRequired() ) ) {
				throw exception;
			}
			return false;
		} catch ( MissingSourcePropertyException exception ) {
			if ( property.isSourceRequired( configuration.isSourcePropertyRequired() ) ) {
				throw exception;
			}
			return false;
		}
	}

	@SuppressWarnings( "unchecked" )
	private void updateOrReplaceProperty( T destination, Object sourceValue, Property property, TranslationSource translationSource ) {
		Object destinationValue = property.canGetValue() ? property.getValue( destination ) : null;
		if ( property.shouldUpdate() && sourceValue != null && destinationValue != null ) {
			if ( property.isTypeOrSubtype( Collection.class ) ) {
				updateCollection( sourceValue, (Collection<Object>)destinationValue, (CollectionProperty)property, translationSource );
			} else if ( property.isTypeOrSubtype( Map.class ) ) {
				updateMap( sourceValue, (Map<Object, Object>)destinationValue, (CollectionProperty)property, translationSource );
			} else {
				translationSource.update( sourceValue, destinationValue );
			}
		} else {
			destinationValue = getPropertyValueTranslation( sourceValue, property, translationSource );
			property.setValue( destination, destinationValue );
		}
	}

	private void updateMap( Object source, Map<Object, Object> destinationMap, CollectionProperty property, TranslationSource translationSource ) {
		configuration.getCollectionTranslator().updateMap( source, destinationMap, translationSource, property );

	}

	private void updateCollection( Object source, Collection<Object> destinationCollection, CollectionProperty property,
			TranslationSource translationSource ) {
		configuration.getCollectionTranslator().updateCollection( source, destinationCollection, translationSource, property );
	}
}
