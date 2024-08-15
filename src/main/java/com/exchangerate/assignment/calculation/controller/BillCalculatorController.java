package com.exchangerate.assignment.calculation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangerate.assignment.calculation.model.Bill;
import com.exchangerate.assignment.calculation.service.ExchangeRateAndBillCalculationService;

@RestController
@RequestMapping("/api")
public class BillCalculatorController {
	@Autowired	private ExchangeRateAndBillCalculationService service;

	@PostMapping("/calculate")
	public ResponseEntity<Double> calculate(@RequestBody Bill bill) {
		double finalAmount = service.calculateTotalPayableAmount(bill);
		return ResponseEntity.ok(finalAmount);
	}
}
