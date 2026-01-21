# Use Eclipse Temurin OpenJDK 21 as the base image for building
FROM eclipse-temurin:21-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Make mvnw executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Use Eclipse Temurin OpenJDK 21 JRE for the runtime image
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Set environment variables (can be overridden)
ENV FRANKFURTHER_URL=https://api.frankfurter.app
ENV FRANKFURTHER_TIMEOUT=5000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]