# 前端项目运行指南

## 方法一：开发模式运行（推荐）

### 1. 进入前端目录
```bash
cd ai-code-review/frontend
```

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
npm run dev
```

### 4. 访问前端
打开浏览器访问：http://localhost:3000

## 方法二：构建后运行

### 1. 安装依赖
```bash
npm install
```

### 2. 构建生产版本
```bash
npm run build
```

### 3. 构建后的文件位置
构建后的静态文件会自动生成到 `../backend/src/main/resources/static` 目录

### 4. 重启后端服务
重启后端Spring Boot应用，即可通过 http://localhost:8080 访问

## 注意事项

1. 确保后端服务已经启动（端口8080）
2. 开发模式下，前端运行在3000端口，会自动代理API请求到8080端口
3. 如果遇到依赖安装问题，可以尝试：
   ```bash
   npm install --registry=https://registry.npmmirror.com
   ```

## 常见问题

1. **端口被占用**
   - 修改 `vite.config.js` 中的端口号
   - 或者停止占用3000端口的其他服务

2. **API请求失败**
   - 检查后端服务是否正常运行
   - 确认 `vite.config.js` 中的代理配置正确

3. **依赖安装慢**
   - 使用淘宝镜像：`npm config set registry https://registry.npmmirror.com`