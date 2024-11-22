FROM openjdk:8-jdk-alpine

WORKDIR /app

# Expose cổng 
EXPOSE 10000

# Copy file JAR đã build sẵn
COPY target/freshfood-1.0.jar app.jar

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
