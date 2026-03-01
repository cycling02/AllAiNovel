package com.cycling.core.network.api

import com.cycling.core.network.model.ChatCompletionChunkResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader

class StreamApi(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson = Gson()
) {
    private val TAG = "StreamApi"

    fun streamChatCompletion(
        fullUrl: String,
        authorization: String,
        requestBody: String
    ): Flow<String> = flow {
        android.util.Log.d(TAG, "流式请求: 开始, url=$fullUrl")
        android.util.Log.d(TAG, "流式请求: requestBody长度=${requestBody.length}")
        
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = requestBody.toRequestBody(mediaType)
        
        val request = Request.Builder()
            .url(fullUrl)
            .header("Authorization", authorization)
            .header("Accept", "text/event-stream")
            .header("Cache-Control", "no-cache")
            .header("Connection", "keep-alive")
            .post(body)
            .build()
        
        var reader: BufferedReader? = null
        var response: okhttp3.Response? = null
        
        try {
            response = okHttpClient.newCall(request).execute()
            
            if (!response.isSuccessful) {
                val errorBody = response.body?.string() ?: "Unknown error"
                android.util.Log.e(TAG, "流式请求: 失败, code=${response.code}, body=$errorBody")
                throw Exception("API请求失败: ${response.code} - $errorBody")
            }
            
            android.util.Log.d(TAG, "流式请求: 连接成功, 开始读取流")
            
            val responseBody = response.body
            if (responseBody == null) {
                android.util.Log.e(TAG, "流式请求: response body 为空")
                throw Exception("Response body 为空")
            }
            
            reader = BufferedReader(InputStreamReader(responseBody.byteStream(), "UTF-8"))
            var line: String?
            var totalContent = StringBuilder()
            var chunkCount = 0
            
            while (reader.readLine().also { line = it } != null) {
                val currentLine = line ?: continue
                
                android.util.Log.v(TAG, "流式请求: 收到行 - ${currentLine.take(100)}")
                
                if (currentLine.startsWith("data: ")) {
                    val data = currentLine.removePrefix("data: ").trim()
                    
                    if (data == "[DONE]") {
                        android.util.Log.d(TAG, "流式请求: 完成, 总长度=${totalContent.length}, chunk数=$chunkCount")
                        break
                    }
                    
                    try {
                        val chunk = gson.fromJson(data, ChatCompletionChunkResponse::class.java)
                        val content = chunk.choices?.firstOrNull()?.delta?.content
                        if (!content.isNullOrEmpty()) {
                            totalContent.append(content)
                            chunkCount++
                            android.util.Log.d(TAG, "流式请求: emit chunk#$chunkCount, 内容='$content', 总长度=${totalContent.length}")
                            emit(content)
                        }
                    } catch (e: Exception) {
                        android.util.Log.w(TAG, "流式请求: 解析失败 - ${e.message}, data=$data")
                    }
                }
            }
            
            android.util.Log.d(TAG, "流式请求: 流结束")
            
        } catch (e: Exception) {
            android.util.Log.e(TAG, "流式请求: 异常 - ${e.message}", e)
            throw e
        } finally {
            try {
                reader?.close()
            } catch (e: Exception) {
                android.util.Log.w(TAG, "关闭reader失败: ${e.message}")
            }
            try {
                response?.body?.close()
            } catch (e: Exception) {
                android.util.Log.w(TAG, "关闭response失败: ${e.message}")
            }
        }
    }.flowOn(Dispatchers.IO)
}
