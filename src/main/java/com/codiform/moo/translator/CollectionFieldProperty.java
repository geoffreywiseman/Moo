package com.codiform.moo.translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.codiform.moo.TranslationException;

public class CollectionFieldProperty extends AbstractCollectionProperty {

	private Field field;
	private String name;
	private String expression;
	private boolean explicit;
	private boolean ignore;

	public CollectionFieldProperty(Field field, String name, String expression,
			boolean explicit, boolean ignore) {
		super(
				field.getAnnotation( com.codiform.moo.annotation.CollectionProperty.class ) );
		this.field = field;
		this.name = name;
		this.expression = expression;
		this.explicit = explicit;
		this.ignore = ignore;

		if( !field.isAccessible() )
			field.setAccessible( true );
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return field.getAnnotation( annotationClass );
	}

	public String getName() {
		return name;
	}

	public String getTranslationExpression() {
		return expression;
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			field.set( instance, value );
		} catch( IllegalArgumentException exception ) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception );
		} catch( IllegalAccessException exception ) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception );
		}
	}

	public boolean canSupportNull() {
		return true;
	}

	public Class<?> getDeclaringClass() {
		return field.getDeclaringClass();
	}

	public boolean isExplicit() {
		return explicit;
	}

	@Override
	public boolean isIgnored() {
		return ignore;
	}

	@Override
	public boolean canGetValue() {
		return true;
	}

	@Override
	public Object getValue(Object instance) {
		try {
			return field.get( instance );
		} catch( IllegalArgumentException exception ) {
			throw new TranslationException( "Cannot get value for property",
					exception );
		} catch( IllegalAccessException exception ) {
			throw new TranslationException( "Cannot access getter property",
					exception );
		}
	}

}
