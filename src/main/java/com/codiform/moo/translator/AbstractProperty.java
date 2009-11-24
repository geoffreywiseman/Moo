package com.codiform.moo.translator;

import com.codiform.moo.IncompatibleTypeTranslationException;
import com.codiform.moo.TranslationException;

public abstract class AbstractProperty implements Property {

	public boolean canSupportNull() {
		return !getType().isPrimitive();
	}

	protected void checkValue(Object value) {
		if (value == null) {
			if (!canSupportNull()) {
				throw new TranslationException(
						"Cannot store null in primitive field " + getName()
								+ " (" + getType().getSimpleName() + ")");
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

}