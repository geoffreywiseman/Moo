package com.codiform.moo.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codiform.moo.MatcherInitializationException;
import com.codiform.moo.TranslationException;
import com.codiform.moo.UnsupportedTranslationException;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.CollectionProperty;
import com.codiform.moo.property.source.NoOpSourceProperty;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import com.codiform.moo.session.TranslationSource;

/**
 * A translator for handling issues specific to collections. In particular, this handles defensive
 * copying of collections that don't need to be transformed, but also deals with making a copy of a
 * collection that matches the original collection type (set, list, etc.) but where each item in the
 * collection has been translated.
 * 
 * <p>
 * This approach has limitations; it can't change the type of the collection, and it can't currently
 * handle sorted sets with translation (where does the comparator for the translated objects come
 * from?), maps, or allow you to select the implementation class for the collection (LinkedList vs.
 * ArrayList).
 * </p>
 */
public class CollectionTranslator {

	private Configuration configuration;
	private Logger log;
	private SourcePropertyFactory sourcePropertyFactory;

	/**
	 * Create a collection translator with a known configuration.
	 * 
	 * @param configuration
	 */
	public CollectionTranslator( Configuration configuration, SourcePropertyFactory sourcePropertyFactory ) {
		this.configuration = configuration;
		this.sourcePropertyFactory = sourcePropertyFactory;
		this.log = LoggerFactory.getLogger( getClass() );
	}

	/**
	 * Translate a collection. Make a defensive copy if need be. If the element is annotated with
	 * the TranslateCollection element, then create an entirely new collection and translate each
	 * element of the contents of the source collection.
	 * 
	 * @param value
	 *            the source collection
	 * @param annotation
	 *            the {@link CollectionProperty} annotation, if present
	 * @param cache
	 *            the translation cache of previously-translated elements
	 * @return the translated collection
	 */
	public Object translate( Object value, CollectionProperty property, TranslationSource cache ) {
		if ( property.shouldItemsBeTranslated() ) {
			Object target = createTargetCollection( value, property, cache );
			translateToTargetCollection( value, target, property, cache );
			return target;
		} else if ( shouldCopy( value, property ) ) {
			Object target = createTargetCollection( value, property, cache );
			copyToTargetCollection( value, target, property );
			return target;
		} else {
			return value;
		}
	}

	private boolean shouldCopy( Object value, CollectionProperty property ) {
		if( !hasDefaultFactory( property ) ) {
			return true;
		}
		if( configuration.isPerformingDefensiveCopies() ) {
			return true;
		}
		if( property.getItemExpression() != null ) {
			return true;
		}
		Class<?> targetClass = property.getType();
		if ( !targetClass.isInstance( value ) ) {
			return true;
		}
		return false;
	}

	@SuppressWarnings( "unchecked" )
	private void copyToTargetCollection( Object value, Object target, CollectionProperty property ) {
		if ( value instanceof Collection ) {
			if ( target instanceof Collection ) {
				Collection<Object> targetCollection = (Collection<Object>)target;
				Iterator<?> sourceItems = ( (Collection<?>)value ).iterator();
				SourceProperty itemSource = getItemSource( property.getItemExpression() );
				while ( sourceItems.hasNext() ) {
					Object item = itemSource.getValue( sourceItems.next() );
					targetCollection.add( item );
				}
			} else {
				throw new TranslationException( "Cannot translate collection to target of type: " + target.getClass().getName() );
			}
		} else {
			throw new TranslationException( "Cannot translate collection from type: " + value.getClass().getName() );
		}
	}

	private boolean hasDefaultFactory( CollectionProperty property ) {
		return property.getFactory() == DefaultCollectionTargetFactory.class;
	}

	@SuppressWarnings( "unchecked" )
	private void translateToTargetCollection( Object value, Object target, CollectionProperty property, TranslationSource cache ) {
		if ( value instanceof Collection ) {
			if ( target instanceof Collection ) {
				Collection<Object> targetCollection = (Collection<Object>)target;
				Iterator<?> sourceItems = ( (Collection<?>)value ).iterator();
				SourceProperty itemSource = getItemSource( property.getItemExpression() );
				while ( sourceItems.hasNext() ) {
					Object item = itemSource.getValue( sourceItems.next() );
					Object translated = cache.getTranslation( item, property.getItemClass() );
					targetCollection.add( translated );
				}
			} else {
				throw new TranslationException( "Cannot translate collection to target of type: " + target.getClass().getName() );
			}
		} else {
			throw new TranslationException( "Cannot translate from collection of type: " + target.getClass().getName() );
		}
	}

	private SourceProperty getItemSource( String itemExpression ) {
		if( itemExpression == null )
			return new NoOpSourceProperty();
		else
			return sourcePropertyFactory.getSourceProperty( itemExpression ); 
	}

	private Object createTargetCollection( Object value, CollectionProperty property, TranslationSource cache ) {
		Class<? extends TranslationTargetFactory> factoryType = property.getFactory();
		TranslationTargetFactory factory = cache.getTranslationTargetFactory( factoryType );
		Object targetCollection = factory.getTranslationTargetInstance( value, property.getType() );
		log.trace( "Target factory type {} created target collection of type {} for source {}", factoryType, targetCollection.getClass(), value );
		return targetCollection;
	}

	@SuppressWarnings( "unchecked" )
	public void updateCollection( Object source, Collection<Object> destinationCollection, TranslationSource translationSource,
			CollectionProperty property ) {
		if ( source instanceof Collection ) {
			Collection<Object> sourceCollection = (Collection<Object>)source;
			if ( property.hasMatcher() ) {
				updateCollectionWithMatcher( sourceCollection, destinationCollection, translationSource, property );
			} else {
				updateCollectionInOrder( sourceCollection, destinationCollection, translationSource, property );
			}
		} else {
			throw new UnsupportedTranslationException( "Cannot update Collection from " + source.getClass().getName() );
		}
	}

	private void updateCollectionWithMatcher( Collection<Object> sourceCollection, Collection<Object> destinationCollection,
			TranslationSource translationSource, CollectionProperty property ) {
		Class<CollectionMatcher<Object, Object>> matcherClass = property.getMatcherType();
		try {
			Collection<Object> unmatched = new ArrayList<Object>( destinationCollection );
			CollectionMatcher<Object, Object> matcher = matcherClass.newInstance();
			matcher.setTargets( destinationCollection );
			for ( Object source : sourceCollection ) {
				Object destination = matcher.getTarget( source );
				if ( destination == null ) {
					if ( property.shouldItemsBeTranslated() ) {
						destinationCollection.add( translationSource.getTranslation( source, property.getItemClass() ) );
					} else {
						destinationCollection.add( source );
					}
				} else {
					unmatched.remove( destination );
					translationSource.update( source, destination );
				}
			}
			if ( property.shouldRemoveOrphans() ) {
				for ( Object item : unmatched ) {
					destinationCollection.remove( item );
				}
			}
		} catch ( InstantiationException exception ) {
			throw new MatcherInitializationException( matcherClass, exception );
		} catch ( IllegalAccessException exception ) {
			throw new MatcherInitializationException( matcherClass, exception );
		}
	}

	private void updateCollectionInOrder( Collection<Object> sourceCollection, Collection<Object> destinationCollection,
			TranslationSource translationSource, CollectionProperty property ) {
		Iterator<Object> source = sourceCollection.iterator();
		Iterator<Object> destination = destinationCollection.iterator();

		while ( source.hasNext() && destination.hasNext() ) {
			translationSource.update( source.next(), destination.next() );
		}

		if ( source.hasNext() && !destination.hasNext() ) {
			while ( source.hasNext() ) {
				if ( property.shouldItemsBeTranslated() ) {
					Object translation = translationSource.getTranslation( source.next(), property.getItemClass() );
					destinationCollection.add( translation );
				} else {
					destinationCollection.add( source.next() );
				}
			}
		} else if ( destination.hasNext() && !source.hasNext() && property.shouldRemoveOrphans() ) {
			while ( destination.hasNext() ) {
				destination.next();
				destination.remove();
			}
		}
	}

}
