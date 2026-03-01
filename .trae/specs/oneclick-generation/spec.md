# 一键生成模块 Spec

## Why
用户希望输入一段随意的自然语言描述，AI就能自动解析并生成完整的书籍结构，包括书籍信息、角色档案、世界观设定、大纲结构和章节内容。这符合"能少输入就少输入"的核心原则，让创作流程更加顺畅。

## What Changes
- 新增 `feature-oneclick` 模块
- 新增 `OneClickGenerationScreen` 主界面
- 新增 `OneClickGenerationViewModel` 处理生成逻辑
- 新增 `OneClickGenerationState/Intent/Effect` 状态管理
- 在 `AiRepository` 新增 `parseAndGenerateBook` 方法
- 新增 `ParsePromptUseCase` 解析用户描述
- 新增 `GenerateBookStructureUseCase` 批量生成书籍结构
- 在书籍列表页新增"一键生成"入口

## Impact
- Affected specs: 书籍管理模块（需要集成一键生成入口）
- Affected code:
  - `domain`: 新增 UseCase，修改 `AiRepository` 接口
  - `core-network`: 修改 `AiRepositoryImpl` 添加解析和生成实现
  - `feature-book`: 新增一键生成入口
  - `feature-oneclick`: 全新模块，包含所有一键生成相关代码

## ADDED Requirements

### Requirement: 一键生成入口
系统应当在书籍列表页面提供一键生成入口。

#### Scenario: 书籍列表显示一键生成按钮
- **GIVEN** 用户在书籍列表页面
- **WHEN** 用户查看页面
- **THEN** 系统显示"一键生成"按钮（与"新建书籍"并列或作为选项）

#### Scenario: 点击进入一键生成页面
- **WHEN** 用户点击"一键生成"按钮
- **THEN** 系统导航到一键生成页面

### Requirement: 一键生成主界面
系统应当提供简洁的一键生成界面。

#### Scenario: 显示输入区域
- **WHEN** 用户进入一键生成页面
- **THEN** 系统显示：
  - 大文本输入框（多行，支持随意格式描述）
  - 生成选项（复选框）：生成角色、生成世界观、生成大纲、生成第一章
  - 模型选择下拉框
  - "开始生成"按钮

#### Scenario: 输入示例提示
- **WHEN** 用户聚焦输入框
- **THEN** 系统显示示例提示：
  ```
  示例：
  《斗破苍穹》同人小说。
  主角：古无涯，古族大少爷，37岁九星斗圣巅峰...
  老婆洛璃，妹妹古薰儿12岁...
  第一章写他们日常...
  ```

#### Scenario: 选择生成选项
- **WHEN** 用户查看生成选项
- **THEN** 默认全部选中
- **AND** 用户可以取消不需要的选项

#### Scenario: 选择AI模型
- **WHEN** 用户点击模型选择器
- **THEN** 显示已配置的API列表
- **AND** 用户可以选择任意模型进行生成

### Requirement: 智能解析用户描述
系统应当能够解析用户随意的自然语言描述。

#### Scenario: 解析书籍信息
- **GIVEN** 用户输入包含书名或类型的描述
- **WHEN** 系统解析描述
- **THEN** 提取：
  - 书名（如《斗破苍穹》同人 → 自动生成新书名）
  - 类型（如玄幻、都市、仙侠）
  - 简介（基于描述生成）

#### Scenario: 解析角色信息
- **GIVEN** 用户输入包含角色描述
- **WHEN** 系统解析描述
- **THEN** 提取角色列表，每个角色包含：
  - 姓名（如古无涯、洛璃、古薰儿）
  - 身份（如主角、妻子、妹妹）
  - 关键属性（如年龄、修为、特征）
  - 关系（如夫妻关系、兄妹关系）

#### Scenario: 解析世界观信息
- **GIVEN** 用户输入包含世界观描述
- **WHEN** 系统解析描述
- **THEN** 提取世界观设定：
  - 势力/组织（如古族、洛神族）
  - 修炼体系（如斗气等级）
  - 特殊设定（如炼丹师等级）

#### Scenario: 解析大纲和章节
- **GIVEN** 用户输入包含剧情描述
- **WHEN** 系统解析描述
- **THEN** 提取：
  - 大纲结构（卷/章）
  - 第一章内容要求

### Requirement: 分批生成书籍结构
系统应当分批生成书籍的各个部分。

#### Scenario: 第一阶段 - 解析提取
- **WHEN** 用户点击"开始生成"
- **THEN** 系统首先调用AI解析用户描述
- **AND** 提取结构化信息（书籍、角色、世界观、大纲）

#### Scenario: 第二阶段 - 生成详细内容
- **GIVEN** 解析完成
- **WHEN** 系统根据提取的信息
- **THEN** 分批生成：
  1. 书籍信息（标题、简介、类型）
  2. 角色档案（每个角色的完整信息）
  3. 世界观设定（势力、修炼体系等）
  4. 大纲结构（多级大纲）
  5. 第一章内容（基于大纲和角色）

#### Scenario: 显示生成进度
- **WHEN** 系统正在生成
- **THEN** 显示进度指示器：
  ```
  正在生成...
  [=====>    ] 解析描述 ✓
  [========> ] 生成角色 (3个)...
  [          ] 生成世界观
  [          ] 生成大纲
  [          ] 生成第一章
  ```

#### Scenario: 取消生成
- **WHEN** 用户在生成过程中点击取消
- **THEN** 系统取消所有进行中的生成请求
- **AND** 返回输入界面

### Requirement: 生成结果预览
系统应当提供完整的生成结果预览界面。

#### Scenario: 显示书籍信息
- **WHEN** 生成完成
- **THEN** 显示：
  ```
  📚 《古族帝尊》
     类型：同人/玄幻
     简介：古族大少爷古无涯...
  ```

#### Scenario: 显示角色列表
- **WHEN** 生成完成
- **THEN** 显示角色卡片列表：
  ```
  👤 角色（3个）
     ├─ [卡片] 古无涯 - 主角，37岁...
     ├─ [卡片] 洛璃 - 妻子，洛神族...
     └─ [卡片] 古薰儿 - 妹妹，12岁...
  ```
- **AND** 点击卡片可以查看/编辑详情

#### Scenario: 显示世界观设定
- **WHEN** 生成完成
- **THEN** 显示世界观列表：
  ```
  🌍 世界观（4个）
     ├─ [势力] 古族 - 远古八族之一...
     ├─ [势力] 洛神族 - 中州超级势力...
     ├─ [修炼体系] 斗气等级 - 斗者至斗帝...
     └─ [炼丹师] 炼丹师等级 - 一品至帝品...
  ```

#### Scenario: 显示大纲结构
- **WHEN** 生成完成
- **THEN** 显示大纲树：
  ```
  📋 大纲
     └─ 第一卷：帝者之路
        └─ 第一章：古界日常
  ```

#### Scenario: 显示第一章内容
- **WHEN** 生成完成
- **THEN** 显示第一章内容预览（可滚动）
- **AND** 内容可直接编辑

### Requirement: 结果编辑和修改
系统应当允许用户直接编辑生成结果。

#### Scenario: 编辑书籍信息
- **WHEN** 用户点击书籍信息区域
- **THEN** 弹出编辑对话框
- **AND** 用户可以修改标题、类型、简介

#### Scenario: 编辑角色信息
- **WHEN** 用户点击角色卡片
- **THEN** 弹出角色编辑界面
- **AND** 用户可以修改角色属性
- **AND** 提供"重新生成此角色"按钮

#### Scenario: 编辑世界观设定
- **WHEN** 用户点击世界观项
- **THEN** 弹出编辑对话框
- **AND** 用户可以修改设定内容

#### Scenario: 编辑大纲
- **WHEN** 用户点击大纲项
- **THEN** 可以修改标题和摘要

#### Scenario: 编辑第一章内容
- **WHEN** 用户在内容预览区域
- **THEN** 可以直接编辑文本
- **AND** 提供"按指令修改"功能

### Requirement: 修改指令功能
系统应当支持输入修改指令让AI重新调整。

#### Scenario: 输入修改指令
- **GIVEN** 用户对某部分结果不满意
- **WHEN** 用户输入修改指令（如"语气更悲伤"）
- **THEN** 系统调用AI基于原内容和指令重新生成

#### Scenario: 局部重新生成
- **GIVEN** 用户选中某角色或某部分
- **WHEN** 用户点击"重新生成"
- **THEN** 仅重新生成选中的部分
- **AND** 保持其他部分不变

### Requirement: 应用生成结果
系统应当将生成结果保存到数据库。

#### Scenario: 全部应用
- **WHEN** 用户点击"全部应用"
- **THEN** 系统：
  1. 创建书籍
  2. 保存所有角色
  3. 保存所有世界观设定
  4. 保存大纲结构
  5. 创建第一章并保存内容
- **AND** 导航到章节编辑器

#### Scenario: 选择性应用
- **WHEN** 用户取消某些生成选项
- **THEN** 仅应用选中的部分
- **AND** 未选中的部分不保存

#### Scenario: 应用失败处理
- **WHEN** 保存过程中发生错误
- **THEN** 显示错误提示
- **AND** 允许用户重试

## MODIFIED Requirements

### Requirement: BookListScreen 新增入口
系统应当在书籍列表页面新增一键生成入口。

#### Scenario: 显示一键生成按钮
- **GIVEN** `BookListScreen` 需要支持一键生成
- **THEN** 在FAB或顶部菜单中添加"一键生成"选项

### Requirement: AppNavigation 新增路由
系统应当新增一键生成页面的导航路由。

#### Scenario: 新增路由
- **GIVEN** 需要导航到一键生成页面
- **THEN** 新增 `oneclick_generation` 路由
- **AND** 配置从书籍列表到一键生成页面的导航

## Data Models

### OneClickGenerationState
```kotlin
data class OneClickGenerationState(
    // 输入状态
    val userDescription: String = "",
    val generateOptions: GenerationOptions = GenerationOptions(),
    val selectedModel: ApiConfig? = null,
    
    // 生成状态
    val isGenerating: Boolean = false,
    val generationStage: GenerationStage = GenerationStage.IDLE,
    val progress: Float = 0f,
    
    // 解析结果
    val parsedData: ParsedBookData? = null,
    
    // 生成结果
    val generatedBook: Book? = null,
    val generatedCharacters: List<Character> = emptyList(),
    val generatedWorldSettings: List<WorldSetting> = emptyList(),
    val generatedOutline: List<OutlineItem> = emptyList(),
    val generatedChapterContent: String = "",
    
    // 错误状态
    val error: String? = null
)

data class GenerationOptions(
    val generateCharacters: Boolean = true,
    val generateWorldSettings: Boolean = true,
    val generateOutline: Boolean = true,
    val generateFirstChapter: Boolean = true
)

enum class GenerationStage {
    IDLE,
    PARSING,           // 解析描述
    GENERATING_BOOK,   // 生成书籍信息
    GENERATING_CHARACTERS,  // 生成角色
    GENERATING_WORLD,  // 生成世界观
    GENERATING_OUTLINE,     // 生成大纲
    GENERATING_CHAPTER,     // 生成第一章
    COMPLETED,         // 完成
    ERROR              // 错误
}
```

### ParsedBookData
```kotlin
data class ParsedBookData(
    val bookTitle: String?,
    val bookGenre: String?,
    val bookDescription: String?,
    val characters: List<ParsedCharacter>,
    val worldSettings: List<ParsedWorldSetting>,
    val outlineStructure: List<ParsedOutlineItem>,
    val firstChapterHint: String?
)

data class ParsedCharacter(
    val name: String,
    val identity: String?,
    val attributes: Map<String, String>,
    val relationships: List<Pair<String, String>>
)

data class ParsedWorldSetting(
    val category: String,
    val name: String,
    val description: String?
)

data class ParsedOutlineItem(
    val level: Int,
    val title: String,
    val summary: String?,
    val children: List<ParsedOutlineItem> = emptyList()
)
```
