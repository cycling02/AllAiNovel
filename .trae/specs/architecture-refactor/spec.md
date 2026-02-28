# 架构重构 Spec

## Why
当前项目架构较为简单，需要重构为更细粒度的模块化架构，以支持更复杂的功能扩展、提高代码复用性、降低模块间耦合度，并符合 Google 官方推荐的现代 Android 架构最佳实践。

## What Changes
- **BREAKING** 重构模块结构，从 5 模块扩展为 15 模块
- 新增 core-common、core-ui、core-model、core-database、core-network、core-datastore 核心模块
- 新增 feature-editor、feature-ai、feature-settings、feature-book、feature-chapter 功能模块
- 重构 data 模块为依赖 core-database、core-network、core-datastore
- 重构 presentation 模块为依赖各 feature 模块
- 引入 MVI 架构模式替代 MVVM
- 更新所有模块的依赖关系

## Impact
- Affected specs: mvp
- Affected code: 所有模块的 build.gradle.kts、包结构、依赖注入配置

## ADDED Requirements

### Requirement: 核心模块拆分
系统应将 core 模块拆分为 core-common、core-ui、core-model、core-database、core-network、core-datastore 六个独立模块。

#### Scenario: core-common 模块独立
- **WHEN** 项目构建
- **THEN** core-common 模块无外部依赖，提供通用工具类和扩展函数

#### Scenario: core-ui 模块独立
- **WHEN** 项目构建
- **THEN** core-ui 模块依赖 core-common，提供通用 UI 组件和设计系统

#### Scenario: core-model 模块独立
- **WHEN** 项目构建
- **THEN** core-model 模块无外部依赖，定义核心数据模型

#### Scenario: core-database 模块独立
- **WHEN** 项目构建
- **THEN** core-database 模块依赖 core-model 和 core-common，提供 Room 数据库实现

#### Scenario: core-network 模块独立
- **WHEN** 项目构建
- **THEN** core-network 模块依赖 core-model 和 core-common，提供网络请求实现

#### Scenario: core-datastore 模块独立
- **WHEN** 项目构建
- **THEN** core-datastore 模块依赖 core-model 和 core-common，提供偏好设置实现

### Requirement: 功能模块拆分
系统应将 presentation 模块拆分为 feature-editor、feature-ai、feature-settings、feature-book、feature-chapter 五个独立功能模块。

#### Scenario: feature-editor 模块独立
- **WHEN** 项目构建
- **THEN** feature-editor 模块依赖 domain、core-ui、core-common，提供编辑器功能

#### Scenario: feature-ai 模块独立
- **WHEN** 项目构建
- **THEN** feature-ai 模块依赖 domain、core-ui、core-common，提供 AI 写作功能

#### Scenario: feature-settings 模块独立
- **WHEN** 项目构建
- **THEN** feature-settings 模块依赖 domain、core-ui、core-common，提供设置功能

#### Scenario: feature-book 模块独立
- **WHEN** 项目构建
- **THEN** feature-book 模块依赖 domain、core-ui、core-common，提供书籍管理功能

#### Scenario: feature-chapter 模块独立
- **WHEN** 项目构建
- **THEN** feature-chapter 模块依赖 domain、core-ui、core-common，提供章节管理功能

### Requirement: MVI 架构模式
系统应采用 MVI (Model-View-Intent) 架构模式，实现单向数据流。

#### Scenario: 状态管理
- **WHEN** ViewModel 处理用户意图
- **THEN** 通过 StateFlow 发射新状态，UI 订阅状态更新

#### Scenario: 副本处理
- **WHEN** 需要执行副作用（如导航、显示 Toast）
- **THEN** 通过 Channel 或 SharedFlow 发送副作用事件

### Requirement: 模块依赖关系
系统应建立正确的模块依赖关系，确保依赖方向正确。

#### Scenario: 依赖方向正确
- **WHEN** 项目构建
- **THEN** app → presentation → feature-* → domain，data → core-*，无循环依赖

## MODIFIED Requirements

### Requirement: data 模块重构
data 模块应重构为依赖 core-database、core-network、core-datastore，不再直接包含数据库和网络实现。

#### Scenario: data 模块依赖正确
- **WHEN** 项目构建
- **THEN** data 模块依赖 domain、core-database、core-network、core-datastore、core-common

### Requirement: presentation 模块重构
presentation 模块应重构为聚合各 feature 模块，提供统一导航入口。

#### Scenario: presentation 模块依赖正确
- **WHEN** 项目构建
- **THEN** presentation 模块依赖 domain、core-ui、core-common 及各 feature 模块

## REMOVED Requirements

### Requirement: 原 core 模块
**Reason**: 拆分为更细粒度的核心模块
**Migration**: 将 core 模块内容迁移至 core-common 和 core-ui

### Requirement: 原 presentation 模块中的功能代码
**Reason**: 迁移至各 feature 模块
**Migration**: 将书籍、章节、设置、编辑器、AI 功能迁移至对应 feature 模块
