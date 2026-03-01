# Tasks

- [x] Task 1: 创建数据模型层
  - [x] Task 1.1: 在 domain 模块创建 OutlineItem 数据模型（包含 id, bookId, parentId, title, summary, level, sortOrder, status, createdAt, updatedAt）
  - [x] Task 1.2: 在 domain 模块创建 OutlineStatus 枚举（PENDING, WRITING, COMPLETED, ABANDONED）
  - [x] Task 1.3: 在 domain 模块创建 OutlineRepository 接口

- [x] Task 2: 创建数据库层
  - [x] Task 2.1: 在 core-database 模块创建 OutlineItemEntity 实体类
  - [x] Task 2.2: 在 core-database 模块创建 OutlineDao 接口
  - [x] Task 2.3: 在 core-database 模块创建 Entity 与 Model 的 Mapper
  - [x] Task 2.4: 更新 AppDatabase 添加 OutlineItemEntity

- [x] Task 3: 创建数据仓库实现
  - [x] Task 3.1: 在 core-database 模块创建 OutlineRepositoryImpl 实现类

- [x] Task 4: 创建用例层
  - [x] Task 4.1: 创建 GetOutlineByBookIdUseCase
  - [x] Task 4.2: 创建 AddOutlineItemUseCase
  - [x] Task 4.3: 创建 UpdateOutlineItemUseCase
  - [x] Task 4.4: 创建 DeleteOutlineItemUseCase
  - [x] Task 4.5: 创建 ReorderOutlineItemsUseCase

- [x] Task 5: 创建 feature-outline 模块
  - [x] Task 5.1: 创建 build.gradle.kts 配置文件
  - [x] Task 5.2: 在 settings.gradle.kts 注册模块

- [x] Task 6: 创建 UI 层
  - [x] Task 6.1: 创建 OutlineListState 和 OutlineListIntent
  - [x] Task 6.2: 创建 OutlineListViewModel
  - [x] Task 6.3: 创建 OutlineItemUiModel（用于树形展示）
  - [x] Task 6.4: 创建 OutlineListScreen（树形列表页面）
  - [x] Task 6.5: 创建 OutlineItemCard 组件
  - [x] Task 6.6: 创建 AddOutlineItemDialog 组件
  - [x] Task 6.7: 创建 EditOutlineItemDialog 组件
  - [x] Task 6.8: 创建 DeleteConfirmDialog 组件

- [x] Task 7: 集成导航
  - [x] Task 7.1: 在 AppNavigation 添加大纲页面路由
  - [x] Task 7.2: 在章节列表页添加跳转到大纲的入口
  - [x] Task 7.3: 更新 NavGraph 添加 outline 目标页

- [x] Task 8: 依赖注入配置
  - [x] Task 8.1: 在 DatabaseModule 绑定 OutlineRepository
  - [x] Task 8.2: 在 app 模块添加 feature-outline 依赖

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 1, Task 2]
- [Task 4] depends on [Task 1, Task 3]
- [Task 6] depends on [Task 4, Task 5]
- [Task 7] depends on [Task 6]
- [Task 8] depends on [Task 3]
