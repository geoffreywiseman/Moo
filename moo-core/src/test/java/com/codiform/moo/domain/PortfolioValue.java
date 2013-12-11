package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class PortfolioValue {
	
	@MapProperty( valueSource = "lastKnownValue" )
	private Map<Security, Double> positions = new HashMap<Security, Double>();

	public Double getValue( Security security ) {
		return positions.get( security );
	}

	public int size() {
		return positions.size();
	}
	
	public void putValue( Security security, double value ) {
		positions.put( security, value );
	}
	
}