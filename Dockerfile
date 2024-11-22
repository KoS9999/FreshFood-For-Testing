# Sử dụng base image Java
FROM openjdk:17-jdk-slim

# Tạo thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ `target`
COPY target/freshfood-1.0.jar app.jar

# Giải nén file JAR để đảm bảo tất cả tài nguyên được sử dụng
RUN mkdir -p /app/resources && \
    cd /app && \
    jar -xf app.jar

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-cp", "/app:/app/resources", "org.springframework.boot.loader.JarLauncher"]
