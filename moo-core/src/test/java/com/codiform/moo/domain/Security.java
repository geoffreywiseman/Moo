package com.codiform.moo.domain;

public class Security implements Comparable<Security> {
	private String symbol;
	private String market;
	private Security previousSecurity;

	public Security( String symbol, String market ) {
		this.symbol = symbol;
		this.market = market;
	}

	public Security( String symbol, String market, Security previous ) {
		this( symbol, market );
		this.previousSecurity = previous;
	}

	@Override
	public String toString() {
		return symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getMarket() {
		return market;
	}

	public Security getPreviousSecurity() {
		return previousSecurity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( market == null ) ? 0 : market.hashCode() );
		result = prime * result + ( ( symbol == null ) ? 0 : symbol.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		Security other = (Security)obj;
		if ( market == null ) {
			if ( other.market != null )
				return false;
		} else if ( !market.equals( other.market ) )
			return false;
		if ( symbol == null ) {
			return other.symbol == null;
		} else
			return symbol.equals( other.symbol );
	}

	@Override
	public int compareTo( Security o ) {
		return symbol.compareTo( o.getSymbol() );
	}
}
