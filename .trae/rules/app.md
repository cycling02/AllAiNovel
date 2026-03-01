# AllAiNovel 项目规则

## 项目概述
AllAiNovel 是一款AI辅助网络小说写作的Android应用，无后端，开源，供个人使用。
支持接入 DeepSeek API、通义千问(Qwen) 等大模型API。

## 技术栈
- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: 多模块 Clean Architecture
- **最低SDK**: Android 30 (Android 11)
- **目标SDK**: Android 36

## 模块结构
```
AllAiNovel/
├── app/              # 主应用模块，入口点
├── core-common/      # 通用工具类和扩展函数
├── core-database/    # 数据库相关代码
├── core-datastore/   # DataStore相关代码
├── core-model/       # 核心数据模型
├── core-network/     # 网络请求相关代码
├── core-ui/          # 通用UI组件
├── data/             # 数据层：数据库、网络请求、仓库实现
├── domain/           # 业务逻辑层：用例、模型、仓库接口
├── feature-ai/       # AI写作功能模块
├── feature-book/     # 书籍管理功能模块
├── feature-chapter/  # 章节管理功能模块
├── feature-editor/   # 编辑器功能模块
├── feature-settings/ # 设置功能模块
└── presentation/     # 展示层：ViewModel、UI组件、页面
```

## 依赖版本 (libs.versions.toml)
- Android Gradle Plugin: 9.0.1
- Kotlin: 2.0.21
- Compose BOM: 2024.09.00
- Lifecycle Runtime KTX: 2.10.0
- Activity Compose: 1.12.4

## 代码规范
1. 使用Kotlin编写所有代码
2. UI使用Jetpack Compose
3. 遵循Clean Architecture原则
4. 使用MVVM架构模式
5. 数据库使用Room
6. 网络请求使用Retrofit + OkHttp
7. 依赖注入使用Hilt

## 命名规范
- 包名: com.cycling.allainovel
- 类名: 大驼峰命名法
- 函数名: 小驼峰命名法
- 变量名: 小驼峰命名法
- 常量: 全大写下划线分隔

## 构建命令
```powershell
# 清理项目
.\gradlew clean

# 编译Debug版本
.\gradlew assembleDebug

# 编译Release版本
.\gradlew assembleRelease

# 运行单元测试
.\gradlew test

# 运行Lint检查
.\gradlew lint
```

## 功能实现状态

### 已完成功能

| 模块 | 功能 | 状态 |
|------|------|------|
| 书籍管理 | 书籍列表、创建、删除、搜索、状态管理 | ✅ 已完成 |
| 章节管理 | 章节列表、创建、删除、自动编号、状态管理 | ✅ 已完成 |
| 编辑器 | 内容编辑、自动保存(2秒防抖)、字数统计 | ✅ 已完成 |
| AI写作 | 续写、改写、扩写、润色、结果预览与应用 | ✅ 已完成 |
| API配置 | 6种AI提供商支持、API Key管理、默认配置 | ✅ 已完成 |

### 待开发功能

| 优先级 | 功能 | 说明 |
|--------|------|------|
| P0 | 大纲管理 | 创建/编辑书籍大纲，支持多级结构 |
| P0 | 角色管理 | 创建角色档案（姓名、性格、外貌、关系） |
| P0 | 世界观设定 | 地点、势力、力量体系、物品设定 |
| P1 | AI大纲生成 | 根据主题/简介生成故事大纲 |
| P1 | AI角色生成 | 生成角色姓名、性格、背景故事 |
| P1 | AI上下文感知 | AI写作时自动带入角色/世界观设定 |
| P2 | 灵感收集 | 快速记录灵感，支持标签分类 |
| P2 | 名字生成器 | 中文姓名、地名、势力名生成 |
| P2 | 导出功能 | TXT/EPUB导出 |
| P3 | 写作统计 | 每日字数、总字数、写作时长统计 |
| P3 | 历史版本 | 章节修改历史，支持回滚 |
| P3 | 备份恢复 | 本地备份、导出/导入数据 |

## 开发规划

### 第一阶段：核心创作增强
1. 大纲管理模块 (feature-outline)
2. 角色管理模块 (feature-character)
3. 世界观设定模块 (feature-worldbuilding)

### 第二阶段：AI辅助增强
1. AI大纲生成
2. AI角色生成
3. AI上下文感知（设定注入）

### 第三阶段：实用工具
1. 灵感收集
2. 名字生成器
3. 导出功能

### 第四阶段：数据管理
1. 写作统计
2. 历史版本
3. 备份恢复

## API配置
支持的AI模型API:
- DeepSeek (deepseek-chat, deepseek-coder)
- 通义千问 (Qwen)
- 智谱AI
- 百度文心一言
- 月之暗面
- 自定义OpenAI兼容API

## 注意事项
1. API Key需要加密存储
2. 所有网络请求需要处理异常
3. 大文本操作需要在后台线程执行
4. 自动保存功能需要防抖处理
5. 数据库操作使用协程
