package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;

public class LastSecurityPrices {
	@MapProperty( source = "positions", valueSource = "previousPosition", valueClass = SecurityPrice.class )
	private Map<Security, SecurityPrice> prices = new HashMap<Security, SecurityPrice>();

	public int size() {
		return prices.size();
	}

	public SecurityPrice getPrice( Security security ) {
		return prices.get( security );
	}
}