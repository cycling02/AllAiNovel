package com.cycling.core.network.api

import com.cycling.core.network.model.ChatCompletionRequest
import com.cycling.core.network.model.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface OpenAIApi {
    @POST
    suspend fun chatCompletion(
        @Url fullUrl: String,
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
