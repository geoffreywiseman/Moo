package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {
	private Map<Security, Position> positions = new HashMap<Security, Position>();

	public void add( Security symbol, Position position ) {
		positions.put( symbol, position );
	}

	public Position getPosition( Security security ) {
		return positions.get( security );
	}

	public Map<Security, Position> getPositions() {
		return positions;
	}
}