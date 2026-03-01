# Tasks

- [x] Task 1: 扩展AiRepository接口和实现
  - [x] Task 1.1: 在 `AiRepository` 接口新增 `generateCharacter` 方法
  - [x] Task 1.2: 在 `AiRepositoryImpl` 实现角色生成逻辑，包含专业Prompt设计
  - [x] Task 1.3: 创建 `CharacterGenerationResult` 数据类用于解析AI返回的JSON

- [x] Task 2: 创建角色生成用例
  - [x] Task 2.1: 创建 `GenerateCharacterUseCase` 用例类
  - [x] Task 2.2: 用例包含JSON解析逻辑，将AI返回转换为 `Character` 列表

- [x] Task 3: 扩展CharacterListState状态
  - [x] Task 3.1: 在 `CharacterListState` 新增AI生成相关字段（showAiGenerateDialog, isAiGenerating, aiGeneratedCharacters, showAiPreviewDialog）

- [x] Task 4: 扩展CharacterListIntent意图
  - [x] Task 4.1: 在 `CharacterListIntent` 新增AI生成相关意图（ShowAiGenerateDialog, HideAiGenerateDialog, GenerateCharacter, ApplyAiCharacters, HideAiPreviewDialog）

- [x] Task 5: 扩展CharacterListViewModel
  - [x] Task 5.1: 注入 `GenerateCharacterUseCase` 和 `GetDefaultApiConfigUseCase`
  - [x] Task 5.2: 实现AI生成意图处理逻辑
  - [x] Task 5.3: 实现角色预览和应用逻辑

- [x] Task 6: 创建AI生成对话框组件
  - [x] Task 6.1: 创建 `AiGenerateCharacterDialog` 组件，包含角色类型、性别、描述、数量输入

- [x] Task 7: 创建AI角色预览对话框组件
  - [x] Task 7.1: 创建 `AiCharacterPreviewDialog` 组件，展示AI生成的角色列表
  - [x] Task 7.2: 支持确认应用和取消操作

- [x] Task 8: 更新CharacterListScreen界面
  - [x] Task 8.1: 在空状态页面添加"AI生成角色"按钮
  - [x] Task 8.2: 在TopAppBar添加AI生成入口按钮
  - [x] Task 8.3: 集成AI生成对话框和预览对话框

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 5] depends on [Task 2, Task 3, Task 4]
- [Task 8] depends on [Task 5, Task 6, Task 7]
