FROM openjdk:8-jdk-alpine

WORKDIR /app

# Copy the pre-built JAR file from the target folder to the container
COPY target/freshfood-1.0.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
