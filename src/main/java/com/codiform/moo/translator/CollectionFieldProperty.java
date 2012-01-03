package com.codiform.moo.translator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.TranslationException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Ignore;

public class CollectionFieldProperty extends AbstractCollectionProperty {

	private Field field;
	private boolean explicit;
	private boolean ignore;

	public CollectionFieldProperty(Field field) {
		this.field = field;
		com.codiform.moo.annotation.Property propertyAnnotation = getAnnotation( com.codiform.moo.annotation.Property.class );
		Ignore ignoreAnnotation = getAnnotation( Ignore.class );
		explicit = propertyAnnotation != null || ignoreAnnotation != null;
		ignore = ignoreAnnotation != null;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return field.getAnnotation( annotationClass );
	}

	public String getName() {
		return field.getName();
	}

	public String getTranslationExpression() {
		com.codiform.moo.annotation.Property property = getAnnotation( com.codiform.moo.annotation.Property.class );
		if( property == null || property.translation() == null
				|| property.translation().length() == 0 ) {
			return field.getName();
		} else {
			return property.translation();
		}
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			if( !field.isAccessible() )
				field.setAccessible( true );
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

	/* package */boolean isProperty(AccessMode mode) {
		switch( mode ) {
		case FIELD:
			if( explicit ) {
				return isAcceptableField( true );
			} else {
				return isAcceptableField( false );
			}
		case METHOD:
			return getAnnotation( com.codiform.moo.annotation.Property.class ) != null
					&& isAcceptableField( true );
		default:
			throw new IllegalStateException(
					"I have no idea how to deal with access mode: " + mode );
		}
	}

	private boolean isAcceptableField(boolean throwException) {
		int modifiers = field.getModifiers();
		if( Modifier.isStatic( modifiers ) ) {
			if( throwException ) {
				throw new InvalidPropertyException(
						getName(),
						getDeclaringClass(),
						"%s (%s) is annotated with @Property, but is static.  Moo does not support static fields as properties." );
			} else {
				return false;
			}
		} else if( Modifier.isFinal( modifiers ) ) {
			if( throwException ) {
				throw new InvalidPropertyException(
						getName(),
						getDeclaringClass(),
						"%s (%s) is annotated with @Property, but is final.  Moo cannot write to final fields as properties." );
			} else {
				return false;
			}
		} else
			return true;
	}

	public boolean canSupportNull() {
		return !getType().isPrimitive();
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
			field.setAccessible( true );
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
