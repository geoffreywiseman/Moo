package com.codiform.moo.translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Ignore;

public class MethodProperty extends AbstractProperty implements Property {

	private Method method;
	private boolean isProperty = false;
	private Class<?> type;
	private String name;
	private String propertyFailure;
	private boolean explicit;
	private boolean ignore;

	public MethodProperty(Method method) {
		this.method = method;
		name = convertName(method.getName());
		
		com.codiform.moo.annotation.Property propertyAnnotation = getAnnotation( com.codiform.moo.annotation.Property.class );
		Ignore ignoreAnnotation = getAnnotation( Ignore.class );
		explicit = propertyAnnotation != null || ignoreAnnotation != null;
		ignore = ignoreAnnotation != null;
		
		if (name == null) {
			propertyFailure = "Method %s (in %s) is marked with @Property but does not follow the 'set<Name>' pattern required of a method property.";
			return;
		}

		if (method.getParameterTypes().length != 1) {
			propertyFailure = "Method %s (in %s) is marked with @Property but is not a single-parameter method.";
			return;
		} else
			type = method.getParameterTypes()[0];

		int modifiers = method.getModifiers();
		if (Modifier.isStatic(modifiers)) {
			propertyFailure = "Method %s (in %s) is marked with @Property but is static; Moo doesn't support static methods as properties.";
			return;
		}

		// past all guards
		isProperty = true;
	}

	/* package */boolean isProperty(AccessMode mode) {
		if (isExplicit() && !isProperty) {
			throw new InvalidPropertyException(this, propertyFailure);
		}
		switch (mode) {
		case METHOD:
			return isProperty;
		case FIELD:
			return isExplicit() && isProperty;
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

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return method.getAnnotation(annotationClass);
	}

	public String getName() {
		return name;
	}

	public String getTranslationExpression() {
		com.codiform.moo.annotation.Property annotation = getAnnotation(com.codiform.moo.annotation.Property.class);
		if( annotation != null && annotation.translation() != null && annotation.translation().length() > 0 ) {
			return annotation.translation();
		} else {
			return name;
		}
	}

	public Class<?> getType() {
		return type;
	}

	public void setValue(Object instance, Object value) {
		checkValue(value);
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

	public Class<?> getDeclaringClass() {
		return method.getDeclaringClass();
	}

	public boolean isExplicit() {
		return explicit;
	}

	@Override
	public boolean isIgnored() {
		return ignore;
	}

}
