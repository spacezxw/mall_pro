# 基础镜像，使用 OpenJDK 11
FROM openjdk:11-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 JAR 文件到容器
COPY target/mall-admin-1.0-SNAPSHOT.jar app.jar

# 暴露端口（与 docker-compose 中的 8080 匹配）
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]