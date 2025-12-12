# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the built jar from target folder
COPY target/mymemory-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
