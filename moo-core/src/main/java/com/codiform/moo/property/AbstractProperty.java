package com.codiform.moo.property;

import com.codiform.moo.IncompatibleTypeTranslationException;
import com.codiform.moo.NullUnsupportedTranslationException;
import com.codiform.moo.annotation.Optionality;
import com.codiform.moo.translator.PrimitiveAssignment;

public abstract class AbstractProperty implements Property {

	@Override
	public boolean canSupportNull() {
		return !getType().isPrimitive();
	}
	
	protected boolean isSourceRequired(boolean defaultSetting,
			Optionality optionality) {
		if( optionality == null ) {
			return defaultSetting;
		} else {
			switch( optionality ) {
			case REQUIRED:
				return true;
			case OPTIONAL:
				return false;
			default:
				return defaultSetting;
			}
		}
	}

	protected void checkValue(Object value) {
		if (value == null) {
			if (!canSupportNull()) {
				throw new NullUnsupportedTranslationException( getName(), getType() );
			}
		} else if (getType().isPrimitive()) {
			if (!PrimitiveAssignment.isCompatible(getType(), value.getClass())) {
				throw new IncompatibleTypeTranslationException( value, getName(), getType() );
			}
		} else if (value.getClass().isPrimitive()) {
			if (!PrimitiveAssignment.isCompatible(value.getClass(), getType())) {
				throw new IncompatibleTypeTranslationException( value, getName(), getType() );
			}
		} else if (!getType().isAssignableFrom(value.getClass())) {
			throw new IncompatibleTypeTranslationException( value, getName(), getType() );
		}
	}

	@Override
	public boolean isTypeOrSubtype(Class<?> type) {
		return type.isAssignableFrom( getType() );
	}
	
}
