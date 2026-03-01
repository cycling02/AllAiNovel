package com.cycling.domain.usecase.outline

import com.cycling.domain.model.Chapter
import com.cycling.domain.model.ChapterStatus
import com.cycling.domain.model.OutlineItem
import com.cycling.domain.model.OutlineStatus
import com.cycling.domain.repository.ChapterRepository
import com.cycling.domain.repository.OutlineRepository
import com.cycling.domain.usecase.ai.GenerateChapterFromOutlineUseCase
import com.cycling.domain.usecase.apiconfig.GetDefaultApiConfigUseCase
import com.cycling.domain.usecase.chapter.GetChaptersByBookIdUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateAndSaveChapterUseCase @Inject constructor(
    private val generateChapterFromOutlineUseCase: GenerateChapterFromOutlineUseCase,
    private val getDefaultApiConfigUseCase: GetDefaultApiConfigUseCase,
    private val chapterRepository: ChapterRepository,
    private val outlineRepository: OutlineRepository,
    private val getOutlineByBookIdUseCase: GetOutlineByBookIdUseCase,
    private val getChaptersByBookIdUseCase: GetChaptersByBookIdUseCase
) {
    suspend operator fun invoke(
        outlineItem: OutlineItem,
        bookTitle: String,
        bookSummary: String
    ): Result<Long> {
        android.util.Log.d("GenerateAndSaveChapter", "Starting generation for outline: ${outlineItem.id}, book: ${outlineItem.bookId}")

        val apiConfig = getDefaultApiConfigUseCase().first()
        if (apiConfig == null) {
            android.util.Log.e("GenerateAndSaveChapter", "No default API config found")
            return Result.failure(IllegalStateException("未配置默认API，请先在设置中配置AI API"))
        }
        android.util.Log.d("GenerateAndSaveChapter", "API config found: ${apiConfig.name}, model: ${apiConfig.model}")

        val allOutlines = getOutlineByBookIdUseCase(outlineItem.bookId).first()
        val allChapters = getChaptersByBookIdUseCase(outlineItem.bookId).first()
        android.util.Log.d("GenerateAndSaveChapter", "Found ${allOutlines.size} outlines, ${allChapters.size} chapters")

        val sortedOutlines = allOutlines
            .filter { it.level == outlineItem.level }
            .sortedBy { it.sortOrder }
        android.util.Log.d("GenerateAndSaveChapter", "Filtered ${sortedOutlines.size} outlines at level ${outlineItem.level}")

        val currentIndex = sortedOutlines.indexOfFirst { it.id == outlineItem.id }
        if (currentIndex < 0) {
            android.util.Log.e("GenerateAndSaveChapter", "Current outline not found in sorted list. outlineId: ${outlineItem.id}, availableIds: ${sortedOutlines.map { it.id }}")
            return Result.failure(IllegalStateException("找不到当前大纲项"))
        }
        android.util.Log.d("GenerateAndSaveChapter", "Current outline index: $currentIndex")

        val previousOutline = if (currentIndex > 0) sortedOutlines[currentIndex - 1] else null
        val nextOutline = if (currentIndex < sortedOutlines.size - 1) sortedOutlines[currentIndex + 1] else null

        val previousChapterContent = previousOutline?.chapterId?.let { prevChapterId ->
            allChapters.find { it.id == prevChapterId }?.content
        }

        android.util.Log.d("GenerateAndSaveChapter", "Calling AI generation...")
        val generateResult = generateChapterFromOutlineUseCase(
            config = apiConfig,
            outlineItem = outlineItem,
            bookTitle = bookTitle,
            bookSummary = bookSummary,
            previousChapterContent = previousChapterContent,
            nextOutlineItem = nextOutline
        )

        generateResult.onFailure {
            android.util.Log.e("GenerateAndSaveChapter", "AI generation failed: ${it.message}", it)
        }

        return generateResult.mapCatching { content ->
            android.util.Log.d("GenerateAndSaveChapter", "AI generation succeeded, content length: ${content.length}")
            val chapterNumber = allChapters.size + 1
            val wordCount = content.length

            val chapter = Chapter(
                bookId = outlineItem.bookId,
                outlineItemId = outlineItem.id,
                title = outlineItem.title,
                content = content,
                chapterNumber = chapterNumber,
                wordCount = wordCount,
                status = ChapterStatus.DRAFT
            )

            android.util.Log.d("GenerateAndSaveChapter", "Saving chapter: $chapter")
            val chapterId = chapterRepository.insertChapter(chapter)
            android.util.Log.d("GenerateAndSaveChapter", "Chapter saved with id: $chapterId")

            val updatedOutline = outlineItem.copy(
                chapterId = chapterId,
                status = OutlineStatus.COMPLETED
            )
            outlineRepository.updateOutlineItem(updatedOutline)
            android.util.Log.d("GenerateAndSaveChapter", "Outline updated with chapterId: $chapterId")

            chapterId
        }.onFailure {
            android.util.Log.e("GenerateAndSaveChapter", "Failed to save chapter: ${it.message}", it)
        }
    }
}
