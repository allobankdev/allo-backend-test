# Multi-stage build for Allo Bank Backend Challenge
# Stage 1: Build 
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Install maven
RUN apk add --no-cache maven

# Copy pom first (layer cache for dependencies)
COPY pom.xml ./
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src/ src/
RUN mvn package -DskipTests -q

# Stage 2: Runtime 
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 4110

ENTRYPOINT ["java", "-jar", "app.jar"]
