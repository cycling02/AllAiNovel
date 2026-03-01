# Tasks

- [x] Task 1: 创建数据模型层
  - [x] Task 1.1: 在 domain 模块创建 `SettingType` 枚举（LOCATION, FACTION, POWER_SYSTEM, ITEM）
  - [x] Task 1.2: 在 domain 模块创建 `WorldSetting` 数据模型（包含 id, bookId, type, name, description, details, customAttributes, createdAt, updatedAt）
  - [x] Task 1.3: 在 domain 模块创建 `WorldSettingRepository` 接口

- [x] Task 2: 创建数据库层
  - [x] Task 2.1: 在 core-database 模块创建 `WorldSettingEntity` 实体类
  - [x] Task 2.2: 在 core-database 模块创建 `WorldSettingDao` 接口
  - [x] Task 2.3: 在 core-database 模块创建 Entity 与 Model 的 Mapper
  - [x] Task 2.4: 更新 AppDatabase 添加 WorldSettingEntity

- [x] Task 3: 创建数据仓库实现
  - [x] Task 3.1: 在 core-database 模块创建 `WorldSettingRepositoryImpl` 实现类

- [x] Task 4: 创建用例层
  - [x] Task 4.1: 创建 `GetWorldSettingsByBookIdUseCase`
  - [x] Task 4.2: 创建 `AddWorldSettingUseCase`
  - [x] Task 4.3: 创建 `UpdateWorldSettingUseCase`
  - [x] Task 4.4: 创建 `DeleteWorldSettingUseCase`
  - [x] Task 4.5: 创建 `SearchWorldSettingsUseCase`

- [x] Task 5: 创建 feature-worldbuilding 模块
  - [x] Task 5.1: 创建 build.gradle.kts 配置文件
  - [x] Task 5.2: 在 settings.gradle.kts 注册模块

- [x] Task 6: 创建 UI 层
  - [x] Task 6.1: 创建 `WorldSettingListState` 和 `WorldSettingListIntent`
  - [x] Task 6.2: 创建 `WorldSettingListViewModel`
  - [x] Task 6.3: 创建 `WorldSettingListScreen`（列表页面，支持类型筛选）
  - [x] Task 6.4: 创建 `WorldSettingItemCard` 组件
  - [x] Task 6.5: 创建 `AddWorldSettingDialog` 组件
  - [x] Task 6.6: 创建 `EditWorldSettingDialog` 组件
  - [x] Task 6.7: 创建 `DeleteConfirmDialog` 组件

- [x] Task 7: 集成导航
  - [x] Task 7.1: 在 AppNavigation 添加世界观页面路由
  - [x] Task 7.2: 在章节列表页添加跳转到世界观的入口
  - [x] Task 7.3: 更新 NavGraph 添加 worldbuilding 目标页

- [x] Task 8: 依赖注入配置
  - [x] Task 8.1: 在 DatabaseModule 绑定 WorldSettingRepository
  - [x] Task 8.2: 在 app 模块添加 feature-worldbuilding 依赖

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 1, Task 2]
- [Task 4] depends on [Task 1, Task 3]
- [Task 6] depends on [Task 4, Task 5]
- [Task 7] depends on [Task 6]
- [Task 8] depends on [Task 3]
