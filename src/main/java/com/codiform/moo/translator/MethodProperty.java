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

	private Method getter, setter;
	private boolean isProperty = false;
	private Class<?> type;
	private String name;
	private String setterFailure, getterFailure;
	private boolean explicit;
	private boolean ignore;

	public MethodProperty(Method setter) {
		this.setter = setter;
		name = convertName( setter.getName() );

		com.codiform.moo.annotation.Property propertyAnnotation = getAnnotation( com.codiform.moo.annotation.Property.class );
		Ignore ignoreAnnotation = getAnnotation( Ignore.class );
		explicit = propertyAnnotation != null || ignoreAnnotation != null;
		ignore = ignoreAnnotation != null;

		if( name == null ) {
			name = setter.getName();
			setterFailure = "Method %s (in %s) is marked with @Property but does not follow the 'set<Name>' pattern required of a method property.";
			return;
		}

		if( setter.getParameterTypes().length != 1 ) {
			setterFailure = "Method %s (in %s) is marked with @Property but is not a single-parameter method.";
			return;
		} else
			type = setter.getParameterTypes()[0];

		int modifiers = setter.getModifiers();
		if( Modifier.isStatic( modifiers ) ) {
			setterFailure = "Method %s (in %s) is marked with @Property but is static; Moo doesn't support static methods as properties.";
			return;
		}

		// past all guards
		findGetter( setter.getDeclaringClass() );
		isProperty = true;
	}

	private void findGetter(Class<?> searchClass) {
		if( type == Boolean.class || type == boolean.class ) {
			getter = findGetter( searchClass, "is" );
			if( getter == null )
				getter = findGetter( searchClass, "get" );
			if( getter == null )
				getterFailure = "No get/is + " + name + " getter.";
		}
		else if( getter == null ) {
			getter = findGetter( searchClass, "get" );
			if( getter == null )
				getterFailure = "No get " + name + " getter.";
		}
		if( getter != null && getter.getReturnType().isAssignableFrom( type ) ) {
			getter = null;
			getterFailure = "Getter return type is incompatible with " + type.getName();
		}
	}

	private Method findGetter(Class<?> startClass, String prefix) {
		StringBuilder getterBuilder = new StringBuilder( prefix );
		getterBuilder.append( Character.toUpperCase( name.charAt( 0 ) ) );
		if( name.length() > 1 )
			getterBuilder.append( name.substring( 1 ) );

		Class<?> currentClass = startClass;
		Method getter = null;
		String getterName = getterBuilder.toString();

		while( getter == null && currentClass != null ) {
			try {
				getter = currentClass.getDeclaredMethod( getterName );
			} catch( NoSuchMethodException exception ) {
				// do nothing
			}
			currentClass = currentClass.getSuperclass();
		}
		return getter;
	}

	/* package */boolean isProperty(AccessMode mode) {
		if( isExplicit() && !isProperty ) {
			throw new InvalidPropertyException( this, setterFailure );
		}
		switch( mode ) {
		case METHOD:
			return isProperty;
		case FIELD:
			return isExplicit() && isProperty;
		default:
			throw new IllegalStateException(
					"I have no idea how to deal with access mode: " + mode );
		}
	}

	private String convertName(String before) {
		if( before.length() > 3 && before.startsWith( "set" ) ) {
			return Character.toLowerCase( before.charAt( 3 ) )
					+ before.substring( 4 );
		} else {
			return null;
		}
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return setter.getAnnotation( annotationClass );
	}

	public String getName() {
		return name;
	}

	public String getTranslationExpression() {
		com.codiform.moo.annotation.Property annotation = getAnnotation( com.codiform.moo.annotation.Property.class );
		if( annotation != null && annotation.translation() != null
				&& annotation.translation().length() > 0 ) {
			return annotation.translation();
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
			if( !setter.isAccessible() )
				setter.setAccessible( true );
			setter.invoke( instance, value );
		} catch( IllegalArgumentException exception ) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception );
		} catch( IllegalAccessException exception ) {
			throw new TranslationException(
					"Cannot set value for field property " + getName(),
					exception );
		} catch( InvocationTargetException exception ) {
			throw new TranslationException( "Cannot for method property "
					+ getName(), exception );
		}
	}

	public Class<?> getDeclaringClass() {
		return setter.getDeclaringClass();
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
		return getter != null;
	}

	@Override
	public Object getValue(Object instance) {
		if( getter == null ) {
			throw new UnsupportedOperationException( "Cannot get value: " + getterFailure );
		} else {
			try {
				getter.setAccessible( true );
				return getter.invoke( instance );
			} catch( IllegalArgumentException exception ) {
				throw new TranslationException( "Illegal argument to getter", exception );
			} catch( IllegalAccessException exception ) {
				throw new TranslationException( "Cannot access getter", exception );
			} catch( InvocationTargetException exception ) {
				throw new TranslationException( "Error while invoking getter", exception );
			}
		}
	}

}
