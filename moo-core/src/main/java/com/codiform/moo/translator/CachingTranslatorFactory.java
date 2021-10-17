package com.codiform.moo.translator;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.source.CompositeSourcePropertyFactory;
import com.codiform.moo.property.source.SourcePropertyFactory;

/**
 * A translator factory that caches the translators it is asked to supply.
 * 
 * This is not a weakly-referenced cache, so it will retain the translators as long as the factory itself is not GC'd.
 */
public class CachingTranslatorFactory implements TranslatorFactory {

	private Configuration configuration;
	private CollectionTranslator collectionTranslator;
	private MapTranslator mapTranslator;
	private Map<Class<?>, ValueTypeTranslator<?>> valueTypeTranslators;
	private ArrayTranslator arrayTranslator;
	private Map<Class<?>,ObjectTranslator<?>> translators;
	private SourcePropertyFactory sourcePropertyFactory;
	
	public CachingTranslatorFactory( ) {
		this( new Configuration(), new CompositeSourcePropertyFactory() );
	}
	
	public CachingTranslatorFactory( Configuration configuration, SourcePropertyFactory sourcePropertyFactory ) {
		this.configuration = configuration;
		collectionTranslator = new CollectionTranslator( configuration, sourcePropertyFactory );
		mapTranslator = new MapTranslator( configuration, sourcePropertyFactory );
		arrayTranslator = new ArrayTranslator( configuration );
		valueTypeTranslators = new HashMap<>();
		this.sourcePropertyFactory = sourcePropertyFactory;
		this.translators = new HashMap<>();
		initializeValueTypeTranslators();
	}

	private void initializeValueTypeTranslators() {
		valueTypeTranslators.put( String.class, new StringValueTypeTranslator() );
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public synchronized <T> ObjectTranslator<T> getTranslator( Class<T> destinationClass ) {
		if( translators.containsKey( destinationClass ) ) {
			return (ObjectTranslator<T>)translators.get( destinationClass );
		} else {
			ObjectTranslator<T> objectTranslator = new ObjectTranslator<>( destinationClass, configuration, this, sourcePropertyFactory );
			translators.put( destinationClass, objectTranslator );
			return objectTranslator;
		}
	}

	@Override
	public CollectionTranslator getCollectionTranslator() {
		return collectionTranslator;
	}

	@Override
	public ArrayTranslator getArrayTranslator() {
		return arrayTranslator;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <V> ValueTypeTranslator<V> getValueTypeTranslator( Class<V> destinationType ) {
		return (ValueTypeTranslator<V>)valueTypeTranslators.get( destinationType );
	}

	@Override
	public MapTranslator getMapTranslator() {
		return mapTranslator;
	}

}
