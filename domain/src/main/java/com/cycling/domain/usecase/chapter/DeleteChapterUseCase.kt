package com.cycling.domain.usecase.chapter

import com.cycling.domain.repository.ChapterRepository
import javax.inject.Inject

class DeleteChapterUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    suspend operator fun invoke(chapterId: Long) = repository.deleteChapterById(chapterId)
}
