# 手办阁 - 个人手办电子橱窗

一个精美的个人手办收藏展示平台，支持访客浏览、用户登录注册、手办增删改查、图片上传、点赞标记等功能。

## 📋 功能特性

### 访客功能
- 无需登录即可浏览手办展示墙
- 查看每个手办的详细信息
- 浏览热门手办榜单
- 所有列表支持分页
- 图片支持灯箱放大效果

### 用户功能
- 注册/登录账号
- 增删改手办信息
- 上传多张手办图片
- 设置公开/私密展示状态
- 自定义分类和标签
- 给喜欢的手办点赞
- 标记想要的手办

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 3.2
- **语言**: Java 17
- **数据库**: MySQL
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **构建工具**: Maven

### 前端
- **框架**: Angular 17
- **语言**: TypeScript
- **样式**: CSS
- **HTTP客户端**: HttpClient
- **路由**: Angular Router

## 📁 项目结构

```
MyFavor/
├── backend/                    # 后端项目
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── myfavor/
│           │           ├── MyFavorApplication.java
│           │           ├── config/
│           │           │   └── WebMvcConfig.java
│           │           ├── controller/
│           │           │   ├── AuthController.java
│           │           │   ├── CategoryController.java
│           │           │   ├── FigureController.java
│           │           │   ├── FileUploadController.java
│           │           │   ├── LikeController.java
│           │           │   ├── TagController.java
│           │           │   └── WantController.java
│           │           ├── dto/
│           │           │   ├── ApiResponse.java
│           │           │   ├── FigureCreateRequest.java
│           │           │   ├── FigureDTO.java
│           │           │   ├── FigureUpdateRequest.java
│           │           │   ├── JwtResponse.java
│           │           │   ├── LoginRequest.java
│           │           │   ├── PageResponse.java
│           │           │   └── RegisterRequest.java
│           │           ├── entity/
│           │           │   ├── Category.java
│           │           │   ├── Figure.java
│           │           │   ├── FigureImage.java
│           │           │   ├── FigureTag.java
│           │           │   ├── Like.java
│           │           │   ├── Tag.java
│           │           │   ├── User.java
│           │           │   └── Want.java
│           │           ├── repository/
│           │           │   ├── CategoryRepository.java
│           │           │   ├── FigureImageRepository.java
│           │           │   ├── FigureRepository.java
│           │           │   ├── FigureTagRepository.java
│           │           │   ├── LikeRepository.java
│           │           │   ├── TagRepository.java
│           │           │   ├── UserRepository.java
│           │           │   └── WantRepository.java
│           │           ├── security/
│           │           │   ├── AuthEntryPointJwt.java
│           │           │   ├── AuthTokenFilter.java
│           │           │   ├── JwtUtils.java
│           │           │   ├── SecurityConfig.java
│           │           │   ├── UserDetailsImpl.java
│           │           │   └── UserDetailsServiceImpl.java
│           │           └── service/
│           │               ├── CategoryService.java
│           │               ├── FigureService.java
│           │               ├── FileStorageService.java
│           │               ├── LikeService.java
│           │               ├── TagService.java
│           │               ├── UserService.java
│           │               └── WantService.java
│           └── resources/
│               ├── application.yml
│               └── schema.sql
├── frontend/                   # 前端项目
│   ├── angular.json
│   ├── package.json
│   ├── proxy.conf.json
│   ├── tsconfig.json
│   └── src/
│       ├── app/
│       │   ├── app.component.ts/html/css
│       │   ├── app.module.ts
│       │   ├── app-routing.module.ts
│       │   ├── components/
│       │   │   ├── home/
│       │   │   ├── login/
│       │   │   ├── register/
│       │   │   ├── figure-detail/
│       │   │   ├── figure-manage/
│       │   │   ├── figure-form/
│       │   │   └── user-figures/
│       │   ├── guards/
│       │   │   └── auth.guard.ts
│       │   ├── interceptors/
│       │   │   └── auth.interceptor.ts
│       │   ├── models/
│       │   │   ├── figure.model.ts
│       │   │   └── user.model.ts
│       │   └── services/
│       │       ├── auth.service.ts
│       │       ├── category.service.ts
│       │       ├── figure.service.ts
│       │       ├── like.service.ts
│       │       ├── tag.service.ts
│       │       ├── toast.service.ts
│       │       ├── upload.service.ts
│       │       └── want.service.ts
│       ├── index.html
│       ├── main.ts
│       └── styles.css
└── README.md
```

## 🚀 启动步骤

### 环境要求
- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+
- Angular CLI 17+

### 1. 数据库初始化

```sql
-- 执行 backend/src/main/resources/schema.sql 中的 SQL 脚本
-- 或者直接在 MySQL 中创建数据库

CREATE DATABASE IF NOT EXISTS myfavor_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置数据库连接

编辑 `backend/src/main/resources/application.yml` 文件，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myfavor_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root          # 修改为你的 MySQL 用户名
    password: root          # 修改为你的 MySQL 密码
```

### 3. 启动后端服务

```bash
cd backend

# 安装依赖并启动
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/myfavor-backend-1.0.0.jar
```

后端服务将在 `http://localhost:8080` 启动

### 4. 启动前端服务

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm start

# 或者使用 Angular CLI
ng serve
```

前端服务将在 `http://localhost:4200` 启动

## ✅ 验证步骤

### 1. 验证后端 API

使用浏览器或 Postman 访问以下地址：

| 接口 | 方法 | 说明 |
|------|------|------|
| http://localhost:8080/api/figures/public | GET | 公开手办列表 |
| http://localhost:8080/api/figures/popular | GET | 热门榜单 |
| http://localhost:8080/api/auth/register | POST | 用户注册 |
| http://localhost:8080/api/auth/login | POST | 用户登录 |

### 2. 验证前端页面

打开浏览器访问 `http://localhost:4200`

#### 测试流程：

1. **访客浏览**
   - 无需登录即可看到首页展示墙
   - 点击手办卡片查看详情
   - 点击图片可放大（灯箱效果）
   - 测试分页功能

2. **用户注册**
   - 点击右上角"注册"按钮
   - 填写用户名、邮箱、密码
   - 点击注册，提示成功后跳转到登录页

3. **用户登录**
   - 输入用户名和密码
   - 点击登录，成功后跳转到首页
   - 右上角显示用户名

4. **添加手办**
   - 点击"我的手办"进入管理页面
   - 点击"添加手办"按钮
   - 填写手办信息，上传图片
   - 提交后返回管理列表

5. **编辑手办**
   - 在管理列表点击"编辑"
   - 修改手办信息
   - 提交保存

6. **点赞和标记想要**
   - 在手办详情页
   - 点击"点赞"按钮
   - 点击"标记想要"按钮
   - 刷新页面验证状态保存

### 3. 测试数据

数据库初始化脚本中包含了测试数据：

| 用户名 | 密码 | 说明 |
|--------|------|------|
| admin | 123456 | 测试管理员 |
| user1 | 123456 | 测试用户 |

## 📚 API 文档

### 认证相关
| 接口 | 方法 | 认证 | 说明 |
|------|------|------|------|
| /api/auth/register | POST | 否 | 用户注册 |
| /api/auth/login | POST | 否 | 用户登录 |

### 手办相关
| 接口 | 方法 | 认证 | 说明 |
|------|------|------|------|
| /api/figures/public | GET | 否 | 公开手办列表（分页） |
| /api/figures/popular | GET | 否 | 热门榜单（分页） |
| /api/figures/most-viewed | GET | 否 | 最多浏览（分页） |
| /api/figures/search | GET | 否 | 搜索手办（分页） |
| /api/figures/{id} | GET | 否 | 手办详情 |
| /api/figures/my | GET | 是 | 我的手办列表（分页） |
| /api/figures | POST | 是 | 添加手办 |
| /api/figures/{id} | PUT | 是 | 更新手办 |
| /api/figures/{id} | DELETE | 是 | 删除手办 |

### 互动相关
| 接口 | 方法 | 认证 | 说明 |
|------|------|------|------|
| /api/likes/{figureId} | POST | 是 | 切换点赞 |
| /api/likes/{figureId} | GET | 否 | 检查点赞状态 |
| /api/wants/{figureId} | POST | 是 | 切换想要 |
| /api/wants/{figureId} | GET | 否 | 检查想要状态 |

### 文件上传
| 接口 | 方法 | 认证 | 说明 |
|------|------|------|------|
| /api/upload | POST | 是 | 上传单张图片 |
| /api/upload/multiple | POST | 是 | 上传多张图片 |

## 🔧 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 确认数据库用户名密码正确
- 确认数据库已创建

### 2. 前端无法访问后端
- 检查后端服务是否正常启动（端口 8080）
- 确认代理配置正确（proxy.conf.json）
- 检查浏览器控制台是否有跨域错误

### 3. 图片上传失败
- 检查 `uploads` 目录权限
- 确认后端配置文件中 `file.upload.path` 路径正确
- 检查文件大小是否超过限制（默认 10MB）

## 📝 开发说明

### 后端开发
- 使用 Spring Boot 3.2 + Java 17
- JPA 自动建表，首次启动时创建表结构
- JWT 认证，有效期 24 小时
- 文件上传到 `./uploads/` 目录

### 前端开发
- 使用 Angular 17 + TypeScript
- 组件化开发，响应式设计
- 代理配置解决跨域问题
- 支持图片灯箱、分页、搜索

## 📄 许可证

MIT License
