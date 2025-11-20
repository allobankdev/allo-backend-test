I. Setup/Run Intructions

    1. this application idr rate agregator using springboot 3.2.9, java 17 and for data using API public https://api.frankfurter.app/
    2. Build & Run
        a. mvn clean install
        b. mvn spring-boot:run
        or you can starting application direct from main class Application

II. Endpoint usage

    1. Latest IDR Rates
        curl --location 'http://localhost:9000/api/finance/data/LATEST_IDR_RATES' to test the resource type of latest_idr_rates
    2. Historical IDR/USD Rates
        curl --location 'http://localhost:9000/api/finance/data/HISTORICAL_IDR_USD' to test the resource type of historical_idr_usd
    3. Supported Currencies
        curl --location 'http://localhost:9000/api/finance/data/SUPPORTED_CURRENCIES' to test the resource type of available currency support

III. Personalization Note

    1. Github username RianCh140775
    2. unicode(ASCII) = 'R' -> 82
                        'i' -> 105
                        'a' -> 97
                        'n' -> 110
                        'C' -> 67
                        'H' -> 104
                        '1' -> 49
                        '4' -> 52
                        '0' -> 48
                        '7' -> 55
                        '7' -> 55
                        '5' -> 53
                        sum = 941
    Spread factor = (Sum of Unicode Values % 1000) / 100000.0
    Spread factor = (941 % 1000) / 100000.0
    Spread factor = 941 / 100000.0
    Spread factor = 0.00941

IV. Arsitcture rational

    1. Strategy Pattern
        - this pattern use to all of type resource to for put data using diffrent strategy or implementation, thats way this strategy pattern can handle some dinamis input
        - can be easy to add new fetch without change the runner/loader
        - maintain

    2. FactoryBean
        - make configuration more dynamic
        - fleksible to result diffrent object

    3. ApplicationRunner vs @PostConstruct
        - easy to test
        - more secure for initial data loading
