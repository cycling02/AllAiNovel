package com.cycling.domain.usecase.ai

import com.cycling.domain.model.ApiConfig
import com.cycling.domain.model.Chapter
import com.cycling.domain.model.ChapterStatus
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.repository.AiRepository
import javax.inject.Inject

class GenerateChapterFromOutlineUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(
        config: ApiConfig,
        outlineItem: OutlineItem,
        bookTitle: String,
        bookSummary: String,
        previousChapterContent: String? = null,
        nextOutlineItem: OutlineItem? = null
    ): Result<String> {
        val prompt = buildPrompt(
            outlineItem = outlineItem,
            bookTitle = bookTitle,
            bookSummary = bookSummary,
            previousChapterContent = previousChapterContent,
            nextOutlineItem = nextOutlineItem
        )

        return repository.generateContent(
            config = config,
            prompt = prompt,
            maxTokens = 2000
        )
    }

    private fun buildPrompt(
        outlineItem: OutlineItem,
        bookTitle: String,
        bookSummary: String,
        previousChapterContent: String?,
        nextOutlineItem: OutlineItem?
    ): String {
        val levelText = when (outlineItem.level) {
            0 -> "卷"
            1 -> "章"
            2 -> "节"
            else -> "段落"
        }

        val sb = StringBuilder()
        sb.appendLine("请根据以下大纲信息，生成一个完整的网络小说${levelText}内容：")
        sb.appendLine()
        sb.appendLine("【书籍信息】")
        sb.appendLine("书名：$bookTitle")
        sb.appendLine("简介：$bookSummary")
        sb.appendLine()
        sb.appendLine("【当前${levelText}信息】")
        sb.appendLine("标题：${outlineItem.title}")
        if (outlineItem.summary.isNotBlank()) {
            sb.appendLine("概要：${outlineItem.summary}")
        }
        sb.appendLine()

        if (!previousChapterContent.isNullOrBlank()) {
            sb.appendLine("【前文衔接】")
            sb.appendLine("上一章结尾内容：")
            sb.appendLine(previousChapterContent.takeLast(500))
            sb.appendLine()
        }

        if (nextOutlineItem != null) {
            sb.appendLine("【后续预告】")
            sb.appendLine("下一章标题：${nextOutlineItem.title}")
            if (nextOutlineItem.summary.isNotBlank()) {
                sb.appendLine("下一章概要：${nextOutlineItem.summary}")
            }
            sb.appendLine()
        }

        sb.appendLine("【写作要求】")
        sb.appendLine("1. 根据标题和概要展开详细内容")
        sb.appendLine("2. 保持网络小说的叙事风格")
        sb.appendLine("3. 注重情节的连贯性和吸引力")
        sb.appendLine("4. 适当加入对话、描写和内心独白")
        sb.appendLine("5. 字数控制在1000-1500字之间（约1章网文长度）")
        sb.appendLine("6. 直接输出正文内容，不要包含标题")
        sb.appendLine()
        sb.appendLine("请开始生成内容：")

        return sb.toString()
    }
}
