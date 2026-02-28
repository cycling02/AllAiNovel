# Tasks

## 1. 核心模块创建
- [ ] Task 1.1: 创建 core-common 模块
  - [ ] 创建 core-common 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 创建通用工具类和扩展函数

- [ ] Task 1.2: 创建 core-model 模块
  - [ ] 创建 core-model 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移核心数据模型（Book、Chapter、ApiConfig）

- [ ] Task 1.3: 创建 core-ui 模块
  - [ ] 创建 core-ui 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移主题和通用 UI 组件

- [ ] Task 1.4: 创建 core-database 模块
  - [ ] 创建 core-database 目录结构
  - [ ] 配置 build.gradle.kts（添加 Room 依赖）
  - [ ] 迁移 Entity、DAO、Database、Mapper

- [ ] Task 1.5: 创建 core-network 模块
  - [ ] 创建 core-network 目录结构
  - [ ] 配置 build.gradle.kts（添加 Retrofit、OkHttp 依赖）
  - [ ] 迁移 API 接口、请求/响应模型

- [ ] Task 1.6: 创建 core-datastore 模块
  - [ ] 创建 core-datastore 目录结构
  - [ ] 配置 build.gradle.kts（添加 DataStore 依赖）
  - [ ] 创建偏好设置实现

## 2. 功能模块创建
- [ ] Task 2.1: 创建 feature-book 模块
  - [ ] 创建 feature-book 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移 BookListViewModel 和 BookListScreen
  - [ ] 实现 MVI 模式（State、Intent、Effect）

- [ ] Task 2.2: 创建 feature-chapter 模块
  - [ ] 创建 feature-chapter 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移 ChapterListViewModel 和 ChapterListScreen
  - [ ] 实现 MVI 模式（State、Intent、Effect）

- [ ] Task 2.3: 创建 feature-editor 模块
  - [ ] 创建 feature-editor 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移 ChapterEditViewModel 和 ChapterEditScreen
  - [ ] 实现 MVI 模式（State、Intent、Effect）

- [ ] Task 2.4: 创建 feature-ai 模块
  - [ ] 创建 feature-ai 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 创建 AI 续写 UI 组件
  - [ ] 实现 MVI 模式（State、Intent、Effect）

- [ ] Task 2.5: 创建 feature-settings 模块
  - [ ] 创建 feature-settings 目录结构
  - [ ] 配置 build.gradle.kts
  - [ ] 迁移 SettingsViewModel 和 SettingsScreen
  - [ ] 实现 MVI 模式（State、Intent、Effect）

## 3. 模块重构
- [ ] Task 3.1: 重构 domain 模块
  - [ ] 更新 build.gradle.kts 依赖 core-model
  - [ ] 移除已迁移至 core-model 的模型
  - [ ] 更新仓库接口

- [ ] Task 3.2: 重构 data 模块
  - [ ] 更新 build.gradle.kts 依赖 core-database、core-network、core-datastore
  - [ ] 移除已迁移至核心模块的代码
  - [ ] 更新仓库实现

- [ ] Task 3.3: 重构 presentation 模块
  - [ ] 更新 build.gradle.kts 依赖各 feature 模块
  - [ ] 移除已迁移至功能模块的代码
  - [ ] 保留导航配置

- [ ] Task 3.4: 重构 app 模块
  - [ ] 更新 build.gradle.kts 依赖 presentation、core-ui、core-common
  - [ ] 更新 Application 类

## 4. 依赖注入重构
- [ ] Task 4.1: 配置 Hilt 模块
  - [ ] 创建 core-database 的 DI 模块
  - [ ] 创建 core-network 的 DI 模块
  - [ ] 创建 core-datastore 的 DI 模块
  - [ ] 更新 data 模块的 DI 模块

- [ ] Task 4.2: 配置功能模块 DI
  - [ ] 为每个 feature 模块创建 DI 模块
  - [ ] 配置 ViewModel 注入

## 5. 导航重构
- [ ] Task 5.1: 重构导航系统
  - [ ] 更新 navigation 模块依赖
  - [ ] 创建统一的导航图
  - [ ] 配置各 feature 模块的导航目的地

## 6. 项目配置更新
- [ ] Task 6.1: 更新 settings.gradle.kts
  - [ ] 添加所有新模块

- [ ] Task 6.2: 更新 libs.versions.toml
  - [ ] 添加新依赖版本

- [ ] Task 6.3: 更新根 build.gradle.kts
  - [ ] 添加新插件声明

## 7. 清理旧代码
- [ ] Task 7.1: 删除原 core 模块
  - [ ] 确认代码已迁移
  - [ ] 删除 core 目录

# Task Dependencies
- Task 1.x 可并行执行
- Task 2.x 依赖 Task 1.2, Task 1.3 完成
- Task 3.1 依赖 Task 1.2 完成
- Task 3.2 依赖 Task 1.4, Task 1.5, Task 1.6 完成
- Task 3.3 依赖 Task 2.x 完成
- Task 4.x 依赖 Task 3.x 完成
- Task 5.1 依赖 Task 2.x 完成
- Task 6.x 可并行执行
- Task 7.1 依赖所有迁移任务完成
