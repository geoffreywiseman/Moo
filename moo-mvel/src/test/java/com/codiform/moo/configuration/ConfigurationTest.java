package com.codiform.moo.configuration;

import org.junit.Test;

import com.codiform.moo.property.source.MvelSourcePropertyFactory;

public class ConfigurationTest {

	@Test
	public void configurationContainsMvelFactory() {
		new Configuration().containsSourcePropertyFactory( MvelSourcePropertyFactory.class );
	}
	
}
