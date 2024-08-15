package com.exchangerate.assignment.calculation.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.exchangerate.assignment.calculation.model.Bill;
import com.exchangerate.assignment.calculation.model.Items;
import com.exchangerate.assignment.calculation.response.CurrencyRateResponse;

@Service
public class ExchangeRateAndBillCalculationService {
	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateAndBillCalculationService.class);

	@Value("${api.url}")
	private String apiUrl;

	@Value("${api.key}")
	private String apiKey;

	public double calculateDiscount(Bill bill) {
		// Check if any item is a grocery
		double discountAmount = 0;
		double extraFivePercentOffOnGroceryItems = 0;
		double totalAmount = getTotalAmount(bill.items());
		double getTotalAmountForGroceryItems = getTotalAmountForGroceryItems(bill.items());
		for (Items item : bill.items()) {
			if (!item.category().equalsIgnoreCase("grocery")) {
				discountAmount = switch (bill.userType().toLowerCase()) {
				case "employee" -> totalAmount * 0.30;
				case "affiliate" -> totalAmount * 0.10;
				case "oldcustomer" -> totalAmount * 0.50;
				default -> 0;
				};
			} else {
			// Apply $5 discount for every $100 for grocery items
				extraFivePercentOffOnGroceryItems = getTotalAmountForGroceryItems
						- Math.floor(getTotalAmountForGroceryItems / 100) * 5;
			}
		}

		// Apply $5 discount for every $100 on the discounted amount for non-grocery
		// items
		double extraFiveDiscount = Math.floor(discountAmount / 100) * 5;
		return (discountAmount + extraFivePercentOffOnGroceryItems) - extraFiveDiscount;
	}

	// Total Amount
	private double getTotalAmount(List<Items> items) {
		return items.stream().filter(item -> !item.category().equalsIgnoreCase("grocery")).mapToDouble(Items::amount)
				.sum();
	}

	// Total Amount
	private double getTotalAmountForGroceryItems(List<Items> items) {
		return items.stream().filter(item -> item.category().equalsIgnoreCase("grocery")).mapToDouble(Items::amount)
				.sum();
	}

	@PreAuthorize("hasRole('USER')") // Using Spring Security
	public double calculateTotalPayableAmount(Bill bill) {
		double finalAmount = calculateDiscount(bill);

		double exchangeRate = fetchExchangeRate(bill.originalCurrency(), bill.targetCurrency());
		return finalAmount * exchangeRate;
	}

	public double fetchExchangeRate(String originalCurrency, String targetCurrency) {

		String exchangeRateUrl = apiUrl != null && apiKey != null ? apiUrl + originalCurrency + "?apikey=" + apiKey
				: "https://open.er-api.com/v6/latest/" + originalCurrency + "?apikey=your-api-key";
		logger.debug("Exchange Rate URI: {} and Original Currency: {} and Targeted Currency: {}", exchangeRateUrl,
				originalCurrency, targetCurrency);

		WebClient webClient = WebClient.create();
		CurrencyRateResponse response = webClient.get().uri(exchangeRateUrl).retrieve()
				.bodyToMono(CurrencyRateResponse.class).block();

		logger.debug("Response of Currency Exchange Rate Client: {}", response);
		double totalPayableAmountAfterDiscounts = 0;
		if (response != null) {
			totalPayableAmountAfterDiscounts = response.getRates().get(targetCurrency);
		}

		return totalPayableAmountAfterDiscounts;

	}
}
