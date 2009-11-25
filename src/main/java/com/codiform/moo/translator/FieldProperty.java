package com.codiform.moo.translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Translation;

public class FieldProperty extends AbstractProperty {

	private Field field;

	public FieldProperty(Field field) {
		this.field = field;
	}

	public AccessMode getAccessMode() {
		return AccessMode.FIELD;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	public String getName() {
		return field.getName();
	}

	public String getTranslationExpression() {
		Translation translationAnnotation = getAnnotation(Translation.class);
		if (translationAnnotation != null) {
			return translationAnnotation.value();
		} else {
			return field.getName();
		}
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void setValue(Object instance, Object value) {
		checkValue(value);
		try {
			if (!field.isAccessible())
				field.setAccessible(true);
			field.set(instance, value);
		} catch (IllegalArgumentException exception) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception);
		} catch (IllegalAccessException exception) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception);
		}
	}

	/* package */boolean isProperty(AccessMode mode) {
		switch (mode) {
		case FIELD:
			return isAcceptableField();
		case METHOD:
			return isAcceptableField()
					&& getAnnotation(com.codiform.moo.annotation.Property.class) != null;
		default:
			throw new IllegalStateException(
					"I have no idea how to deal with access mode: " + mode);
		}
	}

	private boolean isAcceptableField() {
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers))
			return false;
		else if (Modifier.isFinal(modifiers))
			return false;
		else
			return true;
	}

	public boolean canSupportNull() {
		return !getType().isPrimitive();
	}

}
