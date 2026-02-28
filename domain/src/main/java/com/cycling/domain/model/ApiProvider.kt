package com.cycling.domain.model

enum class ApiProvider(
    val displayName: String,
    val defaultBaseUrl: String,
    val defaultModel: String
) {
    DEEPSEEK(
        displayName = "DeepSeek",
        defaultBaseUrl = "https://api.deepseek.com/v1",
        defaultModel = "deepseek-chat"
    ),
    QWEN(
        displayName = "通义千问",
        defaultBaseUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1",
        defaultModel = "qwen-turbo"
    ),
    ZHIPU(
        displayName = "智谱AI",
        defaultBaseUrl = "https://open.bigmodel.cn/api/paas/v4",
        defaultModel = "glm-4"
    ),
    WENXIN(
        displayName = "文心一言",
        defaultBaseUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop",
        defaultModel = "ernie-bot-4"
    ),
    MOONSHOT(
        displayName = "月之暗面",
        defaultBaseUrl = "https://api.moonshot.cn/v1",
        defaultModel = "moonshot-v1-8k"
    ),
    CUSTOM(
        displayName = "自定义",
        defaultBaseUrl = "",
        defaultModel = ""
    );

    companion object {
        fun fromName(name: String): ApiProvider {
            return entries.find { it.name == name } ?: CUSTOM
        }
    }
}
