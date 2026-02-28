package com.cycling.domain.usecase.chapter

import com.cycling.domain.model.Chapter
import com.cycling.domain.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChaptersByBookIdUseCase @Inject constructor(
    private val repository: ChapterRepository
) {
    operator fun invoke(bookId: Long): Flow<List<Chapter>> = repository.getChaptersByBookId(bookId)
}
