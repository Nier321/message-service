# 使用官方 Java 镜像作为基础镜像
FROM openjdk:18-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将项目的 JAR 文件复制到容器中
COPY target/message-service-2.0.0.jar /app.jar

# 设置容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "/app.jar"]

# 设置健康检查命令
HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

# 暴露端口
EXPOSE 8080