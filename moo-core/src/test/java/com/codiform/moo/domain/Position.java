package com.codiform.moo.domain;

import java.util.Date;

public class Position {
	private int shares;
	private float lastKnownPrice;
	private Date pricingDate;

	private Position previousPosition;

	public Position( int shares, float lastKnownPrice, Date pricingDate ) {
		this.shares = shares;
		this.lastKnownPrice = lastKnownPrice;
		this.pricingDate = pricingDate;
	}

	public Date getPricingDate() {
		return pricingDate;
	}

	public Position( int shares, float lastKnownPrice, Date pricingDate, Position previous ) {
		this( shares, lastKnownPrice, pricingDate );
		this.previousPosition = previous;
	}

	public double getLastKnownValue() {
		return ( (double)shares ) * ( (double)lastKnownPrice );
	}

	public Position getPreviousPosition() {
		return previousPosition;
	}
}