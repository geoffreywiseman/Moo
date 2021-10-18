package com.codiform.moo.property.source;

import java.io.Serializable;
import java.util.Map;

import com.codiform.moo.MissingSourcePropertyValueException;
import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;

/**
 * A source property that uses the MVEL expression language to retrieve the value.
 */
public class MvelSourceProperty implements SourceProperty {

	private String expression;
	private Serializable compiledExpression;

	public MvelSourceProperty( String expression, Serializable compiledExpression ) {
		this.expression = expression;
		this.compiledExpression = compiledExpression;
	}

	@Override
	public Object getValue( Object source ) {
		try {
			return MVEL.executeExpression( compiledExpression, source );
		} catch ( PropertyAccessException exception ) {
			throw new MissingSourcePropertyValueException( expression, source.getClass(), exception );
		}
	}

	@Override
	public Object getValue( Object source, Map<String, Object> variables ) {
		try {
			return MVEL.executeExpression( compiledExpression, source, variables );
		} catch ( PropertyAccessException exception ) {
			throw new MissingSourcePropertyValueException( expression, source.getClass(), exception );
		}
	}
	
	@Override
	public String getExpression() {
		return expression;
	}

}
