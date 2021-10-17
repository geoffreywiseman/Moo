package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class SimplifiedPortfolioValue {

	@MapProperty( keySource="symbol" )
	private Map<String, Double> positions = new HashMap<>();

	public Double getValue( String symbol ) {
		return positions.get( symbol );
	}

	public int size() {
		return positions.size();
	}

	public void putValue( String symbol, double value ) {
		positions.put( symbol, value );
	}

}
