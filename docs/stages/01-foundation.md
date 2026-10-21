# 第一阶段：项目基础

## 目标

建立前后端分离工程、统一 API 响应、数据库迁移、JWT 登录与角色边界、公共布局和导航路由。

## 本阶段交付

- Vue 3/TypeScript/Vite 前端入口、路由守卫、认证状态和统一 Axios 客户端。
- Spring Boot 3/Java 21 工程、统一响应/错误处理、Spring Security JWT 登录注册与角色注解。
- Flyway 初始表：用户、角色、用户角色和审计事件；账号密码使用 BCrypt。
- 环境变量示例、部署说明与基础学术风格导航壳。

## 接口与权限

- `POST /api/auth/register`：注册为学生。
- `POST /api/auth/login`：验证凭据并签发 JWT。
- `GET /api/auth/me`：返回当前账户与角色。
- `GET /api/health`：服务状态。
- 管理接口用 `@PreAuthorize` 限制角色；默认拒绝未认证请求，登录/注册与公开页面除外。

## 数据与限制

数据库使用 MySQL/Flyway；JWT 密钥由环境变量提供。第一阶段未提供 OAuth、找回密码、验证码与线上对象存储。

## 验收记录

已完成源码结构和配置；按仓库约定未执行编译、程序启动或数据库迁移。需在本地环境完成首次启动验证。

