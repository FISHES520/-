# 《现代历史学》智慧教学平台

面向历史学本科课程的课程门户与智慧教学平台。项目按 `m.txt` 的六阶段计划交付，每个阶段的实现范围、接口、数据和后续工作记录在 `docs/stages/`。

## 技术栈

- 前端：Vue 3、TypeScript、Vite、Vue Router、Pinia、Element Plus、Axios、ECharts
- 后端：Java 21、Spring Boot 3、Spring Security、JWT、Spring Data JPA、Flyway
- 数据：MySQL 8；开发环境文件存储；AI 由服务端通过环境变量配置

## 本地开发

先复制 `.env.example` 为 `.env` 并填写数据库及 JWT 配置。后端使用 `backend/` 的 Maven 工程，前端使用 `frontend/` 的 Vite 工程。数据库结构由 Flyway 初始化。详细部署和验收步骤见 `docs/DEPLOYMENT.md`。

示例课程、教师、用户与问答内容均标记为演示数据；平台不会把演示数据表述为真实学术成果。未配置 AI 服务时，接口返回明确的未配置状态，不生成伪造回答。

