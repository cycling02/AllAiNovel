# Checklist

## 模块创建
- [x] feature-oneclick 模块已创建并配置
- [x] settings.gradle.kts 已添加新模块
- [x] build.gradle.kts 依赖配置正确

## Domain 层
- [x] ParsePromptUseCase 已实现
- [x] GenerateBookStructureUseCase 已实现
- [x] AiRepository 接口已添加 parseAndGenerateBook 方法
- [x] AiRepositoryImpl 已实现解析和生成逻辑

## 状态管理
- [x] OneClickGenerationState 已定义
- [x] OneClickGenerationIntent 已定义
- [x] OneClickGenerationEffect 已定义
- [x] OneClickGenerationViewModel 已实现

## UI 界面
- [x] 输入界面（文本框、选项、模型选择器）已实现
- [x] 生成进度显示组件已实现
- [x] 结果预览界面（书籍、角色、世界观、大纲、章节）已实现
- [x] 结果编辑功能（点击编辑、修改指令）已实现

## 解析和生成逻辑
- [x] 解析 Prompt 设计完成
- [x] 书籍信息生成逻辑已实现
- [x] 角色批量生成逻辑已实现
- [x] 世界观批量生成逻辑已实现
- [x] 大纲结构生成逻辑已实现
- [x] 第一章内容生成逻辑已实现

## 集成
- [x] BookListScreen 已添加"一键生成"入口
- [x] AppNavigation 已添加 oneclick_generation 路由
- [x] 导航配置正确

## 结果应用
- [x] 保存书籍到数据库功能已实现
- [x] 批量保存角色功能已实现
- [x] 批量保存世界观设定功能已实现
- [x] 保存大纲结构功能已实现
- [x] 创建第一章并保存内容功能已实现
- [x] 导航到编辑器功能已实现

## 测试验证
- [ ] 输入自然语言描述能正确解析
- [ ] 分批生成显示正确进度
- [ ] 生成结果能正确预览
- [ ] 能直接编辑生成结果
- [ ] 修改指令能重新生成
- [ ] 应用结果能正确保存到数据库
- [ ] 能从书籍列表导航到一键生成页面
