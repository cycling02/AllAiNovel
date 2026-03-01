# 提示词(Prompt)管理 Spec

## Why
当前AI写作功能的提示词是硬编码在代码中的，用户无法自定义或管理提示词模板。提供提示词管理功能可以让用户创建个性化的提示词库，提升AI写作的灵活性和效率。

## What Changes
- 新增提示词数据模型和数据库表
- 新增提示词管理模块(feature-tools/prompt)
- 支持系统预设提示词和用户自定义提示词
- 支持提示词的增删改查、收藏、分类管理
- AI写作界面集成提示词选择功能

## Impact
- Affected specs: AI写作功能
- Affected code: 
  - 新增 feature-tools 模块下的 prompt 相关代码
  - 修改 feature-ai 模块的 AiWritingScreen 和 ViewModel
  - 修改 core-database 模块添加 PromptEntity
  - 修改 domain 模块添加 Prompt 模型和仓库接口

## ADDED Requirements

### Requirement: 提示词数据模型
系统 SHALL 提供提示词数据模型，包含以下字段：
- id: 唯一标识
- name: 提示词名称
- content: 提示词内容（支持变量占位符如 {{context}}）
- category: 分类（续写、改写、扩写、润色、自定义等）
- isSystem: 是否为系统预设
- isFavorite: 是否收藏
- sortOrder: 排序顺序
- createdAt: 创建时间
- updatedAt: 更新时间

#### Scenario: 创建提示词数据模型
- **WHEN** 系统初始化时
- **THEN** 提示词模型应包含所有必要字段并支持Room持久化

### Requirement: 系统预设提示词
系统 SHALL 提供系统级预设提示词，用户可查看但不可编辑删除。

#### Scenario: 初始化系统预设
- **WHEN** 用户首次使用应用时
- **THEN** 系统自动初始化内置的预设提示词（续写、改写、扩写、润色等）

#### Scenario: 保护系统预设
- **WHEN** 用户尝试编辑或删除系统预设提示词
- **THEN** 系统阻止操作并提示"系统预设不可修改"

### Requirement: 用户自定义提示词
系统 SHALL 允许用户创建、编辑、删除自己的提示词。

#### Scenario: 创建自定义提示词
- **WHEN** 用户创建新提示词并填写名称、内容、分类
- **THEN** 提示词保存到数据库并显示在列表中

#### Scenario: 编辑自定义提示词
- **WHEN** 用户编辑自己的提示词
- **THEN** 更新后的内容保存到数据库

#### Scenario: 删除自定义提示词
- **WHEN** 用户删除自己的提示词
- **THEN** 提示词从数据库中移除

### Requirement: 提示词收藏功能
系统 SHALL 允许用户收藏常用提示词，方便快速访问。

#### Scenario: 收藏提示词
- **WHEN** 用户点击收藏按钮
- **THEN** 提示词标记为收藏状态，在收藏列表中显示

#### Scenario: 取消收藏
- **WHEN** 用户再次点击收藏按钮
- **THEN** 提示词取消收藏状态

### Requirement: 提示词分类管理
系统 SHALL 支持按分类筛选提示词。

#### Scenario: 按分类筛选
- **WHEN** 用户选择某个分类
- **THEN** 列表仅显示该分类下的提示词

### Requirement: 提示词变量支持
系统 SHALL 支持提示词中的变量占位符，在调用时动态替换。

#### Scenario: 变量替换
- **WHEN** 用户使用包含 {{context}} 变量的提示词
- **THEN** 系统将当前上下文内容替换到变量位置

### Requirement: AI写作集成
系统 SHALL 在AI写作界面提供提示词选择功能。

#### Scenario: 选择提示词
- **WHEN** 用户在AI写作界面选择提示词
- **THEN** 提示词内容自动填充到输入框或作为生成参数

#### Scenario: 快速切换提示词
- **WHEN** 用户切换不同提示词
- **THEN** AI生成使用新的提示词模板
