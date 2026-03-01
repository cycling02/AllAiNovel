package com.cycling.core.network.repository

import com.cycling.domain.model.ApiConfig
import com.cycling.core.network.api.OpenAIApi
import com.cycling.core.network.api.StreamApi
import com.cycling.core.network.model.ChatCompletionRequest
import com.cycling.domain.repository.AiRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepositoryImpl @Inject constructor(
    private val openAIApi: OpenAIApi,
    private val streamApi: StreamApi,
    private val gson: Gson
) : AiRepository {
    
    override suspend fun continueWriting(
        config: ApiConfig,
        context: String,
        maxTokens: Int
    ): Result<String> {
        return try {
            val systemPrompt = buildSystemPrompt()
            val userPrompt = buildUserPrompt(context)
            
            val request = ChatCompletionRequest(
                model = config.model,
                messages = listOf(
                    ChatCompletionRequest.Message(
                        role = "system",
                        content = systemPrompt
                    ),
                    ChatCompletionRequest.Message(
                        role = "user",
                        content = userPrompt
                    )
                ),
                maxTokens = maxTokens,
                temperature = 0.7
            )
            
            val response = openAIApi.chatCompletion(
                fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
                authorization = "Bearer ${config.apiKey}",
                request = request
            )
            
            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("AI返回内容为空"))
            } else {
                Result.success(content)
            }
        } catch (e: HttpException) {
            Result.failure(Exception("API请求失败: ${e.code()} ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("网络错误: ${e.message}"))
        } catch (e: RuntimeException) {
            Result.failure(Exception("续写失败: ${e.message}"))
        }
    }

    override fun continueWritingStream(
        config: ApiConfig,
        context: String,
        maxTokens: Int
    ): Flow<String> {
        val systemPrompt = buildSystemPrompt()
        val userPrompt = buildUserPrompt(context)
        
        val request = ChatCompletionRequest(
            model = config.model,
            messages = listOf(
                ChatCompletionRequest.Message(
                    role = "system",
                    content = systemPrompt
                ),
                ChatCompletionRequest.Message(
                    role = "user",
                    content = userPrompt
                )
            ),
            maxTokens = maxTokens,
            temperature = 0.7,
            stream = true
        )
        
        val requestBody = gson.toJson(request)
        
        return streamApi.streamChatCompletion(
            fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
            authorization = "Bearer ${config.apiKey}",
            requestBody = requestBody
        )
    }

    override fun continueWritingWithContextStream(
        config: ApiConfig,
        context: String,
        bookContext: String,
        maxTokens: Int
    ): Flow<String> {
        val systemPrompt = buildSystemPromptWithContext(bookContext)
        val userPrompt = buildUserPrompt(context)

        val request = ChatCompletionRequest(
            model = config.model,
            messages = listOf(
                ChatCompletionRequest.Message(
                    role = "system",
                    content = systemPrompt
                ),
                ChatCompletionRequest.Message(
                    role = "user",
                    content = userPrompt
                )
            ),
            maxTokens = maxTokens,
            temperature = 0.7,
            stream = true
        )

        val requestBody = gson.toJson(request)

        return streamApi.streamChatCompletion(
            fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
            authorization = "Bearer ${config.apiKey}",
            requestBody = requestBody
        )
    }
    
    private fun buildSystemPrompt(): String {
        return """
            你是一位专业的网络小说作家助手。你的任务是根据已有的小说内容，继续创作后续情节。
            要求：
            1. 保持文风一致，延续已有内容的写作风格
            2. 情节发展要合理，符合人物性格和故事逻辑
            3. 注意细节描写，包括环境、动作、心理活动等
            4. 对话要符合人物性格特点
            5. 控制节奏，张弛有度
            6. 不要添加任何解释说明，直接输出续写内容
        """.trimIndent()
    }

    private fun buildSystemPromptWithContext(bookContext: String): String {
        return """
            你是一位专业的网络小说作家助手。你的任务是根据已有的小说内容，继续创作后续情节。
            
            以下是本书的设定资料，请在续写时参考并保持一致性：
            
            $bookContext
            
            要求：
            1. 保持文风一致，延续已有内容的写作风格
            2. 情节发展要合理，符合人物性格和故事逻辑
            3. 角色行为必须符合其性格设定
            4. 注意细节描写，包括环境、动作、心理活动等
            5. 对话要符合人物性格特点
            6. 控制节奏，张弛有度
            7. 不要添加任何解释说明，直接输出续写内容
        """.trimIndent()
    }
    
    private fun buildUserPrompt(context: String): String {
        return """
            请根据以下内容继续创作：
            
            $context
            
            请直接输出续写内容，不要添加任何解释说明。
        """.trimIndent()
    }

    override suspend fun generateOutline(
        config: ApiConfig,
        topic: String,
        summary: String,
        chapterCount: Int,
        levelCount: Int
    ): Result<String> {
        return try {
            val systemPrompt = buildOutlineSystemPrompt()
            val userPrompt = buildOutlineUserPrompt(topic, summary, chapterCount, levelCount)

            val request = ChatCompletionRequest(
                model = config.model,
                messages = listOf(
                    ChatCompletionRequest.Message(
                        role = "system",
                        content = systemPrompt
                    ),
                    ChatCompletionRequest.Message(
                        role = "user",
                        content = userPrompt
                    )
                ),
                maxTokens = 4000,
                temperature = 0.8
            )

            val response = openAIApi.chatCompletion(
                fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
                authorization = "Bearer ${config.apiKey}",
                request = request
            )

            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("AI返回内容为空"))
            } else {
                Result.success(content)
            }
        } catch (e: HttpException) {
            Result.failure(Exception("API请求失败: ${e.code()} ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("网络错误: ${e.message}"))
        } catch (e: RuntimeException) {
            Result.failure(Exception("大纲生成失败: ${e.message}"))
        }
    }

    private fun buildOutlineSystemPrompt(): String {
        return """
            你是一位专业的网络小说大纲策划师。你的任务是根据用户提供的主题和简介，生成一个结构化的故事大纲。

            你需要以JSON格式输出大纲，格式如下：
            {
              "items": [
                {
                  "title": "卷/章标题",
                  "summary": "内容概述",
                  "level": 1,
                  "children": [
                    {
                      "title": "子章节标题",
                      "summary": "子章节概述",
                      "level": 2,
                      "children": []
                    }
                  ]
                }
              ]
            }

            要求：
            1. 遵循网文创作的起承转合结构
            2. 设置合理的冲突和高潮点
            3. 确保情节连贯、逻辑自洽
            4. 只输出JSON，不要添加其他说明文字
            5. 确保输出是有效的JSON格式
        """.trimIndent()
    }

    private fun buildOutlineUserPrompt(
        topic: String,
        summary: String,
        chapterCount: Int,
        levelCount: Int
    ): String {
        return """
            请根据以下信息生成一个结构化的故事大纲：

            主题/题材: $topic
            故事简介: $summary
            目标章节数: $chapterCount
            大纲层级: ${levelCount}级

            请直接输出JSON格式的大纲，不要添加任何解释说明。
        """.trimIndent()
    }

    override suspend fun generateCharacter(
        config: ApiConfig,
        characterType: String?,
        gender: String?,
        description: String?,
        count: Int
    ): Result<String> {
        return try {
            val systemPrompt = buildCharacterSystemPrompt()
            val userPrompt = buildCharacterUserPrompt(characterType, gender, description, count)

            val request = ChatCompletionRequest(
                model = config.model,
                messages = listOf(
                    ChatCompletionRequest.Message(
                        role = "system",
                        content = systemPrompt
                    ),
                    ChatCompletionRequest.Message(
                        role = "user",
                        content = userPrompt
                    )
                ),
                maxTokens = 3000,
                temperature = 0.9
            )

            val response = openAIApi.chatCompletion(
                fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
                authorization = "Bearer ${config.apiKey}",
                request = request
            )

            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("AI返回内容为空"))
            } else {
                Result.success(content)
            }
        } catch (e: HttpException) {
            Result.failure(Exception("API请求失败: ${e.code()} ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("网络错误: ${e.message}"))
        } catch (e: RuntimeException) {
            Result.failure(Exception("角色生成失败: ${e.message}"))
        }
    }

    private fun buildCharacterSystemPrompt(): String {
        return """
            你是一位专业的网络小说角色设计师。你的任务是根据用户的要求，生成具有鲜明个性的角色档案。

            你需要以JSON格式输出角色数据，格式如下：
            {
              "characters": [
                {
                  "name": "角色姓名",
                  "alias": "别名/绰号",
                  "gender": "性别",
                  "age": "年龄",
                  "personality": "性格特点",
                  "appearance": "外貌描述",
                  "background": "背景故事"
                }
              ]
            }

            要求：
            1. 角色姓名要符合网文风格，有辨识度
            2. 性格要鲜明立体，有优点也有缺点
            3. 外貌描写要生动，突出特征
            4. 背景故事要合理，能解释角色的性格形成
            5. 只输出JSON，不要添加其他说明文字
            6. 确保输出是有效的JSON格式
        """.trimIndent()
    }

    private fun buildCharacterUserPrompt(
        characterType: String?,
        gender: String?,
        description: String?,
        count: Int
    ): String {
        val typeText = characterType?.let { "角色类型: $it" } ?: ""
        val genderText = gender?.let { "性别: $it" } ?: ""
        val descText = description?.let { "要求描述: $it" } ?: ""
        
        return """
            请生成 $count 个网络小说角色：

            $typeText
            $genderText
            $descText

            请直接输出JSON格式的角色数据，不要添加任何解释说明。
        """.trimIndent()
    }

    override suspend fun generateContent(
        config: ApiConfig,
        prompt: String,
        maxTokens: Int
    ): Result<String> {
        return try {
            val request = ChatCompletionRequest(
                model = config.model,
                messages = listOf(
                    ChatCompletionRequest.Message(
                        role = "system",
                        content = "你是一位专业的网络小说作家，擅长创作吸引人的故事内容。"
                    ),
                    ChatCompletionRequest.Message(
                        role = "user",
                        content = prompt
                    )
                ),
                maxTokens = maxTokens,
                temperature = 0.8
            )

            val response = openAIApi.chatCompletion(
                fullUrl = "${config.baseUrl.trimEnd('/')}/chat/completions",
                authorization = "Bearer ${config.apiKey}",
                request = request
            )

            val content = response.choices.firstOrNull()?.message?.content
            if (content.isNullOrBlank()) {
                Result.failure(Exception("AI返回内容为空"))
            } else {
                Result.success(content)
            }
        } catch (e: HttpException) {
            Result.failure(Exception("API请求失败: ${e.code()} ${e.message()}"))
        } catch (e: IOException) {
            Result.failure(Exception("网络错误: ${e.message}"))
        } catch (e: RuntimeException) {
            Result.failure(Exception("内容生成失败: ${e.message}"))
        }
    }
}
