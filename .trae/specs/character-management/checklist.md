# Checklist

## 数据模型层
- [x] Character 数据模型定义正确，包含所有必要字段
- [x] CharacterRelation 数据模型定义正确
- [x] RelationType 枚举包含常用的关系类型
- [x] CharacterRepository 接口定义了所有必要的 CRUD 方法

## 数据库层
- [x] CharacterEntity 正确映射到数据库表
- [x] CharacterRelationEntity 正确映射到数据库表
- [x] CharacterDao 包含按 bookId 查询、增删改等方法
- [x] CharacterRelationDao 包含关系管理方法
- [x] AppDatabase 版本正确更新
- [x] Entity 与 Model 的 Mapper 转换正确

## 数据仓库层
- [x] CharacterRepositoryImpl 正确实现 CharacterRepository 接口
- [x] 数据库操作在 IO 线程执行

## 用例层
- [x] GetCharactersByBookIdUseCase 返回角色列表
- [x] AddCharacterUseCase 正确验证必填字段
- [x] DeleteCharacterUseCase 正确处理级联删除关系
- [x] GetCharacterRelationsUseCase 返回角色关系列表

## UI 层
- [x] CharacterListScreen 以列表形式展示角色
- [x] 角色卡片显示姓名、别名、性别等基本信息
- [x] 添加对话框正确验证输入
- [x] 编辑对话框正确显示现有数据
- [x] 删除确认对话框正常工作
- [x] 空状态正确显示引导信息

## 导航集成
- [x] 从章节列表页可跳转到角色管理页面
- [x] 角色页面正确接收 bookId 参数
- [x] 返回导航正常工作

## 依赖注入
- [x] CharacterRepository 正确注入到 ViewModel
- [x] Hilt 模块配置正确
