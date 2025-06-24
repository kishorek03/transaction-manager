# Use JDK base image
FROM eclipse-temurin:21-jdk

# Set working directory inside container
WORKDIR /app

# Copy the built JAR file (use wildcard for flexibility)
COPY target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
