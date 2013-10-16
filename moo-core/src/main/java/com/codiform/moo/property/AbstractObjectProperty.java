package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.DefaultObjectTargetFactory;
import com.codiform.moo.translator.TranslationTargetFactory;

public abstract class AbstractObjectProperty extends AbstractProperty {

	private final Optionality optionality;
	private final boolean translate;
	private final boolean update;
	private final Class<? extends TranslationTargetFactory> factory;
	
	public AbstractObjectProperty( com.codiform.moo.annotation.Property annotation ) {
		super();
		if( annotation == null ) {
			optionality = null;
			translate = false;
			update = false;
			factory = DefaultObjectTargetFactory.class;
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