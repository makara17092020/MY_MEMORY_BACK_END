# Use Eclipse Temurin Java 17 JDK
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy your Spring Boot JAR
COPY target/mymemory-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
    