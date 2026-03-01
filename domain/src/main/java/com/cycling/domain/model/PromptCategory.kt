package com.cycling.domain.model

enum class PromptCategory(val displayName: String) {
    CONTINUE_WRITING("续写"),
    REWRITE("改写"),
    EXPAND("扩写"),
    POLISH("润色"),
    CHARACTER("角色生成"),
    OUTLINE("大纲生成"),
    WORLD_SETTING("世界观设定"),
    CUSTOM("自定义")
}
