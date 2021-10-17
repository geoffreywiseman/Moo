package com.codiform.moo.property.source;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.codiform.moo.MissingSourcePropertyValueException;

public class ReflectionSourceProperty implements SourceProperty {

	private String propertyName;
	private String isGetterName;
	private String getGetterName;
	
	public ReflectionSourceProperty( String propertyName ) {
		this.propertyName = propertyName;
		String capped = cap( propertyName );
		isGetterName = "is" + capped;
		getGetterName = "get" + capped;
	}
	
	private String cap( String propertyName ) {
		char firstChar = propertyName.charAt( 0 );
		if( Character.isLowerCase( firstChar ) ) {
			return "" + Character.toUpperCase( firstChar ) + propertyName.substring( 1 );
		} else {
			return propertyName;
		}
	}

	@Override
	public Object getValue( Object source ) {
		Class<?> currentClass = source.getClass();
		while( currentClass != null ) {
			Method getter = getGetter( source, currentClass );
			if( getter != null )
				return getValue( getter, source );
			Field field = getField( source, currentClass );
			if( field != null )
				return getValue( field, source );
			currentClass = currentClass.getSuperclass();
		}
		throw new MissingSourcePropertyValueException( propertyName, source.getClass() );
	}

	private Object getValue( Field field, Object source ) {
		field.setAccessible( true );
		try {
			return field.get( source );
		} catch ( IllegalArgumentException | IllegalAccessException cause ) {
			throw new MissingSourcePropertyValueException( propertyName, source.getClass(), cause );
		}
	}

	private Field getField( Object source, Class<?> currentClass ) {
		for( Field item : currentClass.getDeclaredFields( ) ) {
			if( item.getName().equals( propertyName ) )
				return item;
		}
		return null;
	}

	private Object getValue( Method getter, Object source ) {
		getter.setAccessible( true );
		try {
			return getter.invoke( source );
		} catch ( IllegalArgumentException | InvocationTargetException | IllegalAccessException cause ) {
			throw new MissingSourcePropertyValueException( propertyName, source.getClass(), cause );
		}
	}

	private Method getGetter( Object source, Class<?> currentClass ) {
		for( Method item : currentClass.getDeclaredMethods() ) {
			if( item.getParameterTypes().length == 0 ) {
				String methodName = item.getName();
				if( methodName.equals( isGetterName ) || methodName.equals( getGetterName ) )
					return item;
			}
		}
		return null;
	}

	@Override
	public Object getValue( Object source, Map<String, Object> variables ) {
		return getValue( source );
	}
	
	public String getExpression() {
		return propertyName;
	}

	@Override
	public String toString() {
		return "ReflectionSourceProperty [propertyName=" + propertyName + "]";
	}

}
