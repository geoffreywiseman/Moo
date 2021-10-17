package com.codiform.moo;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.annotation.MapProperty;
import com.codiform.moo.domain.Position;
import com.codiform.moo.domain.Security;

public class RenamedHoldings {
	@MapProperty( keySource = "previousSecurity", nullKeys = false )
	private Map<Security, Position> positions = new HashMap<>();

	public Position getPosition( Security security ) {
		return positions.get( security );
	}

	public int size() {
		return positions.size();
	}
}
