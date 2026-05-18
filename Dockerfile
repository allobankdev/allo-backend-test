FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -q

COPY src/ src/
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 4110

ENTRYPOINT ["java", "-jar", "app.jar"]
