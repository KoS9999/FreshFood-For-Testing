# Sử dụng base image Java
FROM openjdk:17-jdk-slim

# Tạo thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ `target`
COPY target/freshfood-1.0.jar app.jar

# Giải nén file JAR để đảm bảo tất cả tài nguyên được sử dụng
RUN mkdir -p /app/resources /app/upload && \
    jar -xf app.jar && \
    mv /app/BOOT-INF/classes/upload/images /app/upload/images && \
    ls -l /app/upload/images

# Đảm bảo thư mục `upload` tồn tại và có quyền truy cập
RUN mkdir -p /app/upload && chmod -R 755 /app/upload

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-cp", "/app:/app/resources", "org.springframework.boot.loader.JarLauncher"]
