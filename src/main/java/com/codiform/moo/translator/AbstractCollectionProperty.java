package com.codiform.moo.translator;

import com.codiform.moo.annotation.Optionality;

public abstract class AbstractCollectionProperty extends AbstractProperty
		implements
		CollectionProperty {

	private final Class<?> itemTranslation;
	private final Class<CollectionMatcher<Object, Object>> matcher;
	private final Optionality optionality;
	private final boolean removeOrphans;

	@SuppressWarnings("unchecked")
	public AbstractCollectionProperty(
			com.codiform.moo.annotation.CollectionProperty annotation) {
		if( annotation != null ) {
			itemTranslation = annotation.itemTranslation() == Object.class ? null
					: annotation.itemTranslation();
			matcher = (Class<CollectionMatcher<Object, Object>>) (annotation.matcher() == IndexMatcher.class ? null
					: annotation.matcher());
			optionality = annotation.optionality();
			removeOrphans = annotation.removeOrphans();
		} else {
			itemTranslation = null;
			matcher = null;
			optionality = null;
			removeOrphans = true;
		}
	}

	@Override
	public Class<?> getItemTranslationType() {
		return itemTranslation;
	}

	@Override
	public boolean hasMatcher() {
		return matcher != null;
	}

	@Override
	public boolean shouldItemsBeTranslated() {
		return itemTranslation != null;
	}

	@Override
	public Class<CollectionMatcher<Object, Object>> getMatcherType() {
		return matcher;
	}

	@Override
	public boolean shouldUpdate() {
		com.codiform.moo.annotation.CollectionProperty annotation = getAnnotation( com.codiform.moo.annotation.CollectionProperty.class );
		return annotation == null ? false : annotation.update();
	}

	@Override
	public boolean isSourceRequired(boolean defaultSetting) {
		return isSourceRequired( defaultSetting, optionality );
	}

	@Override
	public boolean shouldRemoveOrphans() {
		return removeOrphans;
	}

}
