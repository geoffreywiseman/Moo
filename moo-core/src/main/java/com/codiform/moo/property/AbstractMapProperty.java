package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.DefaultMapTargetFactory;
import com.codiform.moo.translator.TranslationTargetFactory;

public abstract class AbstractMapProperty extends AbstractProperty implements MapProperty {

	private final Optionality optionality;
	private final boolean removeOrphans;
	private final boolean update;
	private Class<? extends TranslationTargetFactory> factory;
	private Class<?> keyClass;
	private Class<?> valueClass;
	private String keySource;
	private String valueSource;
	private boolean nullKeys;

	public AbstractMapProperty( com.codiform.moo.annotation.MapProperty annotation ) {
		if ( annotation != null ) {
			optionality = annotation.optionality();
			removeOrphans = annotation.removeOrphans();
			update = annotation.update();
			factory = annotation.factory();
			keyClass = annotation.keyClass() == Object.class ? null : annotation.keyClass();
			valueClass = annotation.valueClass() == Object.class ? null : annotation.valueClass();
			nullKeys = annotation.nullKeys();
			
			keySource = annotation.keySource().trim();
			if( keySource.isEmpty() )
				keySource = null;
			
			valueSource = annotation.valueSource().trim();
			if( valueSource.isEmpty() )
				valueSource = null;
		} else {
			optionality = null;
			removeOrphans = true;
			update = false;
			factory = DefaultMapTargetFactory.class;
			keyClass = null;
			valueClass = null;
			keySource = null;
			nullKeys = true;
		}
	}

	@Override
	public boolean shouldUpdate() {
		return update;
	}

	@Override
	public boolean isSourceRequired( boolean defaultSetting ) {
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

	@Override
	public Class<? extends TranslationTargetFactory> getFactory() {
		return factory;
	}

	@Override
	public Class<?> getKeyClass() {
		return keyClass;
	}

	@Override
	public Class<?> getValueClass() {
		return valueClass;
	}
	
	@Override
	public String getKeySource() {
		return keySource;
	}
	
	@Override
	public String getValueSource() {
		return valueSource;
	}
	
	@Override
	public boolean allowNullKeys() {
		return nullKeys;
	}

}
