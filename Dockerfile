# Use OpenJDK 8 as the base image (phù hợp với Java version trong POM)
FROM openjdk:8-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/freshfood-1.0.jar app.jar

# Expose the port Spring Boot application will run on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
