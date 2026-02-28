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

## 功能模块规划
1. **项目管理**: 书籍管理、章节管理、数据统计
2. **AI写作**: 续写、改写、扩写、润色、内容生成
3. **设定管理**: 角色、世界观、力量体系、物品、地点
4. **AI配置**: 多模型支持、参数配置、提示词管理
5. **编辑器**: 富文本编辑、自动保存、历史版本
6. **素材库**: 灵感收集、名字生成器、词汇库、模板库
7. **大纲**: 大纲管理、AI大纲辅助
8. **导出**: TXT/EPUB/PDF/Word导出
9. **数据管理**: 本地存储、备份恢复
10. **设置**: 主题、外观、隐私安全

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
