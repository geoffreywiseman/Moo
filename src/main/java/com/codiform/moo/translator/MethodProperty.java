package com.codiform.moo.translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Translate;
import com.codiform.moo.annotation.Translation;

public class MethodProperty extends AbstractProperty implements Property {

	private Method method;
	private boolean isProperty = false;
	private Class<?> type;
	private String name;

	public MethodProperty(Method method) {
		this.method = method;
		name = convertName(method.getName());
		if (name == null)
			return;

		if (method.getParameterTypes().length != 1)
			return;
		else
			type = method.getParameterTypes()[0];

		int modifiers = method.getModifiers();
		if (Modifier.isStatic(modifiers) || Modifier.isAbstract(modifiers))
			return;

		// past all guards
		isProperty = true;
	}

	/* package */boolean isProperty(AccessMode mode) {
		switch (mode) {
		case METHOD:
			return isProperty;
		case FIELD:
			return isProperty
					&& getAnnotation(com.codiform.moo.annotation.Property.class) != null;
		default:
			throw new IllegalStateException(
					"I have no idea how to deal with access mode: " + mode);
		}
	}

	private String convertName(String before) {
		if (before.length() > 3 && before.startsWith("set")) {
			return Character.toLowerCase(before.charAt(3))
					+ before.substring(4);
		} else {
			return null;
		}
	}

	public AccessMode getAccessMode() {
		return AccessMode.METHOD;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	public String getName() {
		return name;
	}

	public String getTranslationExpression() {
		Translation translationAnnotation = getAnnotation(Translation.class);
		if (translationAnnotation != null) {
			return translationAnnotation.value();
		} else {
			return name;
		}
	}

	public Class<?> getType() {
		return type;
	}

	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			if (!method.isAccessible())
				method.setAccessible(true);
			method.invoke(instance, value);
		} catch (IllegalArgumentException exception) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception);
		} catch (IllegalAccessException exception) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception);
		} catch (InvocationTargetException exception) {
			throw new TranslationException("Cannot for method property "
					+ getName(), exception);
		}
	}

	public boolean shouldBeTranslated() {
		return getAnnotation(Translate.class) == null;
	}

}
