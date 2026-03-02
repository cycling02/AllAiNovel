# Tasks

- [x] Task 1: 重构配色系统 (Color.kt)
  - [x] 1.1 添加iOS风格配色（浅色/深色模式）
  - [x] 1.2 添加纸质书风格配色（浅色/护眼/夜间模式）
  - [x] 1.3 定义语义化颜色名称（如 surfaceGlass, paperBackground 等）

- [x] Task 2: 重构字体系统 (Type.kt)
  - [x] 2.1 定义iOS风格字体（无衬线，Roboto）
  - [x] 2.2 定义纸质书风格字体（衬线，Noto Serif）
  - [x] 2.3 添加字体大小和行高规范

- [x] Task 3: 重构主题系统 (Theme.kt)
  - [x] 3.1 创建iOS风格ColorScheme
  - [x] 3.2 创建纸质书风格ColorScheme
  - [x] 3.3 支持编辑器主题独立切换（浅色/护眼/夜间）
  - [x] 3.4 更新状态栏颜色逻辑

- [x] Task 4: 创建毛玻璃效果组件
  - [x] 4.1 创建 GlassCard 可组合函数
  - [x] 4.2 支持浅色/深色模式自适应透明度
  - [x] 4.3 添加模糊效果支持

- [x] Task 5: 创建纸张纹理背景组件
  - [x] 5.1 创建 PaperBackground 可组合函数
  - [x] 5.2 添加噪点纹理效果
  - [x] 5.3 添加书页阴影效果

- [x] Task 6: 创建设计规范常量
  - [x] 6.1 定义间距常量（Dimensions）
  - [x] 6.2 定义圆角常量（CornerRadius）
  - [x] 6.3 定义动效时长常量（AnimationDuration）

- [x] Task 7: 创建编辑器主题状态管理
  - [x] 7.1 创建 EditorTheme 枚举（Light/EyeCare/Night）
  - [x] 7.2 创建 EditorThemeState 和 ViewModel
  - [x] 7.3 使用 DataStore 持久化主题偏好

- [x] Task 8: 更新现有组件使用新主题
  - [x] 8.1 更新 EmptyState 组件
  - [x] 8.2 更新 ErrorMessage 组件
  - [x] 8.3 更新 LoadingIndicator 组件
  - [x] 8.4 更新 InputBottomSheet 组件

# Task Dependencies
- [Task 2] depends on [Task 1]
- [Task 3] depends on [Task 1, Task 2]
- [Task 4] depends on [Task 3]
- [Task 5] depends on [Task 3]
- [Task 7] depends on [Task 3]
- [Task 8] depends on [Task 3, Task 4]
