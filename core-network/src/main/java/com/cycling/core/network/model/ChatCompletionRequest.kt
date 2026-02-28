package com.cycling.core.network.model

import com.google.gson.annotations.SerializedName

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1000,
    val temperature: Double = 0.7,
    val stream: Boolean = false
) {
    data class Message(
        val role: String,
        val content: String
    )
}
