package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.DefaultTargetFactory;
import com.codiform.moo.translator.TranslationTargetFactory;

public abstract class AbstractItemProperty extends AbstractProperty {

	private final Optionality optionality;
	private final boolean translate;
	private final boolean update;
	private final Class<? extends TranslationTargetFactory> factory;
	
	public AbstractItemProperty( com.codiform.moo.annotation.Property annotation ) {
		super();
		if( annotation == null ) {
			optionality = null;
			translate = false;
			update = false;
			factory = DefaultTargetFactory.class;
		} else {
			optionality = annotation.optionality();
			translate = annotation.translate();
			update = annotation.update();
			factory = annotation.factory();
		}
	}
	
	@Override
	public boolean isSourceRequired(boolean defaultSetting) {
		return isSourceRequired( defaultSetting, optionality );
	}

	public boolean shouldBeTranslated() {
		return translate;
	}
	
	public boolean shouldUpdate() {
		return update;
	}

	public Class<? extends TranslationTargetFactory> getFactory() {
		return factory;
	}
	
}