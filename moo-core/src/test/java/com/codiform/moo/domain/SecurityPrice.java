package com.codiform.moo.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.codiform.moo.annotation.Property;

class SecurityPrice {
	private static SimpleDateFormat format = new SimpleDateFormat( "YYYY-MMM-dd HH:mm" );

	@Property( source = "lastKnownPrice" )
	private float price;

	@Property( source = "pricingDate" )
	private Date dateOfPrice;

	public String toString() {
		return "$" + price + " at " + format.format( dateOfPrice );
	}
}