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

# Set timezone to IST
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Kolkata"
ENV TZ=Asia/Kolkata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/transaction-manager-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
