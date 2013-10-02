package com.codiform.moo.domain;

public class OrdinalDto {
	private int rank;
	private String name;

	@SuppressWarnings( "unused" )
	private OrdinalDto() {
		// private constructor for Moo
	}
	
	public OrdinalDto( int rank, String name ) {
		this.rank = rank;
		this.name = name;
	}
	
	public int getRank() {
		return rank;
	}

	public String getName() {
		return name;
	}
}