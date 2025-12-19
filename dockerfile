# Use Java 17
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the JAR from your local target folder
COPY target/mymemory-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
