package com.codiform.moo.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.codiform.moo.GetPropertyException;
import com.codiform.moo.SetPropertyException;

public class MethodProperty extends AbstractObjectProperty {

	private Method getter, setter;
	private Class<?> type;
	private String getterFailure;

	public MethodProperty(Method setter,
			com.codiform.moo.annotation.Property annotation, String name,
			String expression, boolean explicit, boolean ignore) {
		super( name, annotation, expression, explicit, ignore );
		this.setter = setter;

		type = setter.getParameterTypes()[0];

		if( !setter.isAccessible() )
			setter.setAccessible( true );

		findGetter( setter.getDeclaringClass() );
	}

	private void findGetter(Class<?> searchClass) {
		if( type == Boolean.class || type == boolean.class ) {
			getter = findGetter( searchClass, "is" );
			if( getter == null )
				getter = findGetter( searchClass, "get" );
			if( getter == null )
				getterFailure = "No get/is + " + getName() + " getter.";
		}
		else if( getter == null ) {
			getter = findGetter( searchClass, "get" );
			if( getter == null )
				getterFailure = "No get " + getName() + " getter.";
		}
		if( getter != null && !getter.getReturnType().isAssignableFrom( type ) ) {
			getter = null;
			getterFailure = "Getter return type is incompatible with "
					+ type.getName();
		}
		if( getter != null && !getter.isAccessible() )
			getter.setAccessible( true );
	}

	private Method findGetter(Class<?> startClass, String prefix) {
		StringBuilder getterBuilder = new StringBuilder( prefix );
		String name = getName();
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

	public Class<?> getType() {
		return type;
	}

	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			setter.invoke( instance, value );
		} catch( IllegalArgumentException exception ) {
			throw new SetPropertyException( getName(), getType(), value,
					exception );
		} catch( IllegalAccessException exception ) {
			throw new SetPropertyException( getName(), getType(), value,
					exception );
		} catch( InvocationTargetException exception ) {
			throw new SetPropertyException( getName(), getType(), value,
					exception );
		}
	}

	public Class<?> getDeclaringClass() {
		return setter.getDeclaringClass();
	}

	@Override
	public boolean canGetValue() {
		return getter != null;
	}

	@Override
	public Object getValue(Object instance) {
		if( getter == null ) {
			throw new UnsupportedOperationException( "Cannot get value: "
					+ getterFailure );
		} else {
			try {
				getter.setAccessible( true );
				return getter.invoke( instance );
			} catch( IllegalArgumentException exception ) {
				throw new GetPropertyException( getName(), getType(), exception );
			} catch( IllegalAccessException exception ) {
				throw new GetPropertyException( getName(), getType(), exception );
			} catch( InvocationTargetException exception ) {
				throw new GetPropertyException( getName(), getType(), exception );
			}
		}
	}
	
	@Override
	public String toString() {
		return "MethodProperty<" + getName() + ">";
	}

}
