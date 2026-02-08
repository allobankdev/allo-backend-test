# Alternative Build Instructions

## Option 1: Download Maven Binary (Recommended)

```bash
# Download Maven
curl -O https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz

# Extract
tar -xzf apache-maven-3.9.9-bin.tar.gz

# Add to PATH temporarily
export PATH=$PWD/apache-maven-3.9.9/bin:$PATH

# Verify
mvn -version

# Build project
mvn clean install
mvn spring-boot:run
```

## Option 2: Use IDE

1. Open project in IntelliJ IDEA or Eclipse
2. IDE will auto-download dependencies
3. Run `IdrRateAggregatorApplication.java`

## Option 3: Use Docker

```bash
# Build with Docker
docker run -it --rm -v "$PWD":/app -w /app maven:3.9-eclipse-temurin-17 mvn clean install

# Run
docker run -it --rm -v "$PWD":/app -w /app -p 8080:8080 maven:3.9-eclipse-temurin-17 mvn spring-boot:run
```

## Test Endpoints After Running

```bash
curl http://localhost:8080/api/finance/data/latest_idr_rates
curl http://localhost:8080/api/finance/data/historical_idr_usd
curl http://localhost:8080/api/finance/data/supported_currencies
```
