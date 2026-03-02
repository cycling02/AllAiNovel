# UI设计系统重构 Spec

## Why
当前UI使用默认的Material3紫色主题，缺乏个性，不符合写作App的沉浸体验需求。需要打造一套混合风格：iOS现代感 + 纸质书沉浸体验，让用户在创作时感受到"真正在写书"的氛围。

## What Changes
- 重构配色系统，区分iOS风格区域和纸质书风格区域
- 新增纸质书专用配色（米黄背景、墨黑文字、护眼模式）
- 新增衬线字体支持，用于编辑器和阅读区域
- 创建毛玻璃效果组件
- 创建纸张纹理背景组件
- 更新圆角、间距、阴影规范
- **BREAKING** 替换现有紫色主题

## Impact
- Affected specs: 所有使用主题的UI模块
- Affected code: 
  - `core-ui/theme/Color.kt`
  - `core-ui/theme/Theme.kt`
  - `core-ui/theme/Type.kt`
  - 所有feature模块的UI组件

---

## ADDED Requirements

### Requirement: iOS风格配色系统
系统应提供iOS风格的配色方案，用于列表、导航、设置等区域。

#### Scenario: 浅色模式
- **WHEN** 用户使用浅色模式
- **THEN** 背景为白色(#FFFFFF)或浅灰(#F2F2F7)，卡片使用毛玻璃效果(80%透明度)，主文字为黑色(#000000)，强调色为系统蓝(#007AFF)

#### Scenario: 深色模式
- **WHEN** 用户使用深色模式
- **THEN** 背景为纯黑(#000000)或深灰(#1C1C1E)，卡片使用毛玻璃效果(60%透明度)，主文字为白色(#FFFFFF)，强调色为系统蓝(#0A84FF)

### Requirement: 纸质书风格配色系统
系统应提供纸质书风格的配色方案，用于编辑器和阅读区域。

#### Scenario: 浅色模式
- **WHEN** 用户在编辑器/阅读区使用浅色模式
- **THEN** 背景为米黄(#FFF8E7)，文字为墨黑(#2C2416)，强调色为朱红(#C84B31)

#### Scenario: 护眼模式
- **WHEN** 用户选择护眼模式
- **THEN** 背景为淡绿(#E8F5E9)，文字为深灰(#37474F)

#### Scenario: 夜间模式
- **WHEN** 用户在编辑器/阅读区使用夜间模式
- **THEN** 背景为深褐(#1E1A14)，文字为米白(#E8DCC8)

### Requirement: 字体系统
系统应提供两套字体系统：无衬线字体用于iOS风格区域，衬线字体用于纸质书风格区域。

#### Scenario: iOS风格区域
- **WHEN** 显示列表、导航、设置等界面
- **THEN** 使用Roboto无衬线字体，大标题34sp，标题22sp，正文17sp

#### Scenario: 纸质书风格区域
- **WHEN** 显示编辑器或阅读内容
- **THEN** 使用Noto Serif衬线字体，章节标题24sp，正文18sp，行间距1.8

### Requirement: 毛玻璃效果组件
系统应提供毛玻璃效果的卡片组件。

#### Scenario: 卡片显示
- **WHEN** 显示书籍卡片、列表项等
- **THEN** 卡片背景半透明，带有模糊效果，圆角16dp

### Requirement: 纸张纹理背景
系统应提供纸张纹理背景效果。

#### Scenario: 编辑器背景
- **WHEN** 用户进入编辑器
- **THEN** 背景显示米黄色+轻微噪点纹理，左侧带有书页阴影效果

### Requirement: 设计规范
系统应遵循统一的布局规范。

#### Scenario: 间距规范
- **WHEN** 布局UI元素
- **THEN** 页面边距16dp，卡片内边距16dp，元素间距8/12/16dp，列表行高最小44dp

#### Scenario: 圆角规范
- **WHEN** 应用圆角
- **THEN** 大卡片16dp，小卡片12dp，按钮10dp，输入框10dp

---

## MODIFIED Requirements

### Requirement: 主题切换
原有单一主题切换改为支持三种编辑器主题模式：浅色、护眼、夜间。

#### Scenario: 主题切换
- **WHEN** 用户切换编辑器主题
- **THEN** 编辑器区域应用对应的纸质书配色，其他区域保持iOS风格

---

## REMOVED Requirements

### Requirement: 紫色主题
**Reason**: 不符合产品定位，缺乏个性
**Migration**: 替换为iOS + 纸质书混合风格
