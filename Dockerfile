# -------- BUILD STAGE --------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package

# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar backendtest-0.0.1-SNAPSHOT.jar.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backendtest-0.0.1-SNAPSHOT.jar.jar"]