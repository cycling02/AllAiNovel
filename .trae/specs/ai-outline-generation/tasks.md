# Tasks

- [x] Task 1: 扩展AiRepository接口和实现
  - [x] Task 1.1: 在 `AiRepository` 接口新增 `generateOutline` 方法
  - [x] Task 1.2: 在 `AiRepositoryImpl` 实现大纲生成逻辑，包含专业Prompt设计
  - [x] Task 1.3: 创建 `OutlineGenerationResult` 数据类用于解析AI返回的JSON

- [x] Task 2: 创建大纲生成用例
  - [x] Task 2.1: 创建 `GenerateOutlineUseCase` 用例类
  - [x] Task 2.2: 用例包含JSON解析逻辑，将AI返回转换为 `OutlineItem` 列表

- [x] Task 3: 扩展OutlineListState状态
  - [x] Task 3.1: 在 `OutlineListState` 新增AI生成相关字段（showAiGenerateDialog, isAiGenerating, aiGeneratedOutline, showAiPreviewDialog）

- [x] Task 4: 扩展OutlineListIntent意图
  - [x] Task 4.1: 在 `OutlineListIntent` 新增AI生成相关意图（ShowAiGenerateDialog, HideAiGenerateDialog, GenerateOutline, ApplyAiOutline, HideAiPreviewDialog）

- [x] Task 5: 扩展OutlineListViewModel
  - [x] Task 5.1: 注入 `GenerateOutlineUseCase` 和 `GetDefaultApiConfigUseCase`
  - [x] Task 5.2: 实现AI生成意图处理逻辑
  - [x] Task 5.3: 实现大纲预览和应用逻辑

- [x] Task 6: 创建AI生成对话框组件
  - [x] Task 6.1: 创建 `AiGenerateOutlineDialog` 组件，包含主题、简介、章节数、层级数输入

- [x] Task 7: 创建AI大纲预览对话框组件
  - [x] Task 7.1: 创建 `AiOutlinePreviewDialog` 组件，展示AI生成的大纲树形结构
  - [x] Task 7.2: 支持确认应用和取消操作

- [x] Task 8: 更新OutlineListScreen界面
  - [x] Task 8.1: 在空状态页面添加"AI生成大纲"按钮
  - [x] Task 8.2: 在TopAppBar添加AI生成入口按钮
  - [x] Task 8.3: 集成AI生成对话框和预览对话框

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 5] depends on [Task 2, Task 3, Task 4]
- [Task 8] depends on [Task 5, Task 6, Task 7]
