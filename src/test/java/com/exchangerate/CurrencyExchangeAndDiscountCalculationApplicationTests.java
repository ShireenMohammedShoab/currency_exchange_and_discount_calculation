package com.exchangerate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.exchangerate.assignment.calculation.model.Bill;
import com.exchangerate.assignment.calculation.model.Items;
import com.exchangerate.assignment.calculation.service.ExchangeRateAndBillCalculationService;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeAndDiscountCalculationApplicationTests {

	private static final Logger logger = LoggerFactory
			.getLogger(CurrencyExchangeAndDiscountCalculationApplicationTests.class);

	@InjectMocks
	private ExchangeRateAndBillCalculationService billCalculationService;

	@Test
	void contextLoads() {
		logger.info("---------- Context Loaded ----------");
	}
	
	@Mock
	List<Items> items;

	/**
	 * 30% discount to the Employee
	 */
	@Test
	void testEmployeeUserDiscount() {
		items = new ArrayList<>();

		items.add(new Items("Fruits", 80, "grocery"));
		items.add(new Items("Seat Covers", 220, "Car Accessories"));
		Bill bill = new Bill("employee", items, "USD", "EUR"); // Bill only for Grocery Items
		//Bill bill = new Bill("employee", FALSE, 120, "USD", "EUR"); // Bill only for  Non- Grocery Items		
		double result = billCalculationService.calculateDiscount(bill);
		logger.info("---------- UserType Employee whose total payable amount is {}: ----------", result);
		assertEquals(Double.valueOf(138.7), result);
	}

	/**
	 * 5% discount to old customer
	 */
	@Test
	void testOldCustomerSegmentDicount() {
		items = new ArrayList<>();

		items.add(new Items("T-Shirts", 400, "Cloths"));
		Bill bill = new Bill("oldCustomer", items, "USD", "EUR");
		double result = billCalculationService.calculateDiscount(bill);
		logger.info("---------- UserType Old Customer whose total payable amount is {}: ----------", result);
		assertEquals(Double.valueOf(190.0), result);
	}

	/**
	 * 10% discount to the Affiliate
	 */
	@Test
	void testAffiliateUserDiscount() {
		items = new ArrayList<>();

		items.add(new Items("Grinder", 1500, "Electronics"));
		Bill bill = new Bill("affiliate", items, "USD", "EUR");
		double result = billCalculationService.calculateDiscount(bill);
		logger.info("---------- UserType Affiliate whose total payable amount is {}: ----------", result);
		assertEquals(Double.valueOf(142.5), result);
	}
	

	/**
	 * Convert the bill total from the original currency to the target currency using the retrieved exchange rates.
	 */
	@Test
	void totalBillFromOriginalCurrencyToTargetCurrentUsingExchangeRates() {
		items = new ArrayList<>();

		items.add(new Items("Grinder", 340, "Electronics"));
		Bill bill = new Bill("affiliate", items, "USD", "USD");
		double result = billCalculationService.fetchExchangeRate(bill.originalCurrency(), bill.targetCurrency());
		logger.info("---------- Convert the bill total from the original currency to the target currency using the retrieved exchange rates. {}: ----------", result);
		assertEquals(Double.valueOf(1.0), result);
	}
	
	/**
	 * Calculate the final payable amount in the target currency after applying the applicable discounts.
	 */
	@Test
	void finalPayableAmountAfterApplyingDiscounts() {
		items = new ArrayList<>();

		items.add(new Items("Grinder", 800, "Electronics"));
		Bill bill = new Bill("affiliate", items , "USD", "EUR");
		double result = billCalculationService.calculateTotalPayableAmount(bill);
		logger.info("---------- Calculate the final payable amount in the target currency after applying the applicable discounts. {}: ----------", result);
		assertEquals(Double.valueOf(68.990444), result);
	}
	
}