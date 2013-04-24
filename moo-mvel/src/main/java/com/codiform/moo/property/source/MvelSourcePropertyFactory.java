package com.codiform.moo.property.source;

import java.io.Serializable;

import org.mvel2.MVEL;

import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;

public class MvelSourcePropertyFactory implements SourcePropertyFactory {

	private static final String SUPPORTED_PREFIX = "mvel";

	@Override
	public SourceProperty getSourceProperty( String expression ) {
		Serializable compiledExpression = MVEL.compileExpression( expression );
		return new MvelSourceProperty( expression, compiledExpression );
	}

	@Override
	public SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression ) {
		// ignore the origin; it's only use is to lock in this factory.
		return getSourceProperty( unprefixedExpression );
	}

	@Override
	public boolean supportsPrefix( String prefix ) {
		return SUPPORTED_PREFIX.equals( prefix );
	}

}
