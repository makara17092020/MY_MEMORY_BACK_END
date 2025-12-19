# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS build

# Set working directory
WORKDIR /app

# Copy Maven files and source code
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Build the JAR (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/mymemory-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]