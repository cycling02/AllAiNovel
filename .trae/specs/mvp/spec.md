# AllAiNovel MVP Spec

## Why
创建一个最小可行产品(MVP)，实现AI辅助网络小说写作的核心功能，让用户能够创建书籍、编写章节、使用AI进行续写。

## What Changes
- 搭建多模块Clean Architecture + MVI项目架构
- 实现书籍管理功能（创建、列表、编辑、删除）
- 实现章节管理功能（创建、编辑、删除、排序）
- 实现AI配置功能（API Key配置、模型选择）
- 实现AI续写功能
- 实现基础文本编辑器
- 实现数据本地存储

## Impact
- 新建项目架构和基础依赖配置
- 新建数据层（Room数据库、实体、DAO）
- 新建业务层（用例、仓库接口）
- 新建展示层（ViewModel、UI页面）

## ADDED Requirements

### Requirement: 项目架构
系统应提供多模块Clean Architecture架构，包含app、core、data、domain、presentation五个模块。

#### Scenario: 模块依赖正确
- **WHEN** 项目构建
- **THEN** app依赖presentation，presentation依赖domain，domain无依赖，data依赖domain，core被所有模块依赖

### Requirement: 书籍管理
系统应提供书籍管理功能，支持创建、查看、编辑、删除书籍。

#### Scenario: 创建书籍成功
- **WHEN** 用户点击创建书籍并输入书名
- **THEN** 系统创建新书籍并显示在列表中

#### Scenario: 删除书籍成功
- **WHEN** 用户删除一本书籍
- **THEN** 书籍及其所有章节被删除

### Requirement: 章节管理
系统应提供章节管理功能，支持创建、编辑、删除章节。

#### Scenario: 创建章节成功
- **WHEN** 用户在书籍中创建新章节
- **THEN** 章节被创建并显示在章节列表中

#### Scenario: 编辑章节内容
- **WHEN** 用户点击章节进入编辑
- **THEN** 显示文本编辑器，可编辑章节内容

### Requirement: AI配置
系统应提供AI API配置功能，支持配置API Key和选择模型。

#### Scenario: 配置DeepSeek API
- **WHEN** 用户输入DeepSeek API Key并保存
- **THEN** API Key被加密存储，可用于AI请求

#### Scenario: 切换AI模型
- **WHEN** 用户选择不同的AI模型
- **THEN** 后续AI请求使用选定的模型

### Requirement: AI续写
系统应提供AI续写功能，根据当前章节内容生成续写内容。

#### Scenario: AI续写成功
- **WHEN** 用户点击续写按钮且已配置API
- **THEN** 系统调用AI API生成续写内容并显示

#### Scenario: AI续写失败
- **WHEN** API调用失败
- **THEN** 显示错误提示信息

### Requirement: 本地存储
系统应使用Room数据库进行本地数据持久化存储。

#### Scenario: 数据持久化
- **WHEN** 用户创建或修改数据
- **THEN** 数据被保存到本地数据库

### Requirement: 基础UI
系统应提供基础的用户界面，包括主题支持和Material 3设计。

#### Scenario: 深色/浅色主题
- **WHEN** 系统主题切换
- **THEN** 应用界面跟随系统主题变化
