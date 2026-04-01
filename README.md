# prerequisite
Java version 21.0.5
Gradle version 8.14.3

# step by step
* Clone
	git clone <url>
	
* Open terminal (Use Powershell)

* Open application directory
	cd </path/to/source/location>
	example: cd Z:\bs

* Refresh Gradle Dependencies
	gradle --refresh-dependencies
	
* Build using gradle command
	gradle build

* Run application
	cd <path/to/jar/location>
	example: cd Z:\bs\build\libs

	java -jar <jar_names>
	example: java -jar bs-0.0.1-SNAPSHOT.jar
	
* Test API 'supported_currencies'
	curl.exe "http://127.0.0.1:8080/api/finance/data/supported_currencies"

* Test API 'historical_idr_usd'
	curl.exe "http://127.0.0.1:8080/api/finance/data/historical_idr_usd?dateFrom=2026-03-25&dateTo=2026-04-01"

* Test API 'latest_idr_rates'
	curl.exe "http://127.0.0.1:8080/api/finance/data/latest_idr_rates"

# Personalization Note
* Spread Factor : 0.0037

# Architectural Rationale
i.
	With the Strategy Pattern, the code will be easier to read, debug, and modify.
	in extensibility, When adding a new resource type doesn’t require changing existing logic.
	in maintainability, Each strategy is independent, making it easier to read, test, and modify.
	
ii.
	If using a FactoryBean, it will define when it is created.
	
iii.
	CommandLineRunner is preferable because it executes after the entire Spring Boot application context completed.