package com.cycling.domain.usecase.chapter

import com.cycling.domain.model.Chapter
import com.cycling.domain.repository.ChapterRepository
import javax.inject.Inject

class AddChapterUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    suspend operator fun invoke(chapter: Chapter): Long = repository.insertChapter(chapter)
}
