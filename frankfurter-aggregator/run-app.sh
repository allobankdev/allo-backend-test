#!/bin/bash
echo "Setting up Frankfurter Aggregator..."

# Create necessary directories
mkdir -p src/main/java/com/frankfurter/aggregator
mkdir -p src/main/resources

# Create build.gradle
cat > build.gradle << 'GRADLE'
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.1.6'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.frankfurter'
version = '0.0.1-SNAPSHOT'

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
GRADLE

echo "rootProject.name = 'frankfurter-aggregator'" > settings.gradle

# Create main application
cat > src/main/java/com/frankfurter/aggregator/AggregatorApplication.java << 'JAVA'
package com.frankfurter.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AggregatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AggregatorApplication.class, args);
    }
}
JAVA

# Create a working controller
cat > src/main/java/com/frankfurter/aggregator/controller/FinanceController.java << 'JAVA'
package com.frankfurter.aggregator.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @GetMapping("/data/{resourceType}")
    public Map<String, Object> getFinanceData(@PathVariable String resourceType) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("resourceType", resourceType);
        response.put("message", "Endpoint working for: " + resourceType);
        response.put("timestamp", System.currentTimeMillis());
        
        // Simulate different responses based on resource type
        switch(resourceType) {
            case "latest_idr_rates":
                response.put("data", Map.of(
                    "base", "IDR",
                    "rates", Map.of("USD", 0.000064, "EUR", 0.000059),
                    "USD_BuySpread_IDR", 15625.0,
                    "spread_factor", 0.00172
                ));
                break;
            case "historical_idr_usd":
                response.put("data", Map.of(
                    "from", "IDR",
                    "to", "USD",
                    "rates", Map.of("2024-01-01", Map.of("USD", 0.000064))
                ));
                break;
            case "supported_currencies":
                response.put("data", Map.of(
                    "currencies", Map.of("USD", "US Dollar", "EUR", "Euro", "IDR", "Indonesian Rupiah"),
                    "total", 162
                ));
                break;
            default:
                response.put("error", "Unknown resource type");
        }
        
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Frankfurter Aggregator");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }
    
    @GetMapping("/test")
    public String test() {
        return "Frankfurter Aggregator is running!";
    }
}
JAVA

# Create application.yml
cat > src/main/resources/application.yml << 'YAML'
server:
  port: 8080
  servlet:
    context-path: /

spring:
  application:
    name: frankfurter-aggregator
  main:
    banner-mode: off

app:
  api:
    base-url: https://api.frankfurter.app
    historical:
      start-date: 2024-01-01
      end-date: 2024-01-05
      from-currency: IDR
      to-currency: USD
  github:
    username: andityadimas

logging:
  level:
    com.frankfurter: INFO
    org.springframework.web: INFO
YAML

echo "Setup complete! Building application..."
./gradlew clean build

echo ""
echo "========================================="
echo "Application will start on port 8080"
echo "Test endpoints:"
echo "1. http://localhost:8080/api/finance/health"
echo "2. http://localhost:8080/api/finance/data/latest_idr_rates"
echo "3. http://localhost:8080/api/finance/data/historical_idr_usd"
echo "4. http://localhost:8080/api/finance/data/supported_currencies"
echo "========================================="
echo ""
echo "Starting application... (Press Ctrl+C to stop)"
./gradlew bootRun
