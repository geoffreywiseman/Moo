package com.codiform.moo.translator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;

import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.annotation.Translation;
import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.source.TranslationSource;

public class Translator<T> {

	private Class<T> destinationClass;
	private Configuration configuration;

	public Translator(Class<T> destination, Configuration configuration) {
		this.destinationClass = destination;
		this.configuration = configuration;
	}

	@SuppressWarnings("unchecked")
	private Object transform(Object value, Field item,
			TranslationSource translationSource) {
		com.codiform.moo.annotation.Translate annotation = item
				.getAnnotation(com.codiform.moo.annotation.Translate.class);
		if (value == null) {
			return null;
		} else if (value instanceof Collection) {
			return transformCollection(value, item, translationSource,
					annotation);
		} else if (value.getClass().isArray()) {
			return transformArray((Object[]) value, item, translationSource);
		} else if (annotation != null) {
			return translationSource.getTranslation(value, item.getType());
		} else {
			return value;
		}
	}

	private Object transformArray(Object[] value, Field field,
			TranslationSource translationSource) {
		Class<?> fieldType = field.getType();
		Class<?> valueType = value.getClass();

		if (valueType.isAssignableFrom(fieldType)) {
			return configuration.getArrayTranslator().defensiveCopy(value);
		} else if (fieldType.isArray()) {
			if (valueType.isAssignableFrom(fieldType.getComponentType())) {
				return configuration.getArrayTranslator().copyTo(value, fieldType);
			} else {
				return configuration.getArrayTranslator().translate(value, fieldType
						.getComponentType(), translationSource);
			}
		} else {
			throw new TranslationException(String.format(
					"Cannot translate from source array type %s[] to destination type %s",
					valueType.getComponentType(), fieldType.getName()));
		}
	}

	private Object transformCollection(Object value, Field item,
			TranslationSource translationSource,
			com.codiform.moo.annotation.Translate annotation) {
		if (annotation != null) {
			throw new TranslationException(
					"Cannot use @Translate on a collection (cannot determine internal type due to erasure); use @TranslateCollection instead.");
		} else {
			return configuration.getCollectionTranslator().translate(value, item
					.getAnnotation(TranslateCollection.class),
					translationSource);
		}
	}

	private void setValue(T destination, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(destination, value);
		} catch (IllegalArgumentException exception) {
			throw new TranslationException(String.format(
					"Could not set field: %s", field.getName()), exception);
		} catch (IllegalAccessException exception) {
			throw new TranslationException(String.format(
					"Could not set field: %s", field.getName()), exception);
		}
	}

	private Object getValue(Object source, String expression) {
		return MVEL.eval(expression, source);
	}

	private String getTranslationExpression(Field item) {
		Translation translation = item.getAnnotation(Translation.class);
		if (translation == null || translation.value() == null)
			return item.getName();
		else
			return translation.value();
	}

	private Set<Field> getFieldsToTranslate() {
		Set<Field> fields = new HashSet<Field>();

		Class<?> current = destinationClass;
		while (current != null) {
			for (Field item : current.getDeclaredFields()) {
				int modifiers = item.getModifiers();
				if (!Modifier.isFinal(modifiers)
						&& !Modifier.isStatic(modifiers))
					fields.add(item);
			}
			current = current.getSuperclass();
		}
		return fields;
	}

	public void update(Object source, T destination,
			TranslationSource translationSource) {
		Set<Field> fields = getFieldsToTranslate();
		for (Field item : fields) {
			updateField(source, destination, translationSource, item);
		}
	}

	private void updateField(Object source, T destination,
			TranslationSource translationSource, Field item) {
		String expression = getTranslationExpression(item);
		try {
			Object value = getValue(source, expression);
			value = transform(value, item, translationSource);
			setValue(destination, item, value);
		} catch (PropertyAccessException exception) {
			if( configuration.isSourcePropertyRequired()) {
				throw new TranslationException(
						"Could not find required source property for expression: "
								+ expression, exception);
			}
		}
	}

	public void castAndUpdate(Object source, Object from,
			TranslationSource translationSource) {
		update(source, destinationClass.cast(from), translationSource);
	}

	public T create() {
		try {
			return destinationClass.newInstance();
		} catch (InstantiationException exception) {
			throw new TranslationException(String.format(
					"Error while instantiating %s", destinationClass),
					exception);
		} catch (IllegalAccessException exception) {
			throw new TranslationException(String.format(
					"Not allowed to instantiate %s", destinationClass),
					exception);
		}
	}

}
