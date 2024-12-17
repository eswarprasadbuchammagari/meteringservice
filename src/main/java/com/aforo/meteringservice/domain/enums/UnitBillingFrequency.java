package com.aforo.meteringservice.domain.enums;

public enum UnitBillingFrequency {

	DAILY("DAILY"),
	MONTHLY("MONTHLY"),
	YEARLY("YEARLY"),
	QUARTERLY("QUARTERLY");
	private final String value;
	UnitBillingFrequency(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
