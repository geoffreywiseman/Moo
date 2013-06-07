package com.codiform.moo.translator;

import com.codiform.moo.TranslationException;

public class StringValueTypeTranslator implements ValueTypeTranslator<String> {

	@Override
	public String getTranslation( Object source, Class<String> destinationClass ) {
		try {
			if ( source instanceof String )
				return (String)source;
			else
				return source.toString();
		} catch ( Exception exception ) {
			throw new TranslationException( "Could not translate object " + source + " to String.", exception );
		}
	}

}
