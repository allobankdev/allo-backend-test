# Allo Bank Backend Developer Test Application

### Disclaimer
You may see that some of the commits are committed using the username `aditya-sukoco-aid` instead of `robert-adit-sukoco`. This is because
the last time I used my own computer for coding is when I was working at `PT Akar Inti Data` (which was July 2025), and when I was working
on this assignment, I actually forgot to change the configs for the username and email until the very last moments of working this assignment.
But I just wanna clarify that both of these usernames actually belong to me, `aditya-sukoco-aid` is a work account, and `robert-adit-sukoco` is
a personal account. `PT Akar Inti Data` won't be able to see the activities I made using `aditya-sukoco-aid` for certain reasons.

## Requirements
1. Java SDK version 21
2. Gradle Groovy
3. Internet connection with `https://api.frankfurter.app` not being in your connection's blacklist

## How to Run
1. Install dependencies using `./gradlew build`
2. Run the app using `./gradlew bootRun`
3. Enjoy

## Unique Factors
1. Github username = `robert-adit-sukoco`
2. Unicode Sum = `1822`
2. Spread factor = `0.00822`

## Answers to Architectural Rationale Questions
1.  **Polymorphism Justification:**
The strategy pattern is preferred in this case because using traditional if/else would result in a **really** long if else block, making the code dirty.
Using strategy pattern would also benefit the maintainability of the code, making it easier to add a new condition to the existing flow.
2.  **Client Factory:** 
`FactoryBean` is preferred when the construction of a Bean requires complex steps and/or a handful of configurations that would be 
tedious if we were to use a simple `@Bean` annotation. In this case, external API clients usually require certain configurations 
(e.g. Headers, API Keys), and this is where `FactoryBean` shines.
3.  **Startup Runner Choice:** In this case, using `ApplicationRunner` (or `CommandLineRunner`) is preferable than using the `@PostConstruct` annotation.
`@PostConstruct` runs  after the said Bean is successfully initialized. If we were to use `@PostConstruct`, the API fetching process of each repository 
would block the initialization of other Beans, we don't want this happening because it would increase the application's startup time. Meanwhile, 
`ApplicationRunner` runs after all Beans have been successfully initialized, this means that the API fetching process will not block the application's startup time.

## Endpoints
1. Fetch latest data
```bash
curl --location 'http://localhost:8081/api/finance/data/latest_idr_rates' 
```
2. Fetch historical data
```bash
curl --location 'http://localhost:8081/api/finance/data/historical_idr_usd'
```
3. Fetch latest data
```bash
curl --location 'http://localhost:8081/api/finance/data/supported_currencies'
```

