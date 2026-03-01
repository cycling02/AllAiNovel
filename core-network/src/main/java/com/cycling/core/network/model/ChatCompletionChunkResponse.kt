package com.cycling.core.network.model

import com.google.gson.annotations.SerializedName

data class ChatCompletionChunkResponse(
    val id: String? = null,
    @SerializedName("object")
    val objectType: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<ChunkChoice>? = null
) {
    data class ChunkChoice(
        val index: Int? = null,
        val delta: Delta? = null,
        @SerializedName("finish_reason")
        val finishReason: String? = null
    )

    data class Delta(
        val role: String? = null,
        val content: String? = null
    )
}
