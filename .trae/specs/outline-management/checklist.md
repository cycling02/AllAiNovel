# Checklist

## 数据模型层
- [x] OutlineItem 数据模型定义正确，包含所有必要字段
- [x] OutlineStatus 枚举包含 PENDING, WRITING, COMPLETED, ABANDONED 四种状态
- [x] OutlineRepository 接口定义了所有必要的 CRUD 方法

## 数据库层
- [x] OutlineItemEntity 正确映射到数据库表
- [x] OutlineDao 包含按 bookId 查询、增删改等方法
- [x] AppDatabase 版本正确更新
- [x] Entity 与 Model 的 Mapper 转换正确

## 数据仓库层
- [x] OutlineRepositoryImpl 正确实现 OutlineRepository 接口
- [x] 数据库操作在 IO 线程执行

## 用例层
- [x] GetOutlineByBookIdUseCase 返回树形结构数据
- [x] AddOutlineItemUseCase 正确处理层级限制
- [x] DeleteOutlineItemUseCase 正确处理级联删除
- [x] ReorderOutlineItemsUseCase 正确更新排序

## UI 层
- [x] OutlineListScreen 以树形结构展示大纲
- [x] 大纲项支持展开/折叠
- [x] 添加对话框正确验证输入
- [x] 编辑对话框正确显示现有数据
- [x] 删除确认对话框在有子项时提示用户
- [x] 空状态正确显示引导信息

## 导航集成
- [x] 从章节列表页可跳转到大纲页面
- [x] 大纲页面正确接收 bookId 参数
- [x] 返回导航正常工作

## 依赖注入
- [x] OutlineRepository 正确注入到 ViewModel
- [x] Hilt 模块配置正确
