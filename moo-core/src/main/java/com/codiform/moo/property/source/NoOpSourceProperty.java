package com.codiform.moo.property.source;

import java.util.Map;

public class NoOpSourceProperty implements SourceProperty {

	@Override
	public Object getValue( Object source ) {
		return source;
	}

	@Override
	public Object getValue( Object source, Map<String, Object> variables ) {
		return source;
	}

	@Override
	public String getExpression() {
		return null;
	}

}
