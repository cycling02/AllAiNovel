# 写作统计功能 Spec

## Why
用户需要了解自己的写作进度和习惯，包括每日字数、总字数和写作时长统计，以便更好地规划写作计划和追踪创作进度。

## What Changes
- 新增写作统计模块 (feature-statistics)
- 新增写作记录数据库实体 (WritingSessionEntity)
- 新增统计页面展示每日/每周/每月字数统计
- 新增写作时长追踪功能
- 编辑器模块增加写作会话记录功能

## Impact
- Affected specs: 编辑器模块
- Affected code: 
  - feature-editor (需添加写作会话追踪)
  - core-database (新增WritingSessionEntity)
  - app (新增导航路由)

## ADDED Requirements

### Requirement: 写作会话记录
系统 SHALL 记录每次写作会话的详细信息。

#### Scenario: 开始写作
- **WHEN** 用户进入章节编辑页面
- **THEN** 系统开始记录写作会话，记录开始时间

#### Scenario: 结束写作
- **WHEN** 用户离开章节编辑页面或应用进入后台
- **THEN** 系统计算本次写作时长和新增字数，保存写作记录

### Requirement: 每日字数统计
系统 SHALL 提供每日字数统计功能。

#### Scenario: 查看今日统计
- **WHEN** 用户查看统计页面
- **THEN** 系统显示今日写作字数、今日写作时长

#### Scenario: 查看历史统计
- **WHEN** 用户选择查看历史统计
- **THEN** 系统显示按日期分组的字数统计图表

### Requirement: 总字数统计
系统 SHALL 提供总字数统计功能。

#### Scenario: 查看总字数
- **WHEN** 用户查看统计页面
- **THEN** 系统显示所有书籍的总字数、总章节数、总写作时长

#### Scenario: 按书籍查看统计
- **WHEN** 用户选择特定书籍
- **THEN** 系统显示该书籍的字数统计和章节数

### Requirement: 写作时长统计
系统 SHALL 提供写作时长统计功能。

#### Scenario: 查看今日写作时长
- **WHEN** 用户查看统计页面
- **THEN** 系统显示今日累计写作时长

#### Scenario: 查看写作时长趋势
- **WHEN** 用户查看统计图表
- **THEN** 系统显示近7天/30天的写作时长趋势图

### Requirement: 统计数据展示
系统 SHALL 提供直观的统计数据展示界面。

#### Scenario: 统计概览
- **WHEN** 用户进入统计页面
- **THEN** 系统显示统计概览卡片（今日字数、今日时长、总字数、总时长）

#### Scenario: 字数趋势图表
- **WHEN** 用户查看字数趋势
- **THEN** 系统显示近7天/30天的字数变化折线图

#### Scenario: 写作日历
- **WHEN** 用户查看写作日历
- **THEN** 系统显示日历视图，标记有写作记录的日期

## MODIFIED Requirements

### Requirement: 编辑器状态追踪
编辑器 SHALL 追踪用户的写作状态。

#### Scenario: 记录初始字数
- **WHEN** 用户进入章节编辑页面
- **THEN** 系统记录当前章节的初始字数

#### Scenario: 计算新增字数
- **WHEN** 用户离开章节编辑页面
- **THEN** 系统计算本次写作新增字数（当前字数 - 初始字数）

## REMOVED Requirements
无
