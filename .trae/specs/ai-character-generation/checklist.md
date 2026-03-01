# Checklist

## 数据层
- [x] AiRepository 接口新增 generateCharacter 方法定义正确
- [x] AiRepositoryImpl 实现包含专业的角色生成Prompt
- [x] Prompt要求AI返回JSON格式的角色数据
- [x] CharacterGenerationResult 数据类正确定义JSON解析结构

## 用例层
- [x] GenerateCharacterUseCase 正确调用 AiRepository
- [x] JSON解析逻辑正确处理AI返回内容
- [x] 解析失败时返回有意义的错误信息

## 状态和意图
- [x] CharacterListState 新增字段正确初始化
- [x] CharacterListIntent 新增意图类型定义完整

## ViewModel层
- [x] GenerateCharacterUseCase 和 GetDefaultApiConfigUseCase 正确注入
- [x] 未配置API时显示正确提示
- [x] AI生成过程中正确显示加载状态
- [x] 生成成功后正确显示预览对话框
- [x] 应用角色后数据正确保存到数据库

## UI组件
- [x] AiGenerateCharacterDialog 包含所有必要输入字段
- [x] 角色类型和性别选择正确实现
- [x] AiCharacterPreviewDialog 正确展示角色列表
- [x] 预览对话框支持确认应用和取消操作

## 界面集成
- [x] 空状态页面显示"AI生成角色"按钮
- [x] TopAppBar显示AI生成入口图标
- [x] 对话框正确显示和隐藏
- [x] 加载状态正确显示

## 错误处理
- [x] 网络错误正确处理并提示用户
- [x] API错误正确处理并提示用户
- [x] JSON解析错误正确处理
- [x] 用户可重试失败的生成操作
