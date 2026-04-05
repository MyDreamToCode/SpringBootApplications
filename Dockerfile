# Multi-stage Dockerfile for building and running the Spring Boot application

# Build stage: use Maven image with JDK 17 to build the artifact
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
# copy only dependency descriptors first for better caching
COPY pom.xml .
COPY src ./src

# Package the application (skip tests in CI build by default)
RUN mvn -B -DskipTests package

# Run stage: use a lightweight JRE image
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the built war from the build stage (adjust filename if you change artifactId/version)
COPY --from=build /app/target/studentregistration-0.0.1-SNAPSHOT.war ./app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.war"]

