package com.codiform.moo.property.source;

import java.io.Serializable;

import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codiform.moo.MissingSourcePropertyException;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;

public class MvelSourcePropertyFactory implements SourcePropertyFactory {

	private static final String SUPPORTED_PREFIX = "mvel";
	private final Logger log = LoggerFactory.getLogger( getClass() );

	@Override
	public SourceProperty getSourceProperty( String expression ) {
		try {
			return compileAndReturnProperty( expression );
		} catch( ArrayIndexOutOfBoundsException exception ) {
			// Unprefixed expression, just log and continue by returning null.
			log.warn( "Error while compiling expression {}", exception );
			return null;
		} catch( CompileException exception ) {
			// Unprefixed expression, just log and continue by returning null.
			log.warn( "Error while compiling expression {}", exception );
			return null;
		}
	}
	
	private MvelSourceProperty compileAndReturnProperty( String expression ) {
		Serializable compiled = MVEL.compileExpression( expression );
		return new MvelSourceProperty( expression, compiled );
	}

	@Override
	public SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression ) {
		// Because MVEL prefix was used, assume that a failure here is worth reporting right away.
		try {
			return compileAndReturnProperty( unprefixedExpression );
		} catch( ArrayIndexOutOfBoundsException exception ) {
			throw new MissingSourcePropertyException( expressionPrefix + ":" + unprefixedExpression, exception ); 
		} catch( CompileException exception ) {
			throw new MissingSourcePropertyException( expressionPrefix + ":" + unprefixedExpression, exception ); 
		}
	}

	@Override
	public boolean supportsPrefix( String prefix ) {
		return SUPPORTED_PREFIX.equals( prefix );
	}

}
