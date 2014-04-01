package com.codiform.moo.property;

import java.lang.reflect.Field;

import com.codiform.moo.GetPropertyException;
import com.codiform.moo.SetPropertyException;

public class FieldProperty extends AbstractObjectProperty {

	private Field field;

	public FieldProperty(Field field,
			com.codiform.moo.annotation.Property annotation, String name,
			String expression,
			boolean explicit,
			boolean ignore) {
		super( name, annotation, expression, explicit, ignore );
		this.field = field;

		if( !field.isAccessible() )
			field.setAccessible( true );
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			field.set( instance, value );
		} catch( IllegalArgumentException exception ) {
			throw new SetPropertyException(
					getName(), getType(), value,
					exception );
		} catch( IllegalAccessException exception ) {
			throw new SetPropertyException(
					getName(), getType(), value,
					exception );
		}
	}

	public Class<?> getDeclaringClass() {
		return field.getDeclaringClass();
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
			throw new GetPropertyException( getName(), getType(), exception );
		} catch( IllegalAccessException exception ) {
			throw new GetPropertyException( getName(), getType(), exception );
		}
	}
	
	@Override
	public String toString() {
		return "FieldProperty<" + getName() + ">";
	}

}
