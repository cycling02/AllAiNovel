# 创作工具功能 Checklist

## 灵感收集模块

### 数据层
- [x] Inspiration 数据模型定义完整，包含 id、title、content、tags、createdAt、updatedAt 字段
- [x] InspirationEntity 正确映射到数据库表
- [x] InspirationDao 包含增删改查和按标签查询的方法
- [x] InspirationRepository 正确实现所有接口方法

### 用例层
- [x] GetInspirationsUseCase 正确返回所有灵感
- [x] GetInspirationsByTagUseCase 正确按标签筛选
- [x] AddInspirationUseCase 正确创建灵感
- [x] UpdateInspirationUseCase 正确更新灵感
- [x] DeleteInspirationUseCase 正确删除灵感
- [x] SearchInspirationsUseCase 正确搜索灵感
- [x] GetAllTagsUseCase 正确返回所有标签

### UI层
- [x] 灵感列表页面正确展示所有灵感
- [x] 空状态正确显示引导提示
- [x] 创建灵感对话框正确工作
- [x] 编辑灵感对话框正确预填充数据
- [x] 删除确认对话框正确显示
- [x] 标签筛选功能正确工作
- [x] 搜索功能正确过滤灵感

---

## 名字生成器模块

### 数据层
- [x] 姓氏数据集包含常见中文姓氏
- [x] 名字用字数据集区分男性和女性
- [x] 地名前后缀数据集覆盖常见类型
- [x] 势力名前后缀数据集覆盖常见类型
- [x] NameFavorite 数据模型定义完整
- [x] NameFavoriteDao 包含增删查方法

### 用例层
- [x] GenerateChineseNameUseCase 正确生成中文姓名
- [x] 生成姓名支持性别选择
- [x] 生成姓名支持字数选择
- [x] GeneratePlaceNameUseCase 正确生成地名
- [x] GenerateFactionNameUseCase 正确生成势力名

### UI层
- [x] 名字生成器页面正确展示
- [x] 姓名生成选项（性别、字数）正确工作
- [x] 地名类型选择正确工作
- [x] 势力类型选择正确工作
- [x] 复制功能正确复制到剪贴板
- [x] 收藏功能正确保存名字
- [x] 收藏列表正确展示
- [x] 删除收藏正确工作

---

## 导航集成

- [x] 工具入口页面正确展示两个功能入口
- [x] 从工具入口可以进入灵感收集页面
- [x] 从工具入口可以进入名字生成器页面
- [x] 主导航正确添加工具入口
- [x] 依赖注入正确配置
