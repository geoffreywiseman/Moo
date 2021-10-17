package com.codiform.moo.translator;

import com.codiform.moo.configuration.Configuration;
import com.codiform.moo.property.source.CompositeSourcePropertyFactory;
import com.codiform.moo.property.source.SourceProperty;
import com.codiform.moo.property.source.SourcePropertyFactory;
import org.junit.BeforeClass;

public class TestWithTranslatorFactory {

	protected static TranslatorFactory translatorFactory;
	protected static SourcePropertyFactory sourcePropertyFactory;

	@BeforeClass
	public static void createTranslatorFactory() {
		Configuration cfg = new Configuration();
		sourcePropertyFactory = new CompositeSourcePropertyFactory();
		translatorFactory = new CachingTranslatorFactory( cfg, sourcePropertyFactory );
	}

	public TestWithTranslatorFactory() {
		super();
	}
	
	public <T> ObjectTranslator<T> getTranslator( Class<T> destinationType ) {
		return translatorFactory.getTranslator( destinationType );
	}

	public SourceProperty getSourceProperty( String expression ) {
		return sourcePropertyFactory.getSourceProperty( expression );
	}
	

}
