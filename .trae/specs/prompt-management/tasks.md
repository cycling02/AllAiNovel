# Tasks

- [x] Task 1: 创建提示词数据模型和数据库层
  - [x] SubTask 1.1: 在 domain 模块创建 Prompt 数据模型
  - [x] SubTask 1.2: 在 domain 模块创建 PromptRepository 接口
  - [x] SubTask 1.3: 在 core-database 模块创建 PromptEntity 实体
  - [x] SubTask 1.4: 在 core-database 模块创建 PromptDao 数据访问对象
  - [x] SubTask 1.5: 在 core-database 模块创建 PromptMapper 映射器
  - [x] SubTask 1.6: 在 core-database 模块创建 PromptRepositoryImpl 实现
  - [x] SubTask 1.7: 更新 AppDatabase 添加 PromptEntity

- [x] Task 2: 创建提示词管理业务逻辑层
  - [x] SubTask 2.1: 创建 GetPromptsUseCase 获取提示词列表
  - [x] SubTask 2.2: 创建 GetPromptByIdUseCase 获取单个提示词
  - [x] SubTask 2.3: 创建 GetPromptsByCategoryUseCase 按分类获取
  - [x] SubTask 2.4: CreatePromptUseCase 创建提示词
  - [x] SubTask 2.5: UpdatePromptUseCase 更新提示词
  - [x] SubTask 2.6: DeletePromptUseCase 删除提示词
  - [x] SubTask 2.7: ToggleFavoriteUseCase 切换收藏状态
  - [x] SubTask 2.8: InitializeSystemPromptsUseCase 初始化系统预设

- [x] Task 3: 创建提示词管理UI模块
  - [x] SubTask 3.1: 创建 PromptListScreen 提示词列表页面
  - [x] SubTask 3.2: 创建 PromptDetailScreen 提示词详情/编辑页面
  - [x] SubTask 3.3: 创建 PromptViewModel 和相关 State/Intent/Effect
  - [x] SubTask 3.4: 创建 PromptCard 列表项组件
  - [x] SubTask 3.5: 创建 CategoryFilter 分类筛选组件
  - [x] SubTask 3.6: 创建 PromptEditor 提示词编辑器组件

- [x] Task 4: 集成系统预设提示词
  - [x] SubTask 4.1: 定义系统预设提示词数据（续写、改写、扩写、润色）
  - [x] SubTask 4.2: 实现首次启动时初始化系统预设

- [x] Task 5: AI写作模块集成提示词选择
  - [x] SubTask 5.1: 修改 AiWritingState 添加选中提示词字段
  - [x] SubTask 5.2: 修改 AiWritingScreen 添加提示词选择器
  - [x] SubTask 5.3: 修改 AiWritingViewModel 支持提示词变量替换
  - [x] SubTask 5.4: 创建 PromptSelector 提示词选择组件

- [x] Task 6: 添加导航和依赖注入
  - [x] SubTask 6.1: 创建 PromptRoutes 导航路由
  - [x] SubTask 6.2: 创建 PromptNavigation 导航图
  - [x] SubTask 6.3: 创建 PromptModule Hilt模块
  - [x] SubTask 6.4: 在主导航中集成提示词管理入口

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 2]
- [Task 4] depends on [Task 1]
- [Task 5] depends on [Task 2]
- [Task 6] depends on [Task 3]
