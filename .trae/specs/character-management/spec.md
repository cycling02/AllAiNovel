# 角色管理功能 Spec

## Why
网络小说通常有众多角色，角色管理帮助作者创建和维护角色档案，保持角色设定的一致性，避免在长篇创作中出现设定冲突。

## What Changes
- 新增 `feature-character` 模块
- 新增 `Character` 数据模型
- 新增角色相关的数据库表和DAO
- 新增角色列表页面和编辑页面
- 支持角色的增删改查
- 支持角色关系管理
- 支持角色头像设置

## Impact
- Affected specs: 书籍管理模块（需要从书籍详情页跳转到角色管理页面）
- Affected code: 
  - `core-database`: 新增 CharacterDao、CharacterEntity
  - `domain`: 新增 Character 模型和仓库接口
  - `data`: 新增 CharacterRepository 实现
  - `app`: 导航路由新增角色页面

## ADDED Requirements

### Requirement: 角色数据模型
系统应当提供角色数据模型，包含完整的角色信息。

#### Scenario: 角色属性
- **WHEN** 用户创建角色时
- **THEN** 系统支持记录姓名、别名、性别、年龄、性格、外貌描述、背景故事、备注等属性

### Requirement: 角色列表展示
系统应当展示书籍的角色列表。

#### Scenario: 展示角色列表
- **WHEN** 用户进入某本书的角色页面
- **THEN** 系统以列表形式展示该书的所有角色

#### Scenario: 空状态
- **WHEN** 书籍没有任何角色
- **THEN** 系统显示空状态提示，引导用户创建第一个角色

### Requirement: 创建角色
系统应当允许用户创建新角色。

#### Scenario: 创建角色
- **WHEN** 用户点击添加按钮
- **THEN** 系统显示创建表单，用户可输入角色信息

#### Scenario: 必填字段
- **GIVEN** 角色姓名为必填字段
- **WHEN** 用户提交时姓名为空
- **THEN** 系统提示请输入角色姓名

### Requirement: 编辑角色
系统应当允许用户编辑角色信息。

#### Scenario: 编辑角色
- **WHEN** 用户点击编辑按钮
- **THEN** 系统显示编辑表单，预填充现有数据

### Requirement: 删除角色
系统应当允许用户删除角色。

#### Scenario: 删除角色
- **WHEN** 用户点击删除按钮
- **THEN** 系统显示确认对话框，确认后删除角色

### Requirement: 角色搜索
系统应当支持角色搜索功能。

#### Scenario: 搜索角色
- **WHEN** 用户输入搜索关键词
- **THEN** 系统按姓名模糊匹配过滤角色列表

### Requirement: 角色关系
系统应当支持记录角色之间的关系。

#### Scenario: 添加关系
- **WHEN** 用户为角色添加关系
- **THEN** 系统记录关系类型（如：父子、师徒、朋友、敌人等）和相关角色

#### Scenario: 关系展示
- **WHEN** 用户查看角色详情
- **THEN** 系统展示该角色与其他角色的关系

### Requirement: 导航集成
系统应当将角色功能集成到导航中。

#### Scenario: 从书籍进入角色管理
- **WHEN** 用户在书籍详情页点击"角色"按钮
- **THEN** 系统导航到该书的角色管理页面
