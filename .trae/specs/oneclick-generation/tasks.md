# Tasks

- [x] Task 1: 创建 feature-oneclick 模块
  - [x] 1.1 在根目录 settings.gradle.kts 中添加新模块
  - [x] 1.2 创建 feature-oneclick/build.gradle.kts 文件
  - [x] 1.3 配置模块依赖（core-ui、domain、data、feature-book、feature-character、feature-worldbuilding、feature-outline、feature-chapter）

- [x] Task 2: 实现 Domain 层 UseCase
  - [x] 2.1 创建 ParsePromptUseCase - 解析用户自然语言描述
  - [x] 2.2 创建 GenerateBookStructureUseCase - 批量生成书籍结构
  - [x] 2.3 修改 AiRepository 接口，添加 parseAndGenerateBook 方法
  - [x] 2.4 在 core-network 中实现 AiRepositoryImpl 的解析和生成逻辑

- [x] Task 3: 实现 OneClickGeneration 状态管理
  - [x] 3.1 创建 OneClickGenerationState 数据类
  - [x] 3.2 创建 OneClickGenerationIntent 密封类
  - [x] 3.3 创建 OneClickGenerationEffect 密封类
  - [x] 3.4 创建 OneClickGenerationViewModel

- [x] Task 4: 实现 OneClickGenerationScreen UI
  - [x] 4.1 创建输入界面（大文本框、生成选项、模型选择器）
  - [x] 4.2 创建生成进度显示组件
  - [x] 4.3 创建结果预览界面（书籍、角色、世界观、大纲、章节）
  - [x] 4.4 实现结果编辑功能（点击编辑、修改指令）

- [x] Task 5: 实现解析和生成逻辑
  - [x] 5.1 设计解析Prompt，从用户描述提取结构化数据
  - [x] 5.2 实现书籍信息生成逻辑
  - [x] 5.3 实现角色批量生成逻辑
  - [x] 5.4 实现世界观批量生成逻辑
  - [x] 5.5 实现大纲结构生成逻辑
  - [x] 5.6 实现第一章内容生成逻辑

- [x] Task 6: 集成到书籍列表
  - [x] 6.1 在 BookListScreen 添加"一键生成"入口按钮
  - [x] 6.2 在 AppNavigation 添加 oneclick_generation 路由
  - [x] 6.3 配置从书籍列表到一键生成页面的导航

- [x] Task 7: 实现结果应用功能
  - [x] 7.1 实现保存书籍到数据库
  - [x] 7.2 实现批量保存角色
  - [x] 7.3 实现批量保存世界观设定
  - [x] 7.4 实现保存大纲结构
  - [x] 7.5 实现创建第一章并保存内容
  - [x] 7.6 实现导航到编辑器

# Task Dependencies

- Task 2 depends on Task 1
- Task 3 depends on Task 2
- Task 4 depends on Task 3
- Task 5 depends on Task 2
- Task 6 depends on Task 4
- Task 7 depends on Task 4 and Task 5
