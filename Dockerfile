# Stage 1: Build stage using Maven
FROM maven:3.8.1-openjdk-8 AS build

# Set the working directory in the container
WORKDIR /app

# Add custom Maven settings for better dependency resolution
COPY settings.xml /root/.m2/settings.xml

# Copy only the Maven project descriptor to leverage caching for dependencies
COPY pom.xml .

# Download dependencies (this step is cached if pom.xml does not change)
RUN mvn dependency:go-offline -B

# Copy the full project into the container
COPY src ./src

# Build the application (skip tests for faster build in Docker)
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage using OpenJDK
FROM openjdk:8-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/freshfood-1.0.jar app.jar

# Expose the application port (Spring Boot is configured to run on port 8081)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
