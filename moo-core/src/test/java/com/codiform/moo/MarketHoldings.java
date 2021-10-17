package com.codiform.moo;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;
import com.codiform.moo.domain.Position;

public class MarketHoldings {
	@MapProperty( keyClass = String.class, keySource = "market" )
	private Map<String, Position> positions = new HashMap<>();

	public Position getPosition( String market ) {
		return positions.get( market );
	}

	public int size() {
		return positions.size();
	}
}
