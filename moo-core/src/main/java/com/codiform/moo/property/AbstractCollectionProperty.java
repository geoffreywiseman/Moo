package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.CollectionMatcher;
import com.codiform.moo.translator.DefaultCollectionTargetFactory;
import com.codiform.moo.translator.IndexMatcher;
import com.codiform.moo.translator.TranslationTargetFactory;

public abstract class AbstractCollectionProperty extends AbstractProperty
		implements
		CollectionProperty {

	private final Class<?> itemClass;
	private final Class<CollectionMatcher<Object, Object>> matcher;
	private final Optionality optionality;
	private final boolean removeOrphans;
	private final boolean update;
	private String itemExpression;
	private Class<? extends TranslationTargetFactory> factory;

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
			itemExpression = annotation.itemExpression().trim();
			factory = annotation.factory();
			if( itemExpression.length() == 0 )
				itemExpression = null;
		} else {
			itemClass = null;
			matcher = null;
			optionality = null;
			removeOrphans = true;
			update = false;
			itemExpression = null;
			factory = DefaultCollectionTargetFactory.class;
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
		return itemClass != null;
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
	
	public String getItemExpression() {
		return itemExpression;
	}

	@Override
	public Class<? extends TranslationTargetFactory> getFactory() {
		return factory;
	}

}
