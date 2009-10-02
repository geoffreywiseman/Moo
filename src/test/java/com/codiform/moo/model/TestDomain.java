package com.codiform.moo.model;

import java.util.Arrays;
import java.util.List;

public class TestDomain {

	private String string;

	private int integer;

	private float floatingPoint;

	private List<Integer> values = Arrays.asList( 1, 2, 3, 4 );

	public TestDomain( String string, int integer, float floatingPoint ) {
		this.string = string;
		this.integer = integer;
		this.floatingPoint = floatingPoint;
	}

	public String getString() {
		return string;
	}

	public int getInteger() {
		return integer;
	}

	public float getFloatingPoint() {
		return floatingPoint;
	}

	public List<Integer> getValues() {
		return values;
	}
	
	public TestDomain getSelf() {
		return this;
	}
	
	public List<TestDomain> getSelves() {
		return Arrays.asList( this, this, this );
	}

}
