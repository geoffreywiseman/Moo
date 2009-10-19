package com.codiform.moo.translator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mvel2.MVEL;

import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.annotation.Translation;
import com.codiform.moo.cache.TranslationCache;
import com.codiform.moo.cache.TranslatorCache;

public class Translator<T> {

	private Class<T> destinationClass;
	private TranslatorCache translatorCache;

	public Translator(Class<T> destination, TranslatorCache cache) {
		this.destinationClass = destination;
		this.translatorCache = cache;
	}

	public List<T> getEachTranslation(List<?> sources,
			TranslationCache translationCache) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, translationCache));
		}
		return results;
	}

	public Collection<T> getEachTranslation(Collection<?> sources,
			TranslationCache translationCache) {
		List<T> results = new ArrayList<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, translationCache));
		}
		return results;
	}

	public Set<T> getEachTranslation(Set<?> sources,
			TranslationCache translationCache) {
		Set<T> results = new HashSet<T>();
		for (Object source : sources) {
			results.add(getTranslation(source, translationCache));
		}
		return results;
	}

	public T getTranslation(Object source, TranslationCache translationCache) {
		if (source == null) {
			return null;
		} else {
			T translated = translationCache.getTranslation(destinationClass,
					source);
			if (translated != null) {
				return translated;
			} else {
				return translate(source, translationCache);
			}
		}
	}

	private T translate(Object source, TranslationCache translationCache) {
		try {
			T translated = destinationClass.newInstance();
			translationCache.putTranslation(source, translated);
			translate(source, translated, translationCache);
			return translated;
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

	private void translate(Object source, T destination,
			TranslationCache translationCache) {
		Set<Field> fields = getFieldsToTranslate();
		for (Field item : fields) {
			String expression = getTranslationExpression(item);
			Object value = getValue(source, expression);
			value = transform(value, item, translationCache);
			setValue(destination, item, value);
		}
	}

	@SuppressWarnings("unchecked")
	private Object transform(Object value, Field item,
			TranslationCache translationCache) {
		com.codiform.moo.annotation.Translate annotation = item
				.getAnnotation(com.codiform.moo.annotation.Translate.class);
		if (value instanceof Collection) {
			return transformCollection(value, item, translationCache,
					annotation);
		} else if (value.getClass().isArray()) {
			return transformArray((Object[])value, item, translationCache);
		} else if (annotation != null) {
			return translatorCache.getTranslator(item.getType())
					.getTranslation(value, translationCache);
		} else {
			return value;
		}
	}

	private Object transformArray(Object[] value, Field field,
			TranslationCache translationCache) {
		Class<?> fieldType = field.getType();
		Class<?> valueType = value.getClass();
		ArrayTranslator translator = translatorCache.getArrayTranslator();
		
		if( valueType.isAssignableFrom( fieldType ) ) {
			return translator.defensiveCopy( value );
		}
		else if( fieldType.isArray() ) {
			if( valueType.isAssignableFrom( fieldType.getComponentType() ) ) {
				return translator.copyTo( value, fieldType );
			} else {
				return translator.translate( value, fieldType.getComponentType(), translationCache);
			}
		} else {
			throw new TranslationException( String.format( "Cannot translate from array type %s[] to non-array %s", valueType.getComponentType(), fieldType.getClass().getName() ) );
		}
	}

	private Object transformCollection(Object value, Field item,
			TranslationCache translationCache,
			com.codiform.moo.annotation.Translate annotation) {
		if (annotation != null) {
			throw new TranslationException(
					"Cannot use @Translate on a collection, use @TranslateCollection instead.");
		} else {
			return translatorCache.getCollectionTranslator().translate(value,
					item.getAnnotation(TranslateCollection.class),
					translationCache);
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
				fields.add(item);
			}
			current = current.getSuperclass();
		}
		return fields;
	}

	public void update(Object source, Object destination, TranslationCache cache) {
		translate(source, destinationClass.cast(destination), cache);
	}

}
