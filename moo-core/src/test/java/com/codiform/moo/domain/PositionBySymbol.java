package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class PositionBySymbol {
	@MapProperty( keyClass = String.class )
	private Map<String, Position> positions = new HashMap<String, Position>();

	public Position getPosition( String symbol ) {
		return positions.get( symbol );
	}

	public int size() {
		return positions.size();
	}
}