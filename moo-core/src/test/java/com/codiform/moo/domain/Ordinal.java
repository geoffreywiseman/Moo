package com.codiform.moo.domain;

public class Ordinal {
	private int rank;
	private String name;

	public Ordinal(int rank, String name) {
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