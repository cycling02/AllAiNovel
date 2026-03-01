# Tasks

- [x] Task 1: 创建写作统计数据模型
  - [x] SubTask 1.1: 在core-model模块创建WritingSession数据类
  - [x] SubTask 1.2: 在core-database模块创建WritingSessionEntity实体
  - [x] SubTask 1.3: 创建WritingSessionDao数据访问对象
  - [x] SubTask 1.4: 更新AppDatabase添加新实体
  - [x] SubTask 1.5: 创建WritingSessionMapper映射器

- [x] Task 2: 创建写作统计仓库和用例
  - [x] SubTask 2.1: 在domain模块创建WritingSessionRepository接口
  - [x] SubTask 2.2: 在core-database模块实现WritingSessionRepositoryImpl
  - [x] SubTask 2.3: 创建SaveWritingSessionUseCase用例
  - [x] SubTask 2.4: 创建GetDailyStatisticsUseCase用例
  - [x] SubTask 2.5: 创建GetTotalStatisticsUseCase用例
  - [x] SubTask 2.6: 创建GetWritingSessionsByDateRangeUseCase用例

- [x] Task 3: 创建写作统计功能模块 (feature-statistics)
  - [x] SubTask 3.1: 创建feature-statistics模块目录结构
  - [x] SubTask 3.2: 创建StatisticsState、StatisticsIntent、StatisticsEffect
  - [x] SubTask 3.3: 创建StatisticsViewModel
  - [x] SubTask 3.4: 创建StatisticsScreen统计页面UI
  - [x] SubTask 3.5: 创建统计概览卡片组件
  - [x] SubTask 3.6: 创建字数趋势图表组件
  - [x] SubTask 3.7: 创建导航路由和路由定义

- [x] Task 4: 修改编辑器模块添加写作会话追踪
  - [x] SubTask 4.1: 修改ChapterEditState添加初始字数字段
  - [x] SubTask 4.2: 修改ChapterEditViewModel添加写作会话追踪逻辑
  - [x] SubTask 4.3: 在进入编辑器时记录初始字数和开始时间
  - [x] SubTask 4.4: 在离开编辑器时保存写作会话记录

- [x] Task 5: 集成写作统计模块到主应用
  - [x] SubTask 5.1: 在settings.gradle.kts添加feature-statistics模块
  - [x] SubTask 5.2: 在app模块添加feature-statistics依赖
  - [x] SubTask 5.3: 在AppNavigation添加统计页面导航
  - [x] SubTask 5.4: 在书籍列表页面添加统计入口

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 2]
- [Task 4] depends on [Task 2]
- [Task 5] depends on [Task 3, Task 4]
