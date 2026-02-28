package com.cycling.domain.usecase.chapter

import com.cycling.domain.model.Chapter
import com.cycling.domain.repository.ChapterRepository
import javax.inject.Inject

class UpdateChapterUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    suspend operator fun invoke(chapter: Chapter) = repository.updateChapter(chapter)
}
