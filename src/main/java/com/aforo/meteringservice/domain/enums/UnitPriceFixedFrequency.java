package com.aforo.meteringservice.domain.enums;

public enum UnitPriceFixedFrequency {
	MONTHLY("MONTHLY"), 
	DAILY("DAILY"),
	HOURLY("HOURLY"), 
	YEARLY("YEARLY"),
	TWICE_A_MONTH("TWICE_A_MONTH");
	
	
	private final String value;
	UnitPriceFixedFrequency(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
