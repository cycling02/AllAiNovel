# 创作工具功能 Spec

## Why
网络小说创作过程中，作者经常需要快速记录灵感片段，以及生成符合中文语境的角色姓名、地名、势力名称。这两个工具能提升创作效率，减少创作中断。

## What Changes
- 新增 `feature-tools` 模块
- 新增 `Inspiration` 数据模型（灵感收集）
- 新增灵感相关的数据库表和DAO
- 新增灵感收集页面
- 新增名字生成器页面（纯本地算法，无需AI）
- 支持灵感的增删改查和标签分类
- 支持中文姓名、地名、势力名的随机生成

## Impact
- Affected specs: 无
- Affected code: 
  - `core-database`: 新增 InspirationDao、InspirationEntity
  - `domain`: 新增 Inspiration 模型和仓库接口
  - `core-database`: 新增 InspirationRepository 实现
  - `app`: 导航路由新增工具页面

## ADDED Requirements

### 灵感收集模块

### Requirement: 灵感数据模型
系统应当提供灵感数据模型，支持快速记录灵感片段。

#### Scenario: 灵感属性
- **WHEN** 用户创建灵感时
- **THEN** 系统支持记录标题、内容、标签、创建时间等属性

### Requirement: 灵感列表展示
系统应当展示灵感列表。

#### Scenario: 展示灵感列表
- **WHEN** 用户进入灵感页面
- **THEN** 系统以卡片形式展示所有灵感，按创建时间倒序排列

#### Scenario: 空状态
- **WHEN** 没有任何灵感
- **THEN** 系统显示空状态提示，引导用户创建第一个灵感

### Requirement: 创建灵感
系统应当允许用户快速创建灵感。

#### Scenario: 快速创建
- **WHEN** 用户点击添加按钮
- **THEN** 系统显示简洁的创建表单，用户可输入标题和内容

#### Scenario: 添加标签
- **WHEN** 用户创建或编辑灵感时
- **THEN** 系统允许用户添加多个标签进行分类

### Requirement: 编辑灵感
系统应当允许用户编辑灵感信息。

#### Scenario: 编辑灵感
- **WHEN** 用户点击编辑按钮
- **THEN** 系统显示编辑表单，预填充现有数据

### Requirement: 删除灵感
系统应当允许用户删除灵感。

#### Scenario: 删除灵感
- **WHEN** 用户点击删除按钮
- **THEN** 系统显示确认对话框，确认后删除灵感

### Requirement: 标签筛选
系统应当支持按标签筛选灵感。

#### Scenario: 按标签筛选
- **WHEN** 用户点击某个标签
- **THEN** 系统只显示包含该标签的灵感

#### Scenario: 查看所有标签
- **WHEN** 用户进入标签列表
- **THEN** 系统展示所有使用过的标签及其使用次数

### Requirement: 灵感搜索
系统应当支持灵感搜索功能。

#### Scenario: 搜索灵感
- **WHEN** 用户输入搜索关键词
- **THEN** 系统按标题和内容模糊匹配过滤灵感列表

---

### 名字生成器模块

### Requirement: 中文姓名生成
系统应当提供中文姓名生成功能。

#### Scenario: 生成随机姓名
- **WHEN** 用户点击生成按钮
- **THEN** 系统随机生成一个中文姓名（姓+名）

#### Scenario: 批量生成
- **WHEN** 用户选择批量生成
- **THEN** 系统一次生成多个姓名供选择

#### Scenario: 指定性别
- **WHEN** 用户选择性别（男/女）
- **THEN** 系统生成符合性别特征的姓名

#### Scenario: 指定字数
- **WHEN** 用户选择名字字数（单字/双字）
- **THEN** 系统生成对应字数的名字

### Requirement: 地名生成
系统应当提供地名生成功能。

#### Scenario: 生成随机地名
- **WHEN** 用户点击生成地名
- **THEN** 系统随机生成一个中文地名

#### Scenario: 地名类型选择
- **WHEN** 用户选择地名类型
- **THEN** 系统生成对应类型的地名（城市、山脉、河流、森林等）

### Requirement: 势力名生成
系统应当提供势力名称生成功能。

#### Scenario: 生成随机势力名
- **WHEN** 用户点击生成势力名
- **THEN** 系统随机生成一个势力名称

#### Scenario: 势力类型选择
- **WHEN** 用户选择势力类型
- **THEN** 系统生成对应类型的势力名（宗门、家族、帝国、商会等）

### Requirement: 生成结果操作
系统应当支持对生成结果的操作。

#### Scenario: 复制结果
- **WHEN** 用户点击复制按钮
- **THEN** 系统将生成的名字复制到剪贴板

#### Scenario: 收藏结果
- **WHEN** 用户点击收藏按钮
- **THEN** 系统将生成的名字保存到收藏列表

#### Scenario: 重新生成
- **WHEN** 用户点击重新生成按钮
- **THEN** 系统生成新的名字

### Requirement: 收藏管理
系统应当支持收藏的名字管理。

#### Scenario: 查看收藏
- **WHEN** 用户进入收藏列表
- **THEN** 系统展示所有收藏的名字

#### Scenario: 删除收藏
- **WHEN** 用户删除某个收藏
- **THEN** 系统从收藏列表中移除该名字

---

### 导航集成

### Requirement: 工具入口
系统应当将工具功能集成到主导航中。

#### Scenario: 从主页面进入工具
- **WHEN** 用户在主页面点击"工具"按钮
- **THEN** 系统导航到工具页面，展示灵感收集和名字生成器入口
