FROM openjdk:8-jdk-alpine

WORKDIR /app

# Copy file JAR đã build sẵn
COPY target/freshfood-1.0.jar app.jar

# Expose cổng (Render sẽ tự động ánh xạ dựa trên biến PORT)
EXPOSE 8080

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
