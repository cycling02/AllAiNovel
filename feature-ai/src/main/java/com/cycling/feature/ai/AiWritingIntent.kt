package com.cycling.feature.ai

sealed class AiWritingIntent {
    data object LoadContext : AiWritingIntent()
    data class UpdateContext(val context: String) : AiWritingIntent()
    data class SelectMode(val mode: AiWritingMode) : AiWritingIntent()
    data class GenerateContent(val prompt: String? = null) : AiWritingIntent()
    data class ApplyResult(val result: String) : AiWritingIntent()
    data class Regenerate(val prompt: String? = null) : AiWritingIntent()
    data object ClearResult : AiWritingIntent()
    data object DismissError : AiWritingIntent()
}

enum class AiWritingMode {
    CONTINUE,
    REWRITE,
    EXPAND,
    POLISH
}
