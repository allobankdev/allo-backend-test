# Finance API - IDR Exchange Rate Aggregator

A production-ready Spring Boot application that aggregates and serves IDR exchange rate data from the Frankfurter API.

## 🚀 Features

- **Strategy Pattern Implementation**: Clean separation of concerns for different data resources
- **Thread-safe Immutable Data Store**: Data loaded once at startup, served immutably
- **FactoryBean for WebClient**: Custom configuration and lifecycle management
- **ApplicationRunner**: Robust data initialization with timeout handling
- **Comprehensive Error Handling**: Graceful degradation and meaningful error responses
- **Production-ready Testing**: Unit and integration tests for all components

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Internet connection (to access Frankfurter API)

## 🛠️ Setup & Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/finance-api.git
   cd finance-api