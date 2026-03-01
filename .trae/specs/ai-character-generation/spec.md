# AI角色生成功能 Spec

## Why
网络小说创作中，设计角色是耗时且需要创意的工作。AI角色生成功能可以根据用户提供的简单描述或要求，自动生成完整的角色档案，包括姓名、性格、外貌、背景故事等，帮助作者快速创建角色，激发创作灵感。

## What Changes
- 在 `AiRepository` 接口新增 `generateCharacter` 方法
- 在 `AiRepositoryImpl` 实现角色生成逻辑
- 新增 `GenerateCharacterUseCase` 用例
- 在 `feature-character` 模块新增AI生成角色入口和对话框
- 在 `CharacterListViewModel` 新增AI生成相关状态和意图处理

## Impact
- Affected specs: 角色管理模块（需要集成AI生成入口）
- Affected code:
  - `domain`: 新增 `GenerateCharacterUseCase`，修改 `AiRepository` 接口
  - `core-network`: 修改 `AiRepositoryImpl` 添加角色生成实现
  - `feature-character`: 新增AI生成对话框、修改 `CharacterListState/Intent/ViewModel`

## ADDED Requirements

### Requirement: AI角色生成入口
系统应当在角色管理页面提供AI生成入口。

#### Scenario: 空角色状态显示AI生成选项
- **GIVEN** 书籍没有任何角色
- **WHEN** 用户查看空状态页面
- **THEN** 系统显示"AI生成角色"按钮作为备选方案

#### Scenario: 工具栏AI生成入口
- **WHEN** 用户点击角色页面的AI生成按钮
- **THEN** 系统显示角色生成配置对话框

### Requirement: 角色生成配置对话框
系统应当提供角色生成配置界面。

#### Scenario: 输入生成参数
- **WHEN** 用户打开AI生成对话框
- **THEN** 系统显示以下输入项：
  - 角色类型（可选，如：主角、配角、反派）
  - 性别（可选，如：男、女、未知）
  - 简要描述（可选，描述角色特点或要求）
  - 生成数量（可选，默认1个，最多5个）

#### Scenario: 开始生成
- **WHEN** 用户点击"生成"按钮
- **THEN** 系统调用AI生成角色，显示加载状态

#### Scenario: 取消生成
- **WHEN** 用户在生成过程中点击取消
- **THEN** 系统取消生成请求，关闭对话框

### Requirement: AI角色生成结果
系统应当处理AI生成的角色结果。

#### Scenario: 生成成功
- **WHEN** AI成功返回角色内容
- **THEN** 系统解析角色数据，显示预览界面供用户确认

#### Scenario: 预览并应用角色
- **WHEN** 用户在预览界面确认应用
- **THEN** 系统将AI生成的角色保存到数据库

#### Scenario: 生成失败
- **WHEN** AI生成失败（网络错误、API错误等）
- **THEN** 系统显示错误提示，允许用户重试

#### Scenario: 未配置API
- **WHEN** 用户未配置默认AI API
- **THEN** 系统提示用户先配置API

### Requirement: 角色生成Prompt设计
系统应当使用专业的Prompt生成结构化角色数据。

#### Scenario: Prompt结构
- **GIVEN** 系统构建AI请求
- **WHEN** 发送生成请求
- **THEN** Prompt包含：
  - 角色设定（专业网文角色设计师）
  - 输出格式要求（JSON格式，包含name、alias、gender、age、personality、appearance、background字段）
  - 网文角色创作指导（性格鲜明、背景合理、外貌描写等）
  - 用户提供的角色类型和描述

### Requirement: 角色结果解析
系统应当正确解析AI返回的角色数据。

#### Scenario: JSON解析
- **WHEN** AI返回角色内容
- **THEN** 系统解析JSON格式的角色数据，转换为 `Character` 对象

#### Scenario: 解析失败处理
- **WHEN** AI返回的内容无法解析为有效角色
- **THEN** 系统显示原始内容供用户参考，并提示生成格式异常

## MODIFIED Requirements

### Requirement: CharacterListState 状态扩展
系统应当扩展角色列表状态以支持AI生成。

#### Scenario: 新增状态字段
- **GIVEN** `CharacterListState` 需要支持AI生成
- **THEN** 新增以下字段：
  - `showAiGenerateDialog: Boolean` - 是否显示AI生成对话框
  - `isAiGenerating: Boolean` - 是否正在AI生成
  - `aiGeneratedCharacters: List<Character>?` - AI生成的角色预览
  - `showAiPreviewDialog: Boolean` - 是否显示AI预览对话框

### Requirement: CharacterListIntent 意图扩展
系统应当扩展角色列表意图以支持AI生成。

#### Scenario: 新增意图类型
- **GIVEN** 需要处理AI生成相关操作
- **THEN** 新增以下意图：
  - `ShowAiGenerateDialog` - 显示AI生成对话框
  - `HideAiGenerateDialog` - 隐藏AI生成对话框
  - `GenerateCharacter(type, gender, description, count)` - 执行AI生成
  - `ApplyAiCharacters` - 应用AI生成的角色
  - `HideAiPreviewDialog` - 隐藏预览对话框
