package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class PreviousPortfolio {
	@MapProperty
	private Map<Security, Position> positions = new HashMap<>();

	public Position getPosition( Security security ) {
		return positions.get( security );
	}
}
