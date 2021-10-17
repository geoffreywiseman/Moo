package com.codiform.moo.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.codiform.moo.annotation.Property;

class SecurityPrice {
	private static DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");

	@Property( source = "lastKnownPrice" )
	private float price;

	@Property( source = "pricingDate" )
	private ZonedDateTime dateOfPrice;

	public String toString() {
		return "$" + price + " at " + format.format( dateOfPrice );
	}
}
