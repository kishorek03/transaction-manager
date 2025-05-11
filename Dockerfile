# Stage 1: Build the application using Maven
FROM eclipse-temurin:21-jdk as builder

WORKDIR /app

# Copy Maven wrapper and project files
COPY . .

RUN chmod +x ./mvnw

# Build the project (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the built JAR
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
