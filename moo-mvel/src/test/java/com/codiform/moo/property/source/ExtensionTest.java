package com.codiform.moo.property.source;

import org.junit.Test;

public class ExtensionTest {

	@Test
	public void configurationContainsMvelFactory() {
		new CompositeSourcePropertyFactory().containsSourcePropertyFactory( MvelSourcePropertyFactory.class );
	}
	
}
