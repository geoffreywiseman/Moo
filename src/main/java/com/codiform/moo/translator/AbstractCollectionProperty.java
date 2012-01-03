package com.codiform.moo.translator;

import com.codiform.moo.annotation.MatchWith;
import com.codiform.moo.annotation.TranslateCollection;

public abstract class AbstractCollectionProperty extends AbstractProperty implements
		CollectionProperty {

	@Override
	public Class<?> getItemTranslationType() {
		TranslateCollection annotation = getAnnotation( TranslateCollection.class );
		return annotation == null ? null : annotation.value();
	}

	@Override
	public boolean hasMatcher() {
		MatchWith annotation = getAnnotation( MatchWith.class );
		return annotation != null;
	}

	@Override
	public boolean shouldItemsBeTranslated() {
		TranslateCollection annotation = getAnnotation( TranslateCollection.class );
		return annotation != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<CollectionMatcher<Object, Object>> getMatcherClass() {
		MatchWith annotation = getAnnotation( MatchWith.class );
		if( annotation == null ) {
			return null;
		} else {
			Class<? extends CollectionMatcher<?, ?>> matcher = annotation.value();
			return (Class<CollectionMatcher<Object, Object>>) matcher;
		}
	}


}
