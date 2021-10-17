package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class PreviousMarketPrices {
	@MapProperty( source="positions", keyClass = String.class, keySource = "market", valueClass=SecurityPrice.class, valueSource="previousPosition" )
	private Map<String, SecurityPrice> prices = new HashMap<>();

	public int size() {
		return prices.size();
	}

	public SecurityPrice getPrice( String market ) {
		return prices.get( market );
	}
}
