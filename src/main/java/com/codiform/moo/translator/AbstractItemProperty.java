package com.codiform.moo.translator;

import com.codiform.moo.annotation.Optionality;


public abstract class AbstractItemProperty extends AbstractProperty {

	private final Optionality optionality;
	private final boolean translate;
	private final boolean update;
	
	public AbstractItemProperty( com.codiform.moo.annotation.Property annotation ) {
		super();
		if( annotation == null ) {
			optionality = null;
			translate = false;
			update = false;
		} else {
			optionality = annotation.optionality();
			translate = annotation.translate();
			update = annotation.update();
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
	
}