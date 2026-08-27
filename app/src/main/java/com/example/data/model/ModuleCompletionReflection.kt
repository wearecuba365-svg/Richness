package com.example.data.model

data class ModuleReflectionPrompt(
    val id: String,
    val promptText: String,
    val helperHint: String = "Inscribe your honest realization and strategic commitment."
)

data class ModuleCompletionReflectionData(
    val moduleId: Int,
    val vaultOrder: Int,
    val principleName: String,
    val vaultTitle: String,
    val prompts: List<ModuleReflectionPrompt>,
    val xpReward: Int = 100
)

data class ModuleCompletionCelebrationInfo(
    val moduleId: Int,
    val vaultOrder: Int,
    val moduleTitle: String,
    val principleName: String,
    val xpEarned: Int,
    val completedTimestamp: Long = System.currentTimeMillis()
)

object ModuleReflectionPromptsProvider {

    private val MODULE_PROMPTS_MAP: Map<Int, List<ModuleReflectionPrompt>> = mapOf(
        0 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_0_1",
                promptText = "What is one belief about money or wealth you inherited that you are now actively rewriting?",
                helperHint = "Reflect on scarcity scripts from childhood, societal norms, or previous financial ceilings."
            ),
            ModuleReflectionPrompt(
                id = "prompt_0_2",
                promptText = "How will possessing true financial sovereignty change your daily state of mind and service to others?",
                helperHint = "Describe the exact freedom, benevolence, and clarity of purpose you will embody."
            )
        ),
        1 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_1_1",
                promptText = "What is the single burning desire that currently consumes your thoughts more than anything else?",
                helperHint = "State your Definite Major Purpose with absolute clarity and zero hedging."
            ),
            ModuleReflectionPrompt(
                id = "prompt_1_2",
                promptText = "What are you fully prepared to sacrifice, surrender, or eliminate to achieve this aim?",
                helperHint = "Identify comfort habits, distractions, or backup escape plans you are burning."
            )
        ),
        2 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_2_1",
                promptText = "In what area of your life have you allowed doubt or cynicism to disguise itself as 'realism'?",
                helperHint = "Examine where skepticism has shielded you from taking ambitious leaps."
            ),
            ModuleReflectionPrompt(
                id = "prompt_2_2",
                promptText = "Describe a moment when unyielding belief carried you through an uncertain outcome.",
                helperHint = "Recall how conviction altered external conditions and produced an unexpected breakthrough."
            )
        ),
        3 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_3_1",
                promptText = "What subconscious script or recurring internal phrase do you catch yourself saying when challenged?",
                helperHint = "Acknowledge the automatic negative thought before transmuting it."
            ),
            ModuleReflectionPrompt(
                id = "prompt_3_2",
                promptText = "What is the new supreme decree you will impress upon your subconscious mind morning and night?",
                helperHint = "Inscribe your authoritative present-tense command to your inner faculties."
            )
        ),
        4 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_4_1",
                promptText = "What high-leverage skill or specialized knowledge must you master next to expand your competitive moat?",
                helperHint = "Focus on scarce, high-value capabilities that compound your economic power."
            ),
            ModuleReflectionPrompt(
                id = "prompt_4_2",
                promptText = "How will you organize and direct this knowledge into a practical plan for tangible value creation?",
                helperHint = "Outline how your specialized expertise directly serves a specific market or enterprise."
            )
        ),
        5 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_5_1",
                promptText = "What is an audacious, creative solution to a current bottleneck that you haven't yet dared to test?",
                helperHint = "Activate synthetic and creative imagination to formulate a radical alternative."
            ),
            ModuleReflectionPrompt(
                id = "prompt_5_2",
                promptText = "How can you combine two previously unrelated ideas into a novel value proposition?",
                helperHint = "Synthesize disparate concepts into a distinct, high-impact advantage."
            )
        ),
        6 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_6_1",
                promptText = "When an initial plan faltered in the past, did you treat it as temporary defeat or permanent failure? What did it teach you?",
                helperHint = "Reflect on how defeat is merely a signal that your plans were unsound, not that your purpose is flawed."
            ),
            ModuleReflectionPrompt(
                id = "prompt_6_2",
                promptText = "What is one structural flaw or weak point in your current execution plan that needs immediate reinforcement?",
                helperHint = "Identify operational vulnerabilities before they compound into obstacles."
            )
        ),
        7 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_7_1",
                promptText = "What critical decision have you been postponing out of hesitation or fear of judgment?",
                helperHint = "Name the decision directly and resolve to eliminate procrastination."
            ),
            ModuleReflectionPrompt(
                id = "prompt_7_2",
                promptText = "Recall a time you made a prompt, irreversible decision—how did that speed transform the trajectory?",
                helperHint = "Observe how decisive leaders reach decisions quickly and change them slowly, if ever."
            )
        ),
        8 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_8_1",
                promptText = "What is one time you almost gave up on a vital goal but chose to endure—what gave you that second wind?",
                helperHint = "Analyze the internal anchor that allowed you to outlast the storm."
            ),
            ModuleReflectionPrompt(
                id = "prompt_8_2",
                promptText = "What is your non-negotiable protocol when exhaustion, criticism, or stagnation tests your resolve today?",
                helperHint = "Define the exact ritual or mindset reflex that preserves your momentum."
            )
        ),
        9 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_9_1",
                promptText = "Who are the key minds currently influencing your mental frequency for better or worse?",
                helperHint = "Audit your closest associations and their impact on your ambition and clarity."
            ),
            ModuleReflectionPrompt(
                id = "prompt_9_2",
                promptText = "What complementary strength, perspective, or talent is most missing from your current Master Mind alliance?",
                helperHint = "Identify the strategic ally or mentor who possesses what you lack."
            )
        ),
        10 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_10_1",
                promptText = "How do you actively redirect intense emotional or restless energy into creative, productive output?",
                helperHint = "Describe how you transmute urges, frustrations, or passions into focused work."
            ),
            ModuleReflectionPrompt(
                id = "prompt_10_2",
                promptText = "What lower-frequency distraction can you consciously channel into fuel for your life's paramount enterprise?",
                helperHint = "Convert scattered attention into concentrated magnetic drive."
            )
        ),
        11 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_11_1",
                promptText = "What emotion (fear, enthusiasm, gratitude, certainty) have you most frequently transmitted to your subconscious lately?",
                helperHint = "Examine the dominant emotional state feeding your subconscious garden."
            ),
            ModuleReflectionPrompt(
                id = "prompt_11_2",
                promptText = "What positive, triumphant mental seed will you consciously plant tonight before drifting to sleep?",
                helperHint = "Formulate the exact thought of abundance you will hand over to your subconscious."
            )
        ),
        12 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_12_1",
                promptText = "How do you protect your mental bandwidth and focus environment from external static and negative broadcasts?",
                helperHint = "Detail your boundaries against cynicism, doom-scrolling, and toxic chatter."
            ),
            ModuleReflectionPrompt(
                id = "prompt_12_2",
                promptText = "When is your mind at its highest creative frequency, and how will you protect that golden window?",
                helperHint = "Designate your peak cognitive hours for uninterrupted strategic output."
            )
        ),
        13 to listOf(
            ModuleReflectionPrompt(
                id = "prompt_13_1",
                promptText = "Recall an instance when a sudden intuitive flash or hunch guided you to the right strategic move.",
                helperHint = "Reflect on how Infinite Intelligence communicated through your Sixth Sense."
            ),
            ModuleReflectionPrompt(
                id = "prompt_13_2",
                promptText = "What paramount question or dilemma do you now present to your higher faculty for intuitive counsel?",
                helperHint = "Ask with quiet reverence and open receptive stillness."
            )
        )
    )

    fun getPromptsForModule(moduleId: Int): List<ModuleReflectionPrompt> {
        return MODULE_PROMPTS_MAP[moduleId] ?: listOf(
            ModuleReflectionPrompt(
                id = "default_prompt_1",
                promptText = "What is the single most transformative realization you gained from studying this principle?",
                helperHint = "Inscribe your core philosophical breakthrough."
            ),
            ModuleReflectionPrompt(
                id = "default_prompt_2",
                promptText = "What concrete micro-action will you take in the next 24 hours to embody this principle?",
                helperHint = "Translate understanding into immediate physical execution."
            )
        )
    }

    fun getReflectionDataForModule(module: ModuleEntity): ModuleCompletionReflectionData {
        val prompts = getPromptsForModule(module.id)
        return ModuleCompletionReflectionData(
            moduleId = module.id,
            vaultOrder = module.order,
            principleName = module.originalPrinciple,
            vaultTitle = module.title,
            prompts = prompts,
            xpReward = 100
        )
    }

    /**
     * Formats multiple prompt answers into a single cohesive notebook entry content
     */
    fun formatCombinedReflection(
        prompts: List<ModuleReflectionPrompt>,
        answers: Map<String, String>
    ): String {
        val sb = StringBuilder()
        prompts.forEachIndexed { index, prompt ->
            val answer = answers[prompt.id]?.trim() ?: ""
            sb.append("PROMPT ${index + 1}: ${prompt.promptText}\n\n")
            sb.append("REFLECTION:\n")
            sb.append(if (answer.isNotBlank()) answer else "[No reflection provided]")
            if (index < prompts.size - 1) {
                sb.append("\n\n---\n\n")
            }
        }
        return sb.toString()
    }

    /**
     * Parses a combined reflection content back into prompt answers if previously saved
     */
    fun parseSavedAnswers(
        prompts: List<ModuleReflectionPrompt>,
        savedContent: String
    ): Map<String, String> {
        val result = mutableMapOf<String, String>()
        if (savedContent.isBlank()) return result

        val parts = savedContent.split("\n\n---\n\n")
        prompts.forEachIndexed { index, prompt ->
            if (index < parts.size) {
                val part = parts[index]
                val reflectionMarker = "REFLECTION:\n"
                val idx = part.indexOf(reflectionMarker)
                if (idx != -1) {
                    val ans = part.substring(idx + reflectionMarker.length).trim()
                    if (ans != "[No reflection provided]") {
                        result[prompt.id] = ans
                    }
                } else {
                    result[prompt.id] = part.trim()
                }
            }
        }
        return result
    }
}
