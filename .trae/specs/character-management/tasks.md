# Tasks

- [x] Task 1: 创建数据模型层
  - [x] Task 1.1: 在 domain 模块创建 Character 数据模型（包含 id, bookId, name, alias, gender, age, personality, appearance, background, notes, avatarPath, createdAt, updatedAt）
  - [x] Task 1.2: 在 domain 模块创建 CharacterRelation 数据模型（用于记录角色关系）
  - [x] Task 1.3: 在 domain 模块创建 RelationType 枚举（FAMILY, MASTER_DISCIPLE, FRIEND, ENEMY, LOVER, OTHER）
  - [x] Task 1.4: 在 domain 模块创建 CharacterRepository 接口

- [x] Task 2: 创建数据库层
  - [x] Task 2.1: 在 core-database 模块创建 CharacterEntity 实体类
  - [x] Task 2.2: 在 core-database 模块创建 CharacterRelationEntity 实体类
  - [x] Task 2.3: 在 core-database 模块创建 CharacterDao 接口
  - [x] Task 2.4: 在 core-database 模块创建 CharacterRelationDao 接口
  - [x] Task 2.5: 在 core-database 模块创建 Entity 与 Model 的 Mapper
  - [x] Task 2.6: 更新 AppDatabase 添加新实体

- [x] Task 3: 创建数据仓库实现
  - [x] Task 3.1: 在 core-database 模块创建 CharacterRepositoryImpl 实现类
  - [x] Task 3.2: 更新 DatabaseModule 添加依赖注入配置

- [x] Task 4: 创建用例层
  - [x] Task 4.1: 创建 GetCharactersByBookIdUseCase
  - [x] Task 4.2: 创建 GetCharacterByIdUseCase
  - [x] Task 4.3: 创建 AddCharacterUseCase
  - [x] Task 4.4: 创建 UpdateCharacterUseCase
  - [x] Task 4.5: 创建 DeleteCharacterUseCase
  - [x] Task 4.6: 创建 GetCharacterRelationsUseCase
  - [x] Task 4.7: 创建 AddCharacterRelationUseCase
  - [x] Task 4.8: 创建 DeleteCharacterRelationUseCase

- [x] Task 5: 创建 feature-character 模块
  - [x] Task 5.1: 创建 build.gradle.kts 配置文件
  - [x] Task 5.2: 创建 .gitignore, consumer-rules.pro, proguard-rules.pro
  - [x] Task 5.3: 在 settings.gradle.kts 注册模块

- [x] Task 6: 创建 UI 层
  - [x] Task 6.1: 创建 CharacterListState 和 CharacterListIntent
  - [x] Task 6.2: 创建 CharacterListViewModel
  - [x] Task 6.3: 创建 CharacterListScreen（角色列表页面）
  - [x] Task 6.4: 创建 CharacterItemCard 组件
  - [x] Task 6.5: 创建 AddCharacterDialog 组件
  - [x] Task 6.6: 创建 EditCharacterDialog 组件
  - [x] Task 6.7: 创建 CharacterDetailScreen（角色详情页面，含关系展示）
  - [x] Task 6.8: 创建 AddRelationDialog 组件

- [x] Task 7: 集成导航
  - [x] Task 7.1: 创建 CharacterRoutes.kt 和 CharacterNavigation.kt
  - [x] Task 7.2: 在 AppNavigation 添加角色页面路由
  - [x] Task 7.3: 在章节列表页添加跳转到角色管理的入口
  - [x] Task 7.4: 在 app/build.gradle.kts 添加 feature-character 依赖

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 1, Task 2]
- [Task 4] depends on [Task 1, Task 3]
- [Task 6] depends on [Task 4, Task 5]
- [Task 7] depends on [Task 6]
