# 多阶段构建
FROM maven:3.8.4-openjdk-8 AS builder

WORKDIR /app

# 复制后端代码
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

COPY backend/src ./src

# 构建后端
RUN mvn clean package -DskipTests -B

# 前端构建阶段
FROM node:16-alpine AS frontend-builder

WORKDIR /app/frontend

# 复制前端代码
COPY frontend/package*.json ./
RUN npm install

COPY frontend/ ./
RUN npm run build

# 运行阶段
FROM openjdk:8-jre-slim

WORKDIR /app

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 复制后端jar包
COPY --from=builder /app/target/*.jar app.jar

# 复制前端静态资源
COPY --from=frontend-builder /app/frontend/dist ./static

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# 设置JVM参数（适合单容器部署）
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]