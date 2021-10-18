package com.codiform.moo;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.codiform.moo.domain.Position;
import com.codiform.moo.domain.Security;

public class SymbolSortedPortfolio {
	private SortedMap<Security,Position> positions = new TreeMap<>();
	
	public int size() {
		return positions.size();
	}
	
	public Iterator<Security> iterator() {
		return positions.keySet().iterator();
	}
	
	public Position getPosition( Security security ) {
		return positions.get( security );
	}

	public void setPosition( Security security, Position position ) {
		this.positions.put( security, position );
	}

	public void removePosition( Security security ) {
		this.positions.remove( security );
	}
}
