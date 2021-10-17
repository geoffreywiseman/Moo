package com.codiform.moo.property.source;

import java.util.ArrayList;
import java.util.List;

import com.codiform.moo.MissingSourcePropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositeSourcePropertyFactory implements SourcePropertyFactory {

	private List<SourcePropertyFactory> sourcePropertyFactories = new ArrayList<SourcePropertyFactory>();
	private Logger log = LoggerFactory.getLogger( getClass() );
	
	public CompositeSourcePropertyFactory() {
		sourcePropertyFactories = new ArrayList<SourcePropertyFactory>();
		initializeDefaults();
		initializeExtensions();
	}

	private void initializeDefaults() {
		sourcePropertyFactories.add( new ReflectionSourcePropertyFactory() );
	}

	/**
	 * Adding optional extensions to the core configuration.
	 */
	private void initializeExtensions() {
		try {
			Class<?> factory = Class.forName( "com.codiform.moo.property.source.MvelSourcePropertyFactory" );
			sourcePropertyFactories.add( (SourcePropertyFactory)factory.newInstance() );
		} catch ( ClassNotFoundException e ) {
			// No MVEL Extension. That's ok. In fact, to be expected.
		} catch ( InstantiationException exception ) {
			log.warn( "Instantiation exception while configuring extensions.", exception );
		} catch ( IllegalAccessException exception ) {
			log.warn( "Instantiation exception while configuring extensions.", exception );
		}
	}
	
	/* package */ boolean containsSourcePropertyFactory( Class<? extends SourcePropertyFactory> factoryType ) {
		for( SourcePropertyFactory factory : sourcePropertyFactories ) {
			if( factoryType.isInstance( factory ) ) {
				return true;
			}
		}
		return false;
	}


	public SourceProperty getSourceProperty( String expression ) {
		String prefix = getPrefix( expression );
		if ( prefix == null ) {
			return getUnprefixedSourceProperty( expression );
		} else {
			return getPrefixedSourceProperty( prefix, expression );
		}
	}

	private SourceProperty getPrefixedSourceProperty( String prefix, String expression ) {
		String unprefixed = expression.substring( prefix.length() + 1 );
		for ( SourcePropertyFactory item : sourcePropertyFactories ) {
			if ( item.supportsPrefix( prefix ) ) {
				SourceProperty property = item.getSourceProperty( prefix, unprefixed );
				if ( property != null ) {
					return property;
				}
			}
		}
		throw new MissingSourcePropertyException( expression );
	}

	private SourceProperty getUnprefixedSourceProperty( String expression ) {
		for ( SourcePropertyFactory item : sourcePropertyFactories ) {
			SourceProperty property = item.getSourceProperty( expression );
			if ( property != null ) {
				return property;
			}
		}
		throw new MissingSourcePropertyException( expression );
	}

	private String getPrefix( String expression ) {
		int colonIndex = expression.indexOf( ':' );
		if ( colonIndex > 0 && colonIndex < ( expression.length() - 1 ) ) {
			return expression.substring( 0, colonIndex );
		} else {
			return null;
		}
	}

	@Override
	public SourceProperty getSourceProperty( String expressionPrefix, String unprefixedExpression ) {
		return getPrefixedSourceProperty( expressionPrefix, unprefixedExpression );
	}

	@Override
	public boolean supportsPrefix( String prefix ) {
		for( SourcePropertyFactory item : sourcePropertyFactories ) {
			if( item.supportsPrefix( prefix ) ) {
				return true;
			}
		}
		return false;
	}
	
	
}
