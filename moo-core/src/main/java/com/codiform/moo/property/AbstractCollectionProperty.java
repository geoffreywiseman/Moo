package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.*;

public abstract class AbstractCollectionProperty extends AbstractProperty
		implements
		CollectionProperty {

	private final Class<?> itemClass;
	private final Class<CollectionMatcher<Object, Object>> matcher;
	private final Optionality optionality;
	private final boolean removeOrphans;
	private final boolean update;
	private String itemSource;
	private Class<? extends TranslationTargetFactory> factory;
	private Class<? extends TranslationTargetFactory> itemFactory;

	@SuppressWarnings("unchecked")
	public AbstractCollectionProperty(
			com.codiform.moo.annotation.CollectionProperty annotation) {
		if( annotation != null ) {
			itemClass = annotation.itemClass() == Object.class ? null
					: annotation.itemClass();
			matcher = (Class<CollectionMatcher<Object, Object>>) (annotation.matcher() == IndexMatcher.class ? null
					: annotation.matcher());
			optionality = annotation.optionality();
			removeOrphans = annotation.removeOrphans();
			update = annotation.update();
			itemSource = annotation.itemSource().trim();
			factory = annotation.factory();
			itemFactory = annotation.itemFactory();
			if( itemSource.length() == 0 )
				itemSource = null;
		} else {
			itemClass = null;
			matcher = null;
			optionality = null;
			removeOrphans = true;
			update = false;
			itemSource = null;
			factory = DefaultCollectionTargetFactory.class;
			itemFactory = DefaultObjectTargetFactory.class;
		}
	}

	@Override
	public Class<?> getItemClass() {
		return itemClass;
	}

	@Override
	public boolean hasMatcher() {
		return matcher != null;
	}

	@Override
	public boolean shouldItemsBeTranslated() {
		return itemClass != null || getItemFactory() != DefaultObjectTargetFactory.class;
	}

	@Override
	public Class<CollectionMatcher<Object, Object>> getMatcherType() {
		return matcher;
	}

	@Override
	public boolean shouldUpdate() {
		return update;
	}

	@Override
	public boolean isSourceRequired(boolean defaultSetting) {
		return isSourceRequired( defaultSetting, optionality );
	}

	@Override
	public boolean shouldRemoveOrphans() {
		return removeOrphans;
	}
	
	@Override
	public boolean shouldBeTranslated() {
		return false;
	}
	
	public String getItemSource() {
		return itemSource;
	}

	@Override
	public Class<? extends TranslationTargetFactory> getFactory() {
		return factory;
	}

	@Override
	public Class<? extends TranslationTargetFactory> getItemFactory() { return itemFactory; }

}
