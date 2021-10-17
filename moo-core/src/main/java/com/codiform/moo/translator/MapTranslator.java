package com.codiform.moo.translator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.codiform.moo.TranslationException;
import com.codiform.moo.UnsupportedTranslationException;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.MapProperty;
import com.codiform.moo.property.source.NoOpSourceProperty;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import com.codiform.moo.session.TranslationSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapTranslator {

	private Configuration configuration;
	private Logger log;
	private SourcePropertyFactory sourcePropertyFactory;

	/**
	 * Create a map translator.
	 * 
	 * @param configuration
	 *            configuration that may be relevant to the map translator
	 * @param sourcePropertyFactory
	 *            the source property factory used to retrieve source properties
	 */
	public MapTranslator( Configuration configuration, SourcePropertyFactory sourcePropertyFactory ) {
		this.configuration = configuration;
		this.sourcePropertyFactory = sourcePropertyFactory;
		this.log = LoggerFactory.getLogger( getClass() );
	}

	public Object translate( Object value, MapProperty property, TranslationSource translationSource ) {
		if ( shouldTranslate( property ) ) {
			return translateMap( value, property, translationSource );
		} else if ( shouldCopy( property ) ) {
			return copy( value, property, translationSource );
		} else {
			if ( isCompatible( value, property ) ) {
				return value;
			} else {
				return copy( value, property, translationSource );
			}
		}
	}

	@SuppressWarnings( "unchecked" )
	private Object translateMap( Object value, MapProperty property, TranslationSource translationSource ) {
		if ( value instanceof Map ) {
			Object target = createTargetMap( value, property, translationSource );
			if ( target instanceof Map ) {
				translateMap( (Map<Object, Object>)value, (Map<Object, Object>)target, property, translationSource );
				return target;
			} else {
				throw new TranslationException( "Cannot translate map to target of type: " + target.getClass().getName() );
			}
		} else {
			throw new TranslationException( "Cannot translate map from type: " + value.getClass().getName() );
		}
	}

	private void translateMap( Map<Object, Object> source, Map<Object, Object> target, MapProperty property, TranslationSource translationSource ) {
		SourceProperty keySourceProperty = getSourceProperty( property.getKeySource() );
		SourceProperty valueSourceProperty = getSourceProperty( property.getValueSource() );
		for ( Map.Entry<Object, Object> entry : source.entrySet() ) {
			Object key, value;

			key = keySourceProperty.getValue( entry.getKey() );
			if ( key == null && !property.allowNullKeys() )
				continue;

			key = getKeyOrTranslation( key, property, translationSource );
			if ( key == null && !property.allowNullKeys() )
				continue;

			value = valueSourceProperty.getValue( entry.getValue() );
			value = getValueOrTranslation( value, property, translationSource );
			target.put( key, value );
		}
	}

	private Object getValueOrTranslation( Object value, MapProperty property, TranslationSource translationSource ) {
		if ( property.getValueClass() == null )
			return value;
		else
			return translationSource.getTranslation( value, property.getValueClass() );
	}

	private Object getKeyOrTranslation( Object key, MapProperty property, TranslationSource translationSource ) {
		if ( property.getKeyClass() == null )
			return key;
		else
			return translationSource.getTranslation( key, property.getKeyClass() );
	}

	@SuppressWarnings( "unchecked" )
	private Object copy( Object value, MapProperty property, TranslationSource translationSource ) {
		if ( value instanceof Map ) {
			Object target = createTargetMap( value, property, translationSource );
			if ( target instanceof Map ) {
				Map<Object, Object> targetMap = (Map<Object, Object>)target;
				targetMap.putAll( (Map<Object, Object>)value );
				return targetMap;
			} else {
				throw new TranslationException( "Cannot translate Map to target of type: " + target.getClass().getName() );
			}
		} else {
			throw new TranslationException( "Cannot translate collection from type: " + value.getClass().getName() );
		}
	}

	private Object createTargetMap( Object value, MapProperty property, TranslationSource cache ) {
		Class<? extends TranslationTargetFactory> factoryType = property.getFactory();
		TranslationTargetFactory factory = cache.getTranslationTargetFactory( factoryType );
		Object targetMap = factory.getTranslationTargetInstance( value, property.getType() );
		log.trace( "Target factory type {} created target map of type {} for source {}", factoryType, targetMap.getClass(), value );
		return targetMap;
	}

	private boolean isCompatible( Object value, MapProperty property ) {
		return property.getType().isInstance( value );
	}

	private boolean shouldCopy( MapProperty property ) {
		return !hasDefaultFactory( property ) || configuration.isPerformingDefensiveCopies();
	}

	private boolean hasDefaultFactory( MapProperty property ) {
		return property.getFactory() == DefaultMapTargetFactory.class;
	}

	private boolean shouldTranslate( MapProperty property ) {
		return property.getKeyClass() != null || property.getValueClass() != null || property.getKeySource() != null
				|| property.getValueSource() != null;
	}

	@SuppressWarnings( "unchecked" )
	public void updateMap( Object source, Map<Object, Object> destinationMap, TranslationSource translationSource, MapProperty property ) {
		if ( source instanceof Map ) {
			Map<Object, Object> sourceMap = (Map<Object, Object>)source;
			updateMapByKey( sourceMap, destinationMap, translationSource, property );
		} else {
			throw new UnsupportedTranslationException( "Cannot update Map from " + source.getClass().getName() );
		}
	}

	private void updateMapByKey( Map<Object, Object> sourceMap, Map<Object, Object> destinationMap, TranslationSource translationSource,
			MapProperty property ) {
		SourceProperty keySourceProperty = getSourceProperty( property.getKeySource() );
		SourceProperty valueSourceProperty = getSourceProperty( property.getValueSource() );

		for ( Map.Entry<Object, Object> entry : sourceMap.entrySet() ) {
			Object key = entry.getKey();
			key = keySourceProperty.getValue( key );
			if ( key == null && !property.allowNullKeys() )
				continue;

			key = getKeyOrTranslation( key, property, translationSource );
			if ( key == null && !property.allowNullKeys() )
				continue;

			Object sourceValue = entry.getValue();
			sourceValue = valueSourceProperty.getValue( sourceValue );
			sourceValue = getValueOrTranslation( sourceValue, property, translationSource );

			Object destinationValue = destinationMap.get( key );
			if ( destinationValue != null && sourceValue != null ) {
				translationSource.update( sourceValue, destinationValue );
			} else {
				destinationMap.put( key, sourceValue );
			}
		}

		if ( property.shouldRemoveOrphans() ) {
			removeOrphans( sourceMap, destinationMap );
		}
	}

	private void removeOrphans( Map<Object, Object> sourceMap, Map<Object, Object> destinationMap ) {
		Set<Object> toRemove = new HashSet<Object>( destinationMap.keySet() );
		toRemove.removeAll( sourceMap.keySet() );
		for ( Object key : toRemove ) {
			destinationMap.remove( key );
		}
	}

	private SourceProperty getSourceProperty( String sourceExpression ) {
		if ( sourceExpression == null )
			return new NoOpSourceProperty();
		else
			return sourcePropertyFactory.getSourceProperty( sourceExpression );
	}

}
