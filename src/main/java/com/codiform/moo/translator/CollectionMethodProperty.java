package com.codiform.moo.translator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.codiform.moo.GetPropertyException;
import com.codiform.moo.SetPropertyException;

public class CollectionMethodProperty extends AbstractCollectionProperty {

	private Method getter, setter;
	private Class<?> type;
	private String name, expression;
	private String getterFailure;
	private boolean explicit;
	private boolean ignore;

	public CollectionMethodProperty(Method setter,
			com.codiform.moo.annotation.CollectionProperty annotation,
			String name,
			String expression, boolean explicit, boolean ignore) {
		super( annotation );
		this.setter = setter;
		this.name = name;
		this.expression = expression;
		this.explicit = explicit;
		this.ignore = ignore;

		type = setter.getParameterTypes()[0];

		if( !setter.isAccessible() )
			setter.setAccessible( true );

		findGetter( setter.getDeclaringClass() );
	}

	private void findGetter(Class<?> searchClass) {
		getter = findGetter( searchClass, "get" );
		if( getter == null )
			getterFailure = "No get " + name + " getter.";
		if( getter != null && !getter.getReturnType().isAssignableFrom( type ) ) {
			getter = null;
			getterFailure = "Getter return type is incompatible with "
					+ type.getName();
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

	public String getName() {
		return name;
	}

	public String getTranslationExpression() {
		return expression;
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

}
