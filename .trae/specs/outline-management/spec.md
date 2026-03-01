# 大纲管理功能 Spec

## Why
网络小说创作需要规划故事结构，大纲管理帮助作者组织故事脉络、章节安排和情节发展，是创作流程中的核心需求。

## What Changes
- 新增 `feature-outline` 模块
- 新增 `Outline` 和 `OutlineItem` 数据模型
- 新增大纲相关的数据库表和DAO
- 新增大纲列表页面和编辑页面
- 支持多级大纲结构（最多3级）
- 支持大纲项的增删改查
- 支持大纲项拖拽排序
- 支持大纲项展开/折叠

## Impact
- Affected specs: 书籍管理模块（需要从书籍详情页跳转到大纲页面）
- Affected code: 
  - `core-database`: 新增 OutlineDao、OutlineEntity
  - `domain`: 新增 Outline、OutlineItem 模型和仓库接口
  - `data`: 新增 OutlineRepository 实现
  - `app`: 导航路由新增大纲页面

## ADDED Requirements

### Requirement: 大纲数据模型
系统应当提供大纲数据模型，支持多级结构。

#### Scenario: 大纲结构
- **WHEN** 用户创建大纲时
- **THEN** 系统支持最多3级的大纲结构（卷 > 章 > 节）

### Requirement: 大纲列表展示
系统应当展示书籍的大纲列表。

#### Scenario: 展示大纲列表
- **WHEN** 用户进入某本书的大纲页面
- **THEN** 系统以树形结构展示该书的所有大纲项

#### Scenario: 空状态
- **WHEN** 书籍没有任何大纲项
- **THEN** 系统显示空状态提示，引导用户创建第一个大纲项

### Requirement: 创建大纲项
系统应当允许用户创建新的大纲项。

#### Scenario: 创建顶级大纲
- **WHEN** 用户点击添加按钮
- **THEN** 系统显示创建对话框，用户可输入标题和简介

#### Scenario: 创建子大纲
- **WHEN** 用户在某大纲项上点击添加子项
- **THEN** 系统在该项下创建子级大纲项

#### Scenario: 层级限制
- **WHEN** 用户尝试在第3级大纲下添加子项
- **THEN** 系统提示已达到最大层级限制

### Requirement: 编辑大纲项
系统应当允许用户编辑大纲项。

#### Scenario: 编辑大纲内容
- **WHEN** 用户点击编辑按钮
- **THEN** 系统显示编辑对话框，可修改标题、简介、状态

### Requirement: 删除大纲项
系统应当允许用户删除大纲项。

#### Scenario: 删除叶子节点
- **WHEN** 用户删除没有子项的大纲项
- **THEN** 系统直接删除该项

#### Scenario: 删除有子项的节点
- **WHEN** 用户删除有子项的大纲项
- **THEN** 系统提示是否同时删除所有子项

### Requirement: 大纲排序
系统应当支持大纲项的排序。

#### Scenario: 拖拽排序
- **WHEN** 用户长按并拖拽大纲项
- **THEN** 系统允许在同级别内重新排序

### Requirement: 大纲展开折叠
系统应当支持大纲项的展开和折叠。

#### Scenario: 折叠大纲
- **WHEN** 用户点击有子项的大纲项
- **THEN** 系统折叠/展开该大纲项的子项

### Requirement: 大纲状态管理
系统应当支持大纲项的状态管理。

#### Scenario: 状态类型
- **GIVEN** 大纲项状态包括：待写作、写作中、已完成、已放弃
- **WHEN** 用户修改大纲状态
- **THEN** 系统保存状态变更

### Requirement: 导航集成
系统应当将大纲功能集成到导航中。

#### Scenario: 从书籍进入大纲
- **WHEN** 用户在书籍详情页点击"大纲"按钮
- **THEN** 系统导航到该书的大纲页面
