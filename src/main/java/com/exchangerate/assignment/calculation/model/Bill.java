package com.exchangerate.assignment.calculation.model;

import java.util.List;

public record Bill(String userType, // employee, affiliate, customer
		List<Items> items, String originalCurrency, String targetCurrency) {
}
