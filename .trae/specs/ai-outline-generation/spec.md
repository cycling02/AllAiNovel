# AI大纲生成功能 Spec

## Why
网络小说创作中，从零开始构建故事大纲是耗时且需要创意的工作。AI大纲生成功能可以根据用户提供的主题、简介等基础信息，自动生成结构化的故事大纲，帮助作者快速搭建故事框架，提高创作效率。

## What Changes
- 在 `AiRepository` 接口新增 `generateOutline` 方法
- 在 `AiRepositoryImpl` 实现大纲生成逻辑
- 新增 `GenerateOutlineUseCase` 用例
- 在 `feature-outline` 模块新增AI生成大纲入口和对话框
- 在 `OutlineListViewModel` 新增AI生成相关状态和意图处理

## Impact
- Affected specs: 大纲管理模块（需要集成AI生成入口）
- Affected code:
  - `domain`: 新增 `GenerateOutlineUseCase`，修改 `AiRepository` 接口
  - `core-network`: 修改 `AiRepositoryImpl` 添加大纲生成实现
  - `feature-outline`: 新增AI生成对话框、修改 `OutlineListState/Intent/ViewModel`

## ADDED Requirements

### Requirement: AI大纲生成入口
系统应当在大纲管理页面提供AI生成入口。

#### Scenario: 空大纲状态显示AI生成选项
- **GIVEN** 书籍没有任何大纲项
- **WHEN** 用户查看空状态页面
- **THEN** 系统显示"AI生成大纲"按钮作为备选方案

#### Scenario: 工具栏AI生成入口
- **WHEN** 用户点击大纲页面的AI生成按钮
- **THEN** 系统显示大纲生成配置对话框

### Requirement: 大纲生成配置对话框
系统应当提供大纲生成配置界面。

#### Scenario: 输入生成参数
- **WHEN** 用户打开AI生成对话框
- **THEN** 系统显示以下输入项：
  - 主题/题材（必填，如：玄幻、都市、仙侠）
  - 故事简介（必填，描述核心故事线）
  - 目标章节数（可选，默认20章）
  - 大纲层级（可选，默认2级：卷-章）

#### Scenario: 开始生成
- **WHEN** 用户填写必要信息并点击"生成"按钮
- **THEN** 系统调用AI生成大纲，显示加载状态

#### Scenario: 取消生成
- **WHEN** 用户在生成过程中点击取消
- **THEN** 系统取消生成请求，关闭对话框

### Requirement: AI大纲生成结果
系统应当处理AI生成的大纲结果。

#### Scenario: 生成成功
- **WHEN** AI成功返回大纲内容
- **THEN** 系统解析大纲结构，显示预览界面供用户确认

#### Scenario: 预览并应用大纲
- **WHEN** 用户在预览界面确认应用
- **THEN** 系统将AI生成的大纲项保存到数据库

#### Scenario: 生成失败
- **WHEN** AI生成失败（网络错误、API错误等）
- **THEN** 系统显示错误提示，允许用户重试

#### Scenario: 未配置API
- **WHEN** 用户未配置默认AI API
- **THEN** 系统提示用户先配置API，并提供跳转到设置页面的选项

### Requirement: 大纲生成Prompt设计
系统应当使用专业的Prompt生成结构化大纲。

#### Scenario: Prompt结构
- **GIVEN** 系统构建AI请求
- **WHEN** 发送生成请求
- **THEN** Prompt包含：
  - 角色设定（专业网文大纲策划师）
  - 输出格式要求（JSON格式，包含title、summary、level、children字段）
  - 故事结构指导（起承转合、冲突设置等）
  - 用户提供的主题和简介

### Requirement: 大纲结果解析
系统应当正确解析AI返回的大纲结构。

#### Scenario: JSON解析
- **WHEN** AI返回大纲内容
- **THEN** 系统解析JSON格式的大纲数据，转换为 `OutlineItem` 列表

#### Scenario: 解析失败处理
- **WHEN** AI返回的内容无法解析为有效大纲
- **THEN** 系统显示原始内容供用户参考，并提示生成格式异常

## MODIFIED Requirements

### Requirement: OutlineListState 状态扩展
系统应当扩展大纲列表状态以支持AI生成。

#### Scenario: 新增状态字段
- **GIVEN** `OutlineListState` 需要支持AI生成
- **THEN** 新增以下字段：
  - `showAiGenerateDialog: Boolean` - 是否显示AI生成对话框
  - `isAiGenerating: Boolean` - 是否正在AI生成
  - `aiGeneratedOutline: List<OutlineItem>?` - AI生成的大纲预览
  - `showAiPreviewDialog: Boolean` - 是否显示AI预览对话框

### Requirement: OutlineListIntent 意图扩展
系统应当扩展大纲列表意图以支持AI生成。

#### Scenario: 新增意图类型
- **GIVEN** 需要处理AI生成相关操作
- **THEN** 新增以下意图：
  - `ShowAiGenerateDialog` - 显示AI生成对话框
  - `HideAiGenerateDialog` - 隐藏AI生成对话框
  - `GenerateOutline(topic, summary, chapterCount, levelCount)` - 执行AI生成
  - `ApplyAiOutline` - 应用AI生成的大纲
  - `HideAiPreviewDialog` - 隐藏预览对话框
