# Allo Bank Test Application

This is a Spring Boot application that provides finance-related services, including currency exchange rates using the Frankfurter API.

## Prerequisites

- Java 21
- Maven 3.6+
- Docker and Docker Compose

## Running the Application

### Using Maven

1. Clone the repository
2. Navigate to the project directory
3. Set environment variables (optional, defaults are provided):
   ```bash
   FRANKFURTHER_URL=https://api.frankfurter.app
   FRANKFURTHER_TIMEOUT=5000
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`.

### Using Docker Compose (Recommended)

1. Ensure Docker and Docker Compose are installed
2. Run the application:
   ```bash
   docker-compose up --build
   ```

   Or run in background:
   ```bash
   docker-compose up -d --build
   ```

3. Stop the application:
   ```bash
   docker-compose down
   ```

The application will be available at `http://localhost:8080`.

## Testing with Swagger

This application includes Swagger UI for API documentation and testing.

1. Start the application (using either Maven or Docker method above)
2. Open your browser and navigate to: `http://localhost:8080/swagger`
3. You will see the Swagger UI interface with all available endpoints
4. You can test the APIs directly from the Swagger UI:
   - Expand the endpoint you want to test
   - Click "Try it out"
   - Enter any required parameters
   - Click "Execute" to make the API call
   - View the response below