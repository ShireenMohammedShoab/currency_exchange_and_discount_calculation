package com.exchangerate.assignment.calculation.response;

import java.util.Map;

public class CurrencyRateResponse {
	private Map<Object, Double> rates;

	public Map<Object, Double> getRates() {
		return rates;
	}

	public void setRates(Map<Object, Double> rates) {
		this.rates = rates;
	}
}
