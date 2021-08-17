package com.codiform.moo.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class Position {
	private int shares;
	private float lastKnownPrice;
	private ZonedDateTime pricingDate;

	private Position previousPosition;

	public Position( int shares, float lastKnownPrice, ZonedDateTime pricingDate ) {
		this.shares = shares;
		this.lastKnownPrice = lastKnownPrice;
		this.pricingDate = pricingDate;
	}

	public ZonedDateTime getPricingDate() {
		return pricingDate;
	}

	public Position( int shares, float lastKnownPrice, ZonedDateTime pricingDate, Position previous ) {
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
