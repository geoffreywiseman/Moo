package com.codiform.moo.translator;

public interface ValueTypeTranslator<T> {

	T getTranslation( Object source, Class<T> destinationClass );

}
