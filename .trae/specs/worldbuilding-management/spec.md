# 世界观设定功能 Spec

## Why
网络小说（尤其是玄幻、仙侠、奇幻类）通常需要构建完整的世界观，包括地点、势力、力量体系和物品等设定。世界观管理帮助作者系统化地管理这些设定，保持世界观的一致性，避免设定冲突，提升作品质量。

## What Changes
- 新增 `feature-worldbuilding` 模块
- 新增 `WorldSetting` 数据模型（支持地点、势力、力量体系、物品四种类型）
- 新增世界观相关的数据库表和DAO
- 新增世界观列表页面和编辑页面
- 支持世界观的增删改查
- 支持按类型筛选
- 支持设定之间的关联

## Impact
- Affected specs: 书籍管理模块（需要从书籍详情页跳转到世界观管理页面）
- Affected code:
  - `core-database`: 新增 WorldSettingDao、WorldSettingEntity
  - `domain`: 新增 WorldSetting 模型和仓库接口
  - `data`: 新增 WorldSettingRepository 实现
  - `app`: 导航路由新增世界观页面

## ADDED Requirements

### Requirement: 世界观数据模型
系统应当提供世界观数据模型，支持多种设定类型。

#### Scenario: 设定类型
- **WHEN** 用户创建世界观设定时
- **THEN** 系统支持四种类型：地点(LOCATION)、势力(FACTION)、力量体系(POWER_SYSTEM)、物品(ITEM)

#### Scenario: 设定属性
- **WHEN** 用户创建设定时
- **THEN** 系统支持记录名称、描述、详细说明、自定义属性等

### Requirement: 世界观列表展示
系统应当展示书籍的世界观设定列表。

#### Scenario: 展示设定列表
- **WHEN** 用户进入某本书的世界观页面
- **THEN** 系统以列表形式展示该书的所有设定

#### Scenario: 按类型筛选
- **WHEN** 用户选择特定类型标签
- **THEN** 系统只显示该类型的设定

#### Scenario: 空状态
- **WHEN** 书籍没有任何世界观设定
- **THEN** 系统显示空状态提示，引导用户创建第一个设定

### Requirement: 创建设定
系统应当允许用户创建新的世界观设定。

#### Scenario: 创建设定
- **WHEN** 用户点击添加按钮
- **THEN** 系统显示创建表单，用户可选择类型并输入设定信息

#### Scenario: 必填字段
- **GIVEN** 设定名称为必填字段
- **WHEN** 用户提交时名称为空
- **THEN** 系统提示请输入设定名称

### Requirement: 编辑设定
系统应当允许用户编辑设定信息。

#### Scenario: 编辑设定
- **WHEN** 用户点击编辑按钮
- **THEN** 系统显示编辑表单，预填充现有数据

### Requirement: 删除设定
系统应当允许用户删除设定。

#### Scenario: 删除设定
- **WHEN** 用户点击删除按钮
- **THEN** 系统显示确认对话框，确认后删除设定

### Requirement: 设定搜索
系统应当支持设定搜索功能。

#### Scenario: 搜索设定
- **WHEN** 用户输入搜索关键词
- **THEN** 系统按名称和描述模糊匹配过滤设定列表

### Requirement: 设定详情
系统应当支持查看设定详情。

#### Scenario: 查看详情
- **WHEN** 用户点击某个设定
- **THEN** 系统显示设定详情页面，展示完整信息

### Requirement: 自定义属性
系统应当支持为设定添加自定义属性。

#### Scenario: 添加自定义属性
- **WHEN** 用户在编辑设定时
- **THEN** 系统允许添加键值对形式的自定义属性

#### Scenario: 属性展示
- **WHEN** 用户查看设定详情
- **THEN** 系统展示所有自定义属性

### Requirement: 导航集成
系统应当将世界观功能集成到导航中。

#### Scenario: 从书籍进入世界观管理
- **WHEN** 用户在书籍详情页点击"世界观"按钮
- **THEN** 系统导航到该书的世界观管理页面

## Type-Specific Attributes

### 地点 (LOCATION)
推荐自定义属性：
- 地理位置
- 气候环境
- 主要居民
- 历史背景
- 特色建筑

### 势力 (FACTION)
推荐自定义属性：
- 势力类型（宗门、家族、帝国等）
- 领导者
- 核心成员
- 势力范围
- 主要资源

### 力量体系 (POWER_SYSTEM)
推荐自定义属性：
- 修炼等级
- 突破条件
- 功法类型
- 资源需求
- 特殊能力

### 物品 (ITEM)
推荐自定义属性：
- 物品类型（法宝、丹药、材料等）
- 品阶等级
- 获取方式
- 特殊效果
- 使用限制
