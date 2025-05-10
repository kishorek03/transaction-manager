# Use Java 21 base image
FROM eclipse-temurin:21-jdk

# Create app directory
WORKDIR /app

# Copy the built JAR into the image
COPY target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
