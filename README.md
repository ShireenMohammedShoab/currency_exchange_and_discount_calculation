This project is a simple spring boot application applying Currency Exchange and Discount Calculation which is as the following

1.	Third-Party API Integration:
	o	Integrate with a currency exchange API, such as ExchangeRate-API or Open Exchange Rates, to get real-time currency exchange rates.
	o	Use the API key (replace your-api-key in the URL below) to access exchange rates.
		Example endpoint: https://open.er-api.com/v6/latest/{base_currency}?apikey=your-api-key
2.	Discounts and Currency Conversion Logic:
	o	Apply discounts as per the following rules:
			If the user is an employee of the store, they get a 30% discount.
			If the user is an affiliate of the store, they get a 10% discount.
			If the user has been a customer for over 2 years, they get a 5% discount.
			For every $100 on the bill, there is a $5 discount.
			The percentage-based discounts do not apply to groceries.
			A user can get only one of the percentage-based discounts on a bill.
	o	Convert the bill total from the original currency to the target currency using the retrieved exchange rates.
	o	Calculate the final payable amount in the target currency after applying the applicable discounts.
3.	Authentication:
	o	Implement authentication for the exposed endpoints. 
4.	Endpoint Exposure:
	o	Expose an API endpoint (/api/calculate) to accept bill details including items, their categories, total amount, user type, customer tenure, original currency, and target currency.
	o	The endpoint should return the net payable amount in the specified target currency after applying applicable discounts and currency conversion.

## SonarLint Report: Project Executed using SonarLint to adhere code quality summary from eclipse STS.

## UML Digram

- [UML Digram] (/currency_exchange_and_discount_calculation/currency_exchange_and_discount_calculation.umlc)

## Requirements

For building and running the application you need:

- JDK 21
- [Maven 4.0.0](https://maven.apache.org)

## Running the application locally Successfully

There are several ways to run a Spring Boot application on your local machine. 
1. execute the `main` method in the `CurrencyExchangeAndDiscountCalculationApplication` class from your IDE.
  - Right click on Project
  - Select Run As
  - Select Java Application (JRE - Java 21)
  - Select Main Class `CurrencyExchangeAndDiscountCalculationApplication` and click on Run.
  
2. Run through command line 
	1. GO to the directory of project where pom.xml exists in windows
	2. mvn clean install OR  mvn spring-boot:run
3. Run as executable Runnable JAR 
        1. Cmd
        2. Go to path where currency_exchange_and_discount_calculation-0.0.1-SNAPSHOT.jar file present
        3. You can run the application from the command line using:
            java -jar currency_exchange_and_discount_calculation-0.0.1-SNAPSHOT.jar

## To Run Application Junit Test cases
	1. Right click on project from eclipse STS
	2. Select `Run As` option
	3. Select `Junit Test`
	4. Results can be stored in target/surefire-reports/TEST-com.exchangerate.CurrencyExchangeAndDiscountCalculationApplicationTests.xml file


To Test below use cases:
==============================
o	Expose an API endpoint (/api/calculate) to accept bill details including items, their categories, total amount, user type,
 customer tenure, original currency, and target currency.
o	The endpoint should return the net payable amount in the specified target currency after applying applicable discounts and currency conversion.

Run Project via Command Line:
====================================
	1. GO to the directory of project where pom.xml exists in windows
	2. mvn clean install OR  mvn spring-boot:run

NOTE: 
  1. Change isGrocery as "true" to test use case "The percentage-based discounts do not apply to groceries."
  2. Change userType field to "affiliate" or "oldCustomer" or "employee"

Test from Postman:
===========================

Headers:
Basic Auth
Username: user
Password: password123$

curl --location 'http://localhost:8080/api/calculate' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZDEyMyQ=' \
--header 'Cookie: JSESSIONID=E840E1DA3730FA80C89C540D7C71F64C' \
--data '{
    "items": [
        {
            "itemName": "Grinder",
            "category": "Electronics",
            "amount": 800.0
        }
    ],
    "userType": "affiliate",
    "originalCurrency": "USD",
    "targetCurrency": "EUR"
}'

## License
