
### 1. Setup/Run Instructions

* **Prerequisite :** 
  * `Java 11.0.20`.
  * `Git`
* **Steps to clone the repository :** 
  * `git clone https://github.com/devinovski/allo-backend-test.git`.
  * `git checkout -b feat/idr-rate-aggregator`
  * `git pull origin feat/idr-rate-aggregator`

  * **Build and run steps**:

     * Move to the project directory
     * Run the `mvn clean package -DskipTests` command (skip the unittest)
     * Run the JAR file by executing this command: `java -jar target/[the name of the build package].jar`

### 2. Endpoint Usage
  There is one single endpoint `http://localhost:8080/api/finance/data/{resourceType}`. The endpoint containing a path variable named `{resourceType}`.
  It can be replaced with one of the three strings: latest_idr_rates, historical_idr_usd, or supported_currencies.
  
  The API can be exposed by using `curl` command. Example:
  
  **Request**:
  
  `curl http://localhost:8080/api/finance/data/supported_currencies`
  
  **Response**:

```json
{"status":"200 OK","data":{"currencies":{"CHF":"Swiss Franc","MXN":"Mexican Peso","ZAR":"South African Rand","INR":"Indian Rupee","CNY":"Chinese Renminbi Yuan","THB":"Thai Baht","AUD":"Australian Dollar","ILS":"Israeli New Sheqel","KRW":"South Korean Won","JPY":"Japanese Yen","PLN":"Polish Złoty","GBP":"British Pound","IDR":"Indonesian Rupiah","HUF":"Hungarian Forint","PHP":"Philippine Peso","TRY":"Turkish Lira","HKD":"Hong Kong Dollar","ISK":"Icelandic Króna","EUR":"Euro","DKK":"Danish Krone","CAD":"Canadian Dollar","MYR":"Malaysian Ringgit","USD":"United States Dollar","BGN":"Bulgarian Lev","NOK":"Norwegian Krone","RON":"Romanian Leu","SGD":"Singapore Dollar","CZK":"Czech Koruna","SEK":"Swedish Krona","NZD":"New Zealand Dollar","BRL":"Brazilian Real"}}}