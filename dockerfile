# Use an official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot JAR into the container
COPY target/mymemory-0.0.1-SNAPSHOT.jar app.jar

# Expose port if needed
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
