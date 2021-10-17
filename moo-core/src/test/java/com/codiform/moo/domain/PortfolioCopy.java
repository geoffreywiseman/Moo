package com.codiform.moo.domain;

import java.util.HashMap;
import java.util.Map;

public class PortfolioCopy {
	private Map<Security, Position> positions = new HashMap<>();

	public Map<Security, Position> getPositions() {
		return positions;
	}
}
