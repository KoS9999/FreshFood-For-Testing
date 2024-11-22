# Base image for running Java applications
FROM openjdk:8-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the pre-built JAR file from the local system to the container
COPY target/freshfood-1.0.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
