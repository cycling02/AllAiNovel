# 项目界面重构 Spec

## Why
当前界面使用默认Material3样式，缺乏个性，不符合iOS + 纸质书混合风格的设计目标。需要将所有feature模块的界面重构为新的设计风格。

## What Changes
- 书籍列表页：iOS风格，大标题、卡片列表、底部导航
- 编辑器页面：纸质书风格，米黄背景、衬线字体、书页阴影
- 章节列表页：iOS风格，分组列表
- 设置页：iOS风格，分组设置项
- 其他页面：统一使用iOS风格组件

## Impact
- Affected specs: ui-design-system
- Affected code:
  - `feature-book/ui/BookListScreen.kt`
  - `feature-editor/ui/ChapterEditScreen.kt`
  - `feature-chapter/ui/ChapterListScreen.kt`
  - `feature-settings/ui/SettingsScreen.kt`
  - `feature-character/ui/CharacterListScreen.kt`
  - `feature-outline/ui/OutlineListScreen.kt`
  - `feature-worldbuilding/ui/WorldSettingListScreen.kt`

---

## ADDED Requirements

### Requirement: 书籍列表页重构
书籍列表页应采用iOS风格设计。

#### Scenario: 界面布局
- **WHEN** 用户打开书籍列表页
- **THEN** 显示LargeTopAppBar大标题"我的书架"，底部NavigationBar导航，书籍以圆角卡片列表展示

#### Scenario: 书籍卡片
- **WHEN** 显示书籍卡片
- **THEN** 卡片左侧显示彩色封面图标，右侧显示书名、字数，带右箭头指示器

#### Scenario: 空状态
- **WHEN** 书籍列表为空
- **THEN** 显示居中的空状态提示，带图标和引导文字

### Requirement: 编辑器页面重构
编辑器页面应采用纸质书风格设计。

#### Scenario: 纸质书背景
- **WHEN** 用户进入编辑器
- **THEN** 背景使用米黄色(#FFF8E7)，左侧带书页阴影效果

#### Scenario: 衬线字体
- **WHEN** 显示正文内容
- **THEN** 使用衬线字体(Noto Serif)，18sp字号，1.8行间距

#### Scenario: 编辑器主题切换
- **WHEN** 用户切换编辑器主题
- **THEN** 支持浅色/护眼/夜间三种模式

### Requirement: 章节列表页重构
章节列表页应采用iOS风格设计。

#### Scenario: 分组列表
- **WHEN** 显示章节列表
- **THEN** 使用圆角卡片包裹列表项，支持滑动删除

### Requirement: 设置页重构
设置页应采用iOS风格设计。

#### Scenario: 分组设置
- **WHEN** 显示设置页
- **THEN** 设置项按组分类，每组用圆角卡片包裹，组标题在卡片上方

#### Scenario: 设置项样式
- **WHEN** 显示单个设置项
- **THEN** 左侧显示标题，右侧显示当前值和右箭头

### Requirement: 通用组件样式
所有页面应使用统一的设计规范。

#### Scenario: 圆角规范
- **WHEN** 显示卡片或按钮
- **THEN** 大卡片16dp圆角，小卡片12dp圆角，按钮10dp圆角

#### Scenario: 间距规范
- **WHEN** 布局元素
- **THEN** 页面边距16dp，卡片内边距16dp，元素间距8/12/16dp

---

## MODIFIED Requirements

### Requirement: 主题应用
所有页面应使用AllAiNovelTheme主题。

#### Scenario: 主题包装
- **WHEN** 启动应用
- **THEN** 整个应用使用AllAiNovelTheme包装，支持深色模式

---

## REMOVED Requirements

### Requirement: Material3默认样式
**Reason**: 不符合产品风格定位
**Migration**: 替换为iOS + 纸质书混合风格
