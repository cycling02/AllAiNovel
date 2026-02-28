# Checklist

## 核心模块
- [x] core-common 模块创建完成，无外部依赖
- [x] core-model 模块创建完成，包含核心数据模型
- [x] core-ui 模块创建完成，包含主题和通用 UI 组件
- [x] core-database 模块创建完成，包含 Room 数据库实现
- [x] core-network 模块创建完成，包含网络请求实现
- [x] core-datastore 模块创建完成，包含偏好设置实现

## 功能模块
- [x] feature-book 模块创建完成，实现 MVI 模式
- [x] feature-chapter 模块创建完成，实现 MVI 模式
- [x] feature-editor 模块创建完成，实现 MVI 模式
- [x] feature-ai 模块创建完成，实现 MVI 模式
- [x] feature-settings 模块创建完成，实现 MVI 模式

## 模块重构
- [x] domain 模块重构完成，依赖 core-model
- [x] data 模块重构完成，依赖 core-database、core-network、core-datastore
- [x] presentation 模块重构完成，依赖各 feature 模块
- [x] app 模块重构完成，依赖正确

## 依赖注入
- [x] 各核心模块 DI 配置正确
- [x] 各功能模块 DI 配置正确
- [x] Hilt 注入正常工作

## 导航系统
- [x] 导航图配置正确
- [x] 各功能模块导航目的地配置正确
- [x] 页面跳转正常工作

## 项目配置
- [x] settings.gradle.kts 包含所有模块
- [x] libs.versions.toml 依赖版本正确
- [x] 根 build.gradle.kts 插件声明正确

## 代码清理
- [ ] 原 core 模块待删除（保留以确保兼容）
- [x] 无冗余代码残留

## 构建验证
- [ ] 项目可正常构建
- [ ] 无编译错误
- [ ] 无依赖冲突
