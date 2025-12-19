# Stage 1: Build the JAR
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copy Maven wrapper and config first (for caching)
COPY pom.xml mvnw .mvn/ ./
RUN chmod +x mvnw

# Copy source code
COPY src ./src

# Build the JAR (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Create minimal runtime image
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/mymemory-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
