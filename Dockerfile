# Sử dụng Maven image làm builder để build ứng dụng
FROM maven:3.8.4-openjdk-17 AS builder

# Tạo thư mục làm việc trong image
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Build ứng dụng bằng Maven, bỏ qua các bài test
RUN mvn clean package -DskipTests


# Sử dụng OpenJDK slim image để chạy ứng dụng
FROM openjdk:17-jdk-slim

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ stage builder
COPY --from=builder /app/target/*.jar app.jar

# Sao chép template vào đúng thư mục để Spring Boot có thể tìm thấy
COPY src/main/resources/templates /app/templates

# Thiết lập biến môi trường để Spring Boot biết đường dẫn template
ENV SPRING_TEMPLATES_PATH=/app/templates

# Expose cổng 8080 để ứng dụng chạy
EXPOSE 8080

# Command mặc định để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
