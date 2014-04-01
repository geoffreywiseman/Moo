package com.codiform.moo.property;

import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.DefaultObjectTargetFactory;
import com.codiform.moo.translator.TranslationTargetFactory;

public abstract class AbstractObjectProperty extends AbstractProperty {

	private final Optionality optionality;
	private final boolean translate;
	private final boolean update;
	private final Class<? extends TranslationTargetFactory> factory;
	private String name;
	private String sourcePropertyExpression;
	private boolean explicit;
	private boolean ignore;
	
	public AbstractObjectProperty( String name, com.codiform.moo.annotation.Property annotation, String sourcePropertyExpression, boolean explicit, boolean ignore ) {
		super();
		this.name = name;
		this.sourcePropertyExpression = sourcePropertyExpression;
		this.explicit = explicit;
		this.ignore = ignore;
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
	
	public String getName() {
		return name;
	}

	public String getSourcePropertyExpression() {
		return sourcePropertyExpression;
	}

	public boolean isExplicit() {
		return explicit;
	}

	@Override
	public boolean isIgnored() {
		return ignore;
	}

}