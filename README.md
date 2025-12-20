# Prerequisites
1. Java 25
2. Git
3. Maven


### To Clone the Repository
go to repo then copy the link and run the following command in terminal:

```bash
git clone <your-repository-url>
cd <project-directory-name>
```

### Run the application
on the project directory, you can run the application directly using the build tool or by executing the generated jar file.

#### Using maven
```bash
mvn spring-boot:run
```

#### Using generated jar file
Build the project first:

```bash
mvn clean package
```
then run the jar file:
```bash
java -jar target/backendtest-0.0.1-SNAPSHOT.jar
```

### Api Endpoints (running locally)
latest IDR rates:
```bash
curl "http://localhost:8080/api/finance/data/latest_idr_rates" | jq
```
historical IDR rates between two dates:
```bash
curl "http://localhost:8080/api/finance/data/historical_idr_usd" | jq
```
list of supported currencies:
```bash
curl "http://localhost:8080/api/finance/data/supported_currencies" | jq
```

### Personalization Notes
#### GitHub username : MrPandoyo
#### Spread Factor : 0.00985

## Architectural Rationale

#### Polymorphism Justification
Strategy pattern is used with future or scalability in mind, let me give an example : 
for now the implementation of IdrDataFetcher is only 3, but who knows maybe theres gonna be more. if we were to use conditional block inside the controller or service its gonna be a hassle to update it each time a new one pop up.
it makes the code easier to read and maintain.

#### Client Factory
Client factory is used in case there would be another API client to be added in the future, so the code is more maintainable and scalable.
its used over normal @Bean because it put complex setup (authentication, timeouts, retries, header) inside a dedicated class rather than cluttering a configuration file.

#### Startup Runner Choice (ApplicationRunner over @PostConstruct)
@PostConstruct is being run as soon as the individual bean is initialized, but before the full ApplicationContext is fully "ready." The application is still in its cooling-down phase of startup.
while application runner is being run after application context is fully loaded, and in this application we need to make an external call which need the application context to be fully ready.
