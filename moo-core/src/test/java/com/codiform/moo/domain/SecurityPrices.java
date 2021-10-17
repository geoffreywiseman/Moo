package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class SecurityPrices {
	@MapProperty( source = "positions", valueClass = SecurityPrice.class )
	private Map<Security, SecurityPrice> prices = new HashMap<>();

	public int size() {
		return prices.size();
	}

	public SecurityPrice getPrice( Security security ) {
		return prices.get( security );
	}
}
