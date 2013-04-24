package com.codiform.moo.property.source;

import java.util.Arrays;
import java.util.List;

public class ReflectionSourcePropertyFactory implements SourcePropertyFactory {

	private List<String> keywords;
	private List<String> literals;

	public ReflectionSourcePropertyFactory() {
		keywords = Arrays.asList( "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default",
				"do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if", "goto", "implements", "import", "instanceof",
				"int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
				"super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "why" );
		literals = Arrays.asList( "null", "true", "false" );
	}

	@Override
	public SourceProperty getSourceProperty( String expression ) {
		if ( isIdentifier( expression ) ) {
			return new ReflectionSourceProperty( expression );
		} else {
			return null;
		}
	}

	private boolean isIdentifier( String expression ) {
		return isIdentifierChars( expression ) && !isKeyword( expression ) && !isLiteral( expression );
	}

	private boolean isKeyword( String expression ) {
		return keywords.contains( expression );
	}

	private boolean isLiteral( String expression ) {
		return literals.contains( expression );
	}

	private boolean isIdentifierChars( String expression ) {
		for ( int index = 0; index < expression.length(); index++ ) {
			if ( index == 0 ) {
				if ( !Character.isJavaIdentifierStart( expression.charAt( 0 ) ) )
					return false;
			} else {
				if ( !Character.isJavaIdentifierPart( expression.charAt( index ) ) )
					return false;
			}
		}
		return true;
	}

	@Override
	public SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression ) {
		throw new UnsupportedOperationException( "ReflectionSourcePropertyFactory doesn't support prefixes." );
	}

	@Override
	public boolean supportsPrefix( String prefix ) {
		return false;
	}

}
