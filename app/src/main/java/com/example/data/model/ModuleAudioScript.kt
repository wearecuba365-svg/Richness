package com.example.data.model

enum class AudioScriptType(val label: String, val badge: String) {
    AFFIRMATION("Affirmation Decree", "AFFIRMATION"),
    MEDITATION("Guided Meditation", "MEDITATION"),
    MANUSCRIPT("Manuscript Chapter", "MANUSCRIPT"),
    CUSTOM("Custom Autosuggestion", "AUTOSUGGESTION")
}

data class ModuleAudioScript(
    val id: String,
    val moduleId: Int,
    val moduleTitle: String,
    val principleName: String,
    val title: String,
    val type: AudioScriptType,
    val description: String,
    val textToSpeak: String,
    val estimatedDurationSeconds: Int = 120,
    val backgroundTheme: String = "Theta Wealth Frequency"
) {
    val sentences: List<String> by lazy {
        textToSpeak
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }
}
