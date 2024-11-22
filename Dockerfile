# Use Maven base image to build the project
FROM maven:3.8.1-openjdk-8 AS build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK base image for runtime
FROM openjdk:8-jdk-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/freshfood-1.0.jar app.jar

# Expose the port Spring Boot application will run on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
