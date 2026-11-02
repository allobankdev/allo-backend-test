FROM maven:3.9.16-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline
COPY src/ src/
RUN mvn -B -q verify

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
RUN mkdir -p /app/data && chown -R 10001:10001 /app
COPY --from=builder --chown=10001:10001 /app/target/split-bill-1.0.0.jar app.jar
USER 10001
ENV DATABASE_URL=jdbc:h2:file:/app/data/splitbill;DB_CLOSE_ON_EXIT=FALSE
EXPOSE 4110
ENTRYPOINT ["java", "-jar", "app.jar"]
