package com.example.data.remote.gemini

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class GeminiChatService {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val requestAdapter = moshi.adapter(GeminiRequest::class.java)
    private val responseAdapter = moshi.adapter(GeminiResponse::class.java)

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun sendMessage(
        history: List<ChatMessage>,
        userMessage: String,
        role: AdvisorRole,
        modelChoice: GeminiModelChoice,
        userMindsetScore: Int = 50,
        userTier: String = "Novice"
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Local fallback simulation if key is not yet set in Secrets panel
            val fallbackResponse = getSimulatedAdvisorResponse(userMessage, role, userTier)
            return@withContext Result.success(fallbackResponse)
        }

        try {
            // Build conversation history (excluding system and error messages)
            val geminiContents = mutableListOf<GeminiContent>()

            history.filter { !it.isError && it.sender != ChatSender.SYSTEM }
                .takeLast(10) // Keep reasonable context window
                .forEach { msg ->
                    val geminiRole = if (msg.sender == ChatSender.USER) "user" else "model"
                    geminiContents.add(
                        GeminiContent(
                            role = geminiRole,
                            parts = listOf(GeminiPart(text = msg.text))
                        )
                    )
                }

            // Add the new user message
            geminiContents.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userMessage))
                )
            )

            // Contextualized system prompt with user tier and score
            val enhancedSystemPrompt = """
                ${role.systemPrompt}
                
                STUDENT PROFILE:
                - Current Identity Tier: $userTier
                - Mindset Score: $userMindsetScore/100
                
                GUIDELINES:
                - Keep responses inspiring, aristocratic, practical, and punchy.
                - Address the user as "My Friend", "Initiate", "Architect", or "Sovereign".
                - When appropriate, conclude with an actionable 'Master Mind Directive' or reflection decree.
            """.trimIndent()

            val systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = enhancedSystemPrompt))
            )

            val requestObj = GeminiRequest(
                contents = geminiContents,
                systemInstruction = systemInstruction,
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.75f,
                    topP = 0.95f,
                    maxOutputTokens = 2048
                )
            )

            val jsonBody = requestAdapter.toJson(requestObj)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/${modelChoice.modelId}:generateContent?key=$apiKey"

            val httpRequest = Request.Builder()
                .url(url)
                .post(jsonBody.toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody == null) {
                Log.e("GeminiService", "API call failed with code ${response.code}: $responseBody")
                val fallback = getSimulatedAdvisorResponse(userMessage, role, userTier)
                return@withContext Result.success(fallback)
            }

            val parsed = responseAdapter.fromJson(responseBody)
            val replyText = parsed?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            if (!replyText.isNullOrBlank()) {
                Result.success(replyText)
            } else {
                val fallback = getSimulatedAdvisorResponse(userMessage, role, userTier)
                Result.success(fallback)
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Exception during Gemini call", e)
            val fallback = getSimulatedAdvisorResponse(userMessage, role, userTier)
            Result.success(fallback)
        }
    }

    private fun getSimulatedAdvisorResponse(query: String, role: AdvisorRole, userTier: String): String {
        return when (role) {
            AdvisorRole.NAPOLEON_HILL -> """
                Remember this fundamental truth, my aspiring $userTier: *Thoughts are things*, and powerful things at that, when mixed with definiteness of purpose, persistence, and a burning desire for their translation into riches.
                
                Regarding your inquiry: Every failure brings with it the seed of an equivalent advantage. Do not wait for conditions to be perfect; start where you stand and work with whatever tools you have at your command.
                
                **The Master Mind Directive:**
                1. Write down your exact objective with a definite timeline.
                2. Read your affirmation aloud upon rising and before sleeping.
                3. Form your harmonious alliance of trusted minds without delay.
            """.trimIndent()

            AdvisorRole.ANDREW_CARNEGIE -> """
                Let us speak in terms of practical leverage and executive command. 
                
                No single mind can accumulate vast wealth or build lasting institutions in isolation. The accumulation of great fortune calls for power, and power is acquired through highly organized and intelligently directed specialized knowledge.
                
                **Executive Blueprint for $userTier:**
                - Eliminate all drifting and ambiguity in your daily routine.
                - Align yourself with specialists whose capabilities exceed your own in their respective domains.
                - Channel 100% of your energy into your Definite Major Purpose today.
            """.trimIndent()

            AdvisorRole.THOMAS_EDISON -> """
                I see you are wrestling with a challenge. In my laboratories, I never looked upon 10,000 unsuccessful attempts as failures—they were 10,000 discoveries of what does not work.
                
                Persistence is not mere stubbornness; it is the deliberate refusal to accept temporary defeat as permanent surrender. 
                
                **The Transmutation Rule:**
                Channel your current frustration into sustained laboratory experimentation. The breakthrough arrives immediately after the point where the average mind quits.
            """.trimIndent()

            AdvisorRole.SOVEREIGN_STRATEGIST -> """
                Your identity as a **$userTier** is either expanding or contracting right now. Wealth is not something you chase; it is an energetic byproduct of who you have become.
                
                **Sovereign Protocol:**
                1. **Audit Your Environment**: Purge low-frequency distractions and mental drift.
                2. **Calibrate Identity**: Speak and act only from the state of the fulfilled vision.
                3. **Execute High-Leverage Actions**: Do not mistake motion for progress. Complete your high-impact vault quest before sunset.
            """.trimIndent()
        }
    }
}
