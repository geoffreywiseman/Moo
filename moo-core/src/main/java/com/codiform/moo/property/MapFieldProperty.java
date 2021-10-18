package com.codiform.moo.property;

import java.lang.reflect.Field;

import com.codiform.moo.GetPropertyException;
import com.codiform.moo.SetPropertyException;

public class MapFieldProperty extends AbstractMapProperty {

	private Field field;
	private String name;
	private String expression;
	private boolean explicit;
	private boolean ignore;

	public MapFieldProperty(Field field,
			com.codiform.moo.annotation.MapProperty annotation,
			String name, String expression,
			boolean explicit, boolean ignore) {
		super( annotation );
		this.field = field;
		this.name = name;
		this.expression = expression;
		this.explicit = explicit;
		this.ignore = ignore;

		if( !field.isAccessible() )
			field.setAccessible( true );
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSourcePropertyExpression() {
		return expression;
	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	@Override
	public void setValue(Object instance, Object value) {
		checkValue( value );
		try {
			field.set( instance, value );
		} catch( IllegalArgumentException | IllegalAccessException exception ) {
			throw new SetPropertyException( getName(), getType(), value,
					exception );
		}
	}

	@Override
	public boolean canSupportNull() {
		return true;
	}

	@Override
	public Class<?> getDeclaringClass() {
		return field.getDeclaringClass();
	}

	@Override
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
		} catch( IllegalArgumentException | IllegalAccessException exception ) {
			throw new GetPropertyException( getName(), getType(), exception );
		}
	}

}
