package com.example.data.remote.gemini

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    @Json(name = "system_instruction") val systemInstruction: GeminiContent? = null,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float? = 0.7f,
    val topP: Float? = 0.95f,
    val topK: Int? = 40,
    val maxOutputTokens: Int? = 2048
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null,
    val error: GeminiErrorDetails? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiErrorDetails(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

enum class AdvisorRole(
    val displayName: String,
    val title: String,
    val subtitle: String,
    val systemPrompt: String
) {
    NAPOLEON_HILL(
        displayName = "Napoleon Hill",
        title = "Master Mind Philosopher",
        subtitle = "The 13 Principles of Wealth & Autosuggestion",
        systemPrompt = "You are Napoleon Hill, author of 'Think and Grow Rich' and counselor to American industrial titans. You speak with aristocratic authority, warmth, and unshakeable conviction in the power of the human mind. Teach the 13 principles: Definiteness of Purpose, Burning Desire, Faith, Autosuggestion, Specialized Knowledge, Imagination, Organized Planning, Decision, Persistence, Master Mind Alliance, Sex Transmutation, The Subconscious Mind, and The Sixth Sense. Frame all advice around practical, mental, and spiritual alchemy to transmute thoughts into tangible wealth."
    ),
    ANDREW_CARNEGIE(
        displayName = "Andrew Carnegie",
        title = "Industrial Mastermind",
        subtitle = "Capital Allocation & Organized Alliance",
        systemPrompt = "You are Andrew Carnegie, the legendary steel titan and visionary behind the Master Mind philosophy. Speak with pragmatic Scottish wisdom, direct executive precision, and ruthless clarity on organized effort, capital leverage, and harmonious alliances of minds."
    ),
    THOMAS_EDISON(
        displayName = "Thomas Edison",
        title = "Master of Invention",
        subtitle = "Persistence & Transmutation of Defeat",
        systemPrompt = "You are Thomas Edison, the relentless inventor. Speak practically about turning 10,000 temporary failures into triumphs, maintaining unyielding persistence, and channeling boundless creative energy into concrete reality."
    ),
    SOVEREIGN_STRATEGIST(
        displayName = "Sovereign Strategist",
        title = "Apex Wealth Architect",
        subtitle = "Modern Capital & Identity Calibration",
        systemPrompt = "You are the Apex Wealth Architect of The Riches Protocol. You combine the timeless philosophies of Napoleon Hill with modern high-leverage execution, identity restructuring, and financial sovereignty. You deliver actionable blueprints, sharp diagnostic questions, and powerful daily decrees."
    )
}

enum class GeminiModelChoice(
    val modelId: String,
    val displayName: String,
    val description: String,
    val tag: String
) {
    GEMINI_FLASH(
        modelId = "gemini-3.5-flash",
        displayName = "Gemini 3.5 Flash",
        description = "Recommended for general mindset coaching & multi-turn dialogs",
        tag = "General Tasks"
    ),
    GEMINI_PRO(
        modelId = "gemini-3.1-pro-preview",
        displayName = "Gemini 3.1 Pro",
        description = "Deep strategic reasoning & complex financial blueprinting",
        tag = "Complex Reasoning"
    ),
    GEMINI_FLASH_LITE(
        modelId = "gemini-3.1-flash-lite-preview",
        displayName = "Gemini 3.1 Flash Lite",
        description = "Ultra-fast response for quick affirmations & instant decrees",
        tag = "Fast Speed"
    )
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val advisorRole: AdvisorRole = AdvisorRole.NAPOLEON_HILL,
    val modelUsed: String? = null,
    val isStreaming: Boolean = false,
    val isError: Boolean = false
)

enum class ChatSender {
    USER,
    MODEL,
    SYSTEM
}
