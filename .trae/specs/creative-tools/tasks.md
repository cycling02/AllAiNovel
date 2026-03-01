# Tasks

## 灵感收集模块

- [x] Task 1: 创建灵感数据模型和数据库层
  - [x] SubTask 1.1: 在 domain 模块创建 Inspiration 数据模型
  - [x] SubTask 1.2: 在 core-database 创建 InspirationEntity
  - [x] SubTask 1.3: 在 core-database 创建 InspirationDao
  - [x] SubTask 1.4: 在 core-database 创建 InspirationMapper
  - [x] SubTask 1.5: 更新 AppDatabase 添加 InspirationEntity
  - [x] SubTask 1.6: 在 domain 创建 InspirationRepository 接口
  - [x] SubTask 1.7: 在 core-database 创建 InspirationRepositoryImpl

- [x] Task 2: 创建灵感相关用例
  - [x] SubTask 2.1: 创建 GetInspirationsUseCase
  - [x] SubTask 2.2: 创建 GetInspirationsByTagUseCase
  - [x] SubTask 2.3: 创建 AddInspirationUseCase
  - [x] SubTask 2.4: 创建 UpdateInspirationUseCase
  - [x] SubTask 2.5: 创建 DeleteInspirationUseCase
  - [x] SubTask 2.6: 创建 SearchInspirationsUseCase
  - [x] SubTask 2.7: 创建 GetAllTagsUseCase

- [x] Task 3: 创建 feature-tools 模块
  - [x] SubTask 3.1: 创建模块 build.gradle.kts
  - [x] SubTask 3.2: 创建模块目录结构

- [ ] Task 4: 实现灵感收集UI
  - [ ] SubTask 4.1: 创建 InspirationListState
  - [ ] SubTask 4.2: 创建 InspirationListIntent
  - [ ] SubTask 4.3: 创建 InspirationListEffect
  - [ ] SubTask 4.4: 创建 InspirationListViewModel
  - [ ] SubTask 4.5: 创建 InspirationListScreen
  - [ ] SubTask 4.6: 创建 AddInspirationDialog
  - [ ] SubTask 4.7: 创建 EditInspirationDialog
  - [ ] SubTask 4.8: 创建 DeleteInspirationDialog
  - [ ] SubTask 4.9: 创建 InspirationItemCard
  - [ ] SubTask 4.10: 创建 TagChip组件

- [ ] Task 5: 实现灵感导航
  - [ ] SubTask 5.1: 创建 InspirationRoutes
  - [ ] SubTask 5.2: 创建 InspirationNavigation

## 名字生成器模块

- [x] Task 6: 创建名字生成器数据
  - [x] SubTask 6.1: 创建姓氏数据集（常见中文姓氏）
  - [x] SubTask 6.2: 创建男性名字用字数据集
  - [x] SubTask 6.3: 创建女性名字用字数据集
  - [x] SubTask 6.4: 创建地名前缀数据集
  - [x] SubTask 6.5: 创建地名后缀数据集
  - [x] SubTask 6.6: 创建势力名前缀数据集
  - [x] SubTask 6.7: 创建势力名后缀数据集

- [x] Task 7: 创建名字生成器用例
  - [x] SubTask 7.1: 创建 GenerateChineseNameUseCase
  - [x] SubTask 7.2: 创建 GeneratePlaceNameUseCase
  - [x] SubTask 7.3: 创建 GenerateFactionNameUseCase

- [x] Task 8: 创建收藏数据模型和数据库层
  - [x] SubTask 8.1: 在 domain 创建 NameFavorite 数据模型
  - [x] SubTask 8.2: 在 core-database 创建 NameFavoriteEntity
  - [x] SubTask 8.3: 在 core-database 创建 NameFavoriteDao
  - [x] SubTask 8.4: 更新 AppDatabase 添加 NameFavoriteEntity
  - [x] SubTask 8.5: 创建 NameFavoriteRepository 接口和实现

- [x] Task 9: 实现名字生成器UI
  - [x] SubTask 9.1: 创建 NameGeneratorState
  - [x] SubTask 9.2: 创建 NameGeneratorIntent
  - [x] SubTask 9.3: 创建 NameGeneratorEffect
  - [x] SubTask 9.4: 创建 NameGeneratorViewModel
  - [x] SubTask 9.5: 创建 NameGeneratorScreen
  - [x] SubTask 9.6: 创建 NameTypeSelector 组件
  - [x] SubTask 9.7: 创建 GeneratedNameCard 组件
  - [x] SubTask 9.8: 创建 FavoriteNameList 组件

- [x] Task 10: 实现名字生成器导航
  - [x] SubTask 10.1: 创建 NameGeneratorRoutes
  - [x] SubTask 10.2: 创建 NameGeneratorNavigation

## 集成和入口

- [ ] Task 11: 创建工具入口页面
  - [ ] SubTask 11.1: 创建 ToolsScreen（展示灵感收集和名字生成器入口）
  - [ ] SubTask 11.2: 创建 ToolsRoutes
  - [ ] SubTask 11.3: 创建 ToolsNavigation

- [x] Task 12: 集成到主导航
  - [x] SubTask 12.1: 在 app 模块添加 feature-tools 依赖
  - [x] SubTask 12.2: 在主导航添加工具入口
  - [x] SubTask 12.3: 在 DatabaseModule 添加相关DAO依赖

# Task Dependencies
- Task 2 依赖 Task 1
- Task 4 依赖 Task 2 和 Task 3
- Task 5 依赖 Task 4
- Task 7 依赖 Task 6
- Task 8 可以与 Task 6 并行
- Task 9 依赖 Task 7 和 Task 8
- Task 10 依赖 Task 9
- Task 11 依赖 Task 5 和 Task 10
- Task 12 依赖 Task 11
