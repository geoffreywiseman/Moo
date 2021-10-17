package com.codiform.moo.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents license types in the province of Ontario, Canada, as taken from:
 * http://www.mto.gov.on.ca/english/dandv/driver/classes.shtml
 */
public enum LicenseType {
	G("Class G"), F("Class F", G), D("Class D", G), E("Class E", F, G), C("Class C", D, F, G), G1("Class G, Graduated Level 1"), G2(
			"Class G, Graduated Level 2"), M_L("Class M with Condition L"), M("Class M", M_L), M1("Class M, Graduated Level 1", M_L), M2(
			"Class M, Graduated Level 2", M_L), M2_L("Class M, Graduated Level 2 with Condition L"), A_R("Class A with Condition R", D, G), A(
			"Class A", D, G, A_R), B("Class B", C, D, E, F, G);

	private String description;
	private Set<LicenseType> includedTypes;

	LicenseType( String description, LicenseType... includedTypes ) {
		this.description = description;
		this.includedTypes = new HashSet<>();
		for ( LicenseType item : includedTypes ) {
			this.includedTypes.add( item );
		}
	}

	public boolean isIncluded( LicenseType type ) {
		return includedTypes.contains( type );
	}
	
	public String getDescription() {
		return description;
	}
}
