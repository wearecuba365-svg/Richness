package com.example.util

import com.example.data.model.NotebookEntryEntity
import java.util.Locale

/**
 * Category of the detected pattern.
 */
enum class PatternCategory(val displayName: String, val iconEmoji: String) {
    EMOTIONAL_TONE("Emotional Tone", "🕊️"),
    RECURRING_THEME("Recurring Theme", "🌱"),
    MONEY_MINDSET("Money Mindset", "🪙"),
    HABIT_FOCUS("Habit & Focus", "⚡"),
    DECISION_MAKING("Decision & Clarity", "👑")
}

/**
 * Represents an automatically detected recurring word, theme, or emotional tone.
 */
data class DetectedPattern(
    val id: String,
    val themeOrKeyword: String,
    val title: String,
    val plainLanguageObservation: String,
    val gentleReflectionPrompt: String,
    val occurrencesCount: Int,
    val matchingEntryIds: List<Long>,
    val category: PatternCategory,
    val emoji: String,
    val timePeriodLabel: String = "Past 30 Days"
)

/**
 * Lightweight, non-clinical, supportive auto-pattern detection engine.
 * Scans user's existing journal, reflections, and money mindset entries over a rolling period.
 */
object AutoPatternDetector {

    private const val ROLLING_WINDOW_DAYS = 30L
    private const val MS_PER_DAY = 24 * 60 * 60 * 1000L

    // Curated standard English stop words to ignore in dynamic keyword extraction
    private val STOP_WORDS = setOf(
        "the", "and", "that", "have", "for", "not", "with", "you", "this", "but", "his", "from",
        "they", "say", "her", "she", "will", "one", "all", "would", "there", "their", "what",
        "out", "about", "who", "get", "which", "when", "make", "can", "like", "time", "just",
        "him", "know", "take", "people", "into", "year", "your", "good", "some", "could",
        "them", "see", "other", "than", "then", "now", "look", "only", "come", "its", "over",
        "think", "also", "back", "after", "use", "two", "how", "our", "work", "first", "well",
        "way", "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
        "was", "were", "been", "being", "had", "has", "are", "is", "am", "doing", "did", "does",
        "very", "much", "more", "many", "such", "own", "same", "so", "too", "very", "can", "will",
        "should", "would", "may", "might", "must", "shall", "freeform", "reflection", "vault",
        "entry", "note", "title", "today", "yesterday", "tomorrow", "page", "step"
    )

    private data class CuratedPatternRule(
        val id: String,
        val themeName: String,
        val primaryKeyword: String,
        val keywords: List<String>,
        val category: PatternCategory,
        val emoji: String,
        val singularObservation: (String, Int) -> String,
        val pluralObservation: (String, Int) -> String,
        val gentlePrompt: String
    )

    private val CURATED_RULES = listOf(
        CuratedPatternRule(
            id = "theme_guilt",
            themeName = "Guilt & Self-Worth",
            primaryKeyword = "guilt",
            keywords = listOf("guilt", "guilty", "undeserving", "unworthy", "shame", "imposter", "deserve", "not worthy"),
            category = PatternCategory.EMOTIONAL_TONE,
            emoji = "🕊️",
            singularObservation = { kw, count -> "This month you mentioned '$kw' $count time." },
            pluralObservation = { kw, count -> "This month you mentioned '$kw' $count times." },
            gentlePrompt = "Noticing feelings around guilt helps you gently reframe old stories into grounded sovereign worth."
        ),
        CuratedPatternRule(
            id = "theme_starting_over",
            themeName = "Starting Over & Renewal",
            primaryKeyword = "starting over",
            keywords = listOf("starting over", "start over", "fresh start", "reset", "begin again", "reinvent", "restart", "new beginning"),
            category = PatternCategory.RECURRING_THEME,
            emoji = "🌱",
            singularObservation = { _, count -> "You've written about 'starting over' $count time this month." },
            pluralObservation = { _, count -> "You've written about 'starting over' several times this month ($count entries)." },
            gentlePrompt = "Every fresh start is an opportunity to direct your subconscious mind with clean, renewed conviction."
        ),
        CuratedPatternRule(
            id = "theme_scarcity_abundance",
            themeName = "Money Mindset & Abundance",
            primaryKeyword = "scarcity & abundance",
            keywords = listOf("scarcity", "not enough", "abundance", "wealthy", "prosperity", "overflow", "plenty", "fear of loss", "tight with money"),
            category = PatternCategory.MONEY_MINDSET,
            emoji = "🪙",
            singularObservation = { _, count -> "You explored financial abundance and money beliefs $count time this month." },
            pluralObservation = { _, count -> "You explored financial abundance and money beliefs in $count entries this month." },
            gentlePrompt = "Observing your financial self-talk builds awareness so you can choose prosperity consciousness."
        ),
        CuratedPatternRule(
            id = "theme_doubt_faith",
            themeName = "Doubt to Conviction",
            primaryKeyword = "doubt",
            keywords = listOf("doubt", "hesitation", "second guess", "uncertainty", "uncertain", "wavering", "transmut"),
            category = PatternCategory.EMOTIONAL_TONE,
            emoji = "⚡",
            singularObservation = { kw, count -> "This month you wrote about '$kw' $count time." },
            pluralObservation = { kw, count -> "This month you reflected on '$kw' and finding certainty in $count entries." },
            gentlePrompt = "Napoleon Hill taught that doubt is simply the prelude to building indestructible applied faith."
        ),
        CuratedPatternRule(
            id = "theme_definite_purpose",
            themeName = "Definite Purpose & Clarity",
            primaryKeyword = "purpose & clarity",
            keywords = listOf("definite aim", "purpose", "calling", "vision", "clarity", "focus", "core mission"),
            category = PatternCategory.RECURRING_THEME,
            emoji = "🧭",
            singularObservation = { _, count -> "You contemplated your Definite Purpose $count time this month." },
            pluralObservation = { _, count -> "You affirmed your Definite Purpose and core focus in $count entries this month." },
            gentlePrompt = "Revisiting your core vision cements your Definite Chief Aim into burning subconscious desire."
        ),
        CuratedPatternRule(
            id = "theme_discipline_habits",
            themeName = "Discipline & Consistency",
            primaryKeyword = "discipline",
            keywords = listOf("discipline", "consistency", "routine", "habit", "daily ritual", "persistence", "procrastinat"),
            category = PatternCategory.HABIT_FOCUS,
            emoji = "🔥",
            singularObservation = { _, count -> "You mentioned discipline and daily consistency $count time this month." },
            pluralObservation = { _, count -> "You focused on discipline and consistent habits in $count entries this month." },
            gentlePrompt = "Tracking daily habits creates compounding momentum toward your highest goals."
        ),
        CuratedPatternRule(
            id = "theme_gratitude_giving",
            themeName = "Gratitude & Generosity",
            primaryKeyword = "gratitude",
            keywords = listOf("gratitude", "grateful", "thankful", "blessed", "giving", "generosity", "tithing", "service"),
            category = PatternCategory.RECURRING_THEME,
            emoji = "✨",
            singularObservation = { _, count -> "You expressed gratitude and the spirit of giving $count time this month." },
            pluralObservation = { _, count -> "You shared reflections on gratitude and generosity in $count entries this month." },
            gentlePrompt = "Gratitude expands your capacity to receive and magnetizes sovereign prosperity."
        ),
        CuratedPatternRule(
            id = "theme_decision_making",
            themeName = "Decisiveness & Crossroads",
            primaryKeyword = "decision",
            keywords = listOf("decision", "decide", "choice", "crossroad", "bold move", "tradeoff", "verdict"),
            category = PatternCategory.DECISION_MAKING,
            emoji = "👑",
            singularObservation = { _, count -> "You logged a key decision $count time this month." },
            pluralObservation = { _, count -> "You documented key choices and decision rationale in $count entries this month." },
            gentlePrompt = "Quick decisions and firm resolve protect your time and energy from procrastination."
        ),
        CuratedPatternRule(
            id = "theme_fear_reframe",
            themeName = "Navigating Fear",
            primaryKeyword = "fear into action",
            keywords = listOf("fear", "afraid", "scared", "worst case", "action today", "anxiety", "worried"),
            category = PatternCategory.EMOTIONAL_TONE,
            emoji = "🛡️",
            singularObservation = { _, count -> "You worked through fear into action $count time this month." },
            pluralObservation = { _, count -> "You actively reframed fear into decisive action in $count entries this month." },
            gentlePrompt = "Taking immediate constructive action dissolves anxiety and restores sovereign composure."
        ),
        CuratedPatternRule(
            id = "theme_calm_serenity",
            themeName = "Inner Peace & Serenity",
            primaryKeyword = "peace of mind",
            keywords = listOf("peace", "serene", "calm", "stillness", "equanimity", "breathe", "centered", "tranquil"),
            category = PatternCategory.EMOTIONAL_TONE,
            emoji = "🌊",
            singularObservation = { _, count -> "You reflected on inner peace and stillness $count time this month." },
            pluralObservation = { _, count -> "You returned to inner calm and peace of mind in $count entries this month." },
            gentlePrompt = "A quiet, centered mind is the ultimate sanctuary for clear intuition and inspiration."
        )
    )

    /**
     * Detects recurring themes, emotional tones, and recurring keywords across all notebook entries.
     * Evaluates a rolling period of 30 days. If the user has few entries in the last 30 days,
     * it gracefully considers all existing entries so that users with older entries still see value.
     */
    fun detectPatterns(
        entries: List<NotebookEntryEntity>,
        rollingDays: Int = ROLLING_WINDOW_DAYS.toInt()
    ): List<DetectedPattern> {
        if (entries.isEmpty()) return emptyList()

        val now = System.currentTimeMillis()
        val windowMs = rollingDays * MS_PER_DAY

        // Get entries in rolling window (or fallback to all if fewer than 2 in 30 days)
        val recentEntries = entries.filter { now - it.timestamp <= windowMs }
        val targetEntries = if (recentEntries.size >= 2) recentEntries else entries
        val periodLabel = if (recentEntries.size >= 2) "Last $rollingDays Days" else "All-Time Journal"

        val detectedList = mutableListOf<DetectedPattern>()

        // 1. Scan curated emotional, thematic, and mindset rules
        for (rule in CURATED_RULES) {
            val matchingEntries = targetEntries.filter { entry ->
                entryMatchesAnyKeyword(entry, rule.keywords)
            }

            if (matchingEntries.size >= 2 || (matchingEntries.size >= 1 && targetEntries.size <= 3)) {
                val count = matchingEntries.size
                val obsText = if (count == 1) {
                    rule.singularObservation(rule.primaryKeyword, count)
                } else {
                    rule.pluralObservation(rule.primaryKeyword, count)
                }

                detectedList.add(
                    DetectedPattern(
                        id = rule.id,
                        themeOrKeyword = rule.primaryKeyword,
                        title = rule.themeName,
                        plainLanguageObservation = obsText,
                        gentleReflectionPrompt = rule.gentlePrompt,
                        occurrencesCount = count,
                        matchingEntryIds = matchingEntries.map { it.id },
                        category = rule.category,
                        emoji = rule.emoji,
                        timePeriodLabel = periodLabel
                    )
                )
            }
        }

        // 2. Scan Money Mindset emotion tags if any
        val moneyEntries = targetEntries.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET ||
                    it.tags.contains("Money Mindset", ignoreCase = true)
        }

        if (moneyEntries.isNotEmpty()) {
            val emotionCounts = mutableMapOf<String, MutableList<Long>>()
            val moneyEmotions = listOf("Guilt", "Anxiety", "Scarcity", "Excitement", "Confidence", "Doubt", "Peace", "Gratitude")
            
            for (entry in moneyEntries) {
                for (em in moneyEmotions) {
                    if (entry.tags.contains(em, ignoreCase = true) || entry.content.contains("Emotion: $em", ignoreCase = true)) {
                        emotionCounts.getOrPut(em) { mutableListOf() }.add(entry.id)
                    }
                }
            }

            for ((emotion, idList) in emotionCounts) {
                if (idList.size >= 2 && detectedList.none { it.themeOrKeyword.equals(emotion, ignoreCase = true) }) {
                    val count = idList.size
                    detectedList.add(
                        DetectedPattern(
                            id = "money_emotion_${emotion.lowercase()}",
                            themeOrKeyword = emotion.lowercase(),
                            title = "Money Emotion: $emotion",
                            plainLanguageObservation = "This month you noted feeling '$emotion' $count times during financial decisions.",
                            gentleReflectionPrompt = "Observing emotional triggers around money helps you cultivate calm prosperity.",
                            occurrencesCount = count,
                            matchingEntryIds = idList.distinct(),
                            category = PatternCategory.MONEY_MINDSET,
                            emoji = "🪙",
                            timePeriodLabel = periodLabel
                        )
                    )
                }
            }
        }

        // 3. Dynamic Keyword Frequency Extraction (Non-curated custom user terms)
        val dynamicPatterns = extractDynamicKeywords(targetEntries, detectedList, periodLabel)
        detectedList.addAll(dynamicPatterns)

        // Sort patterns: highest occurrences first
        return detectedList.sortedByDescending { it.occurrencesCount }
    }

    /**
     * Checks if a notebook entry contains any of the specified keywords in its title, content, or specialized fields.
     */
    private fun entryMatchesAnyKeyword(entry: NotebookEntryEntity, keywords: List<String>): Boolean {
        val searchableText = buildSearchableText(entry)
        return keywords.any { kw ->
            searchableText.contains(kw, ignoreCase = true)
        }
    }

    /**
     * Extracts dynamic recurring keywords and two-word phrases that appear in 2 or more distinct entries.
     */
    private fun extractDynamicKeywords(
        entries: List<NotebookEntryEntity>,
        alreadyDetected: List<DetectedPattern>,
        periodLabel: String
    ): List<DetectedPattern> {
        val existingKeywords = alreadyDetected.map { it.themeOrKeyword.lowercase() }.toSet()
        val wordEntryMap = mutableMapOf<String, MutableSet<Long>>()

        for (entry in entries) {
            val text = buildSearchableText(entry).lowercase(Locale.US)
            val tokens = text.split(Regex("[^a-zA-Z0-9'-]+"))
                .map { it.trim('\'', '-') }
                .filter { it.length >= 4 && it !in STOP_WORDS && !it.all { char -> char.isDigit() } }

            val uniqueTokensInEntry = tokens.toSet()
            for (token in uniqueTokensInEntry) {
                wordEntryMap.getOrPut(token) { mutableSetOf() }.add(entry.id)
            }
        }

        val dynamicList = mutableListOf<DetectedPattern>()

        for ((word, entryIds) in wordEntryMap) {
            if (entryIds.size >= 2 && word !in existingKeywords) {
                val count = entryIds.size
                val capitalized = word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() }
                dynamicList.add(
                    DetectedPattern(
                        id = "dynamic_word_$word",
                        themeOrKeyword = word,
                        title = "Recurring Focus: $capitalized",
                        plainLanguageObservation = "You've written about '$word' $count times across your recent reflections.",
                        gentleReflectionPrompt = "Recurring themes highlight where your subconscious focus and energy naturally gravitate.",
                        occurrencesCount = count,
                        matchingEntryIds = entryIds.toList(),
                        category = PatternCategory.RECURRING_THEME,
                        emoji = "💡",
                        timePeriodLabel = periodLabel
                    )
                )
            }
        }

        return dynamicList.take(3) // Limit dynamic keywords to top 3 to keep UI clean and focused
    }

    /**
     * Builds full searchable text representation of an entry.
     */
    fun buildSearchableText(entry: NotebookEntryEntity): String {
        return buildString {
            append(entry.title).append(" ")
            append(entry.content).append(" ")
            append(entry.tags).append(" ")
            append(entry.promptQuestion).append(" ")
            append(entry.moduleTitle).append(" ")
            append(entry.fearText).append(" ")
            append(entry.worstCaseText).append(" ")
            append(entry.actionTodayText).append(" ")
            append(entry.decisionText).append(" ")
            append(entry.decisionRationale).append(" ")
            append(entry.outcomeText).append(" ")
            append(entry.comebackObstacle).append(" ")
            append(entry.comebackPlan)
        }
    }

    /**
     * Filters list of notebook entries to those matching a specific pattern.
     */
    fun getMatchingEntries(entries: List<NotebookEntryEntity>, pattern: DetectedPattern): List<NotebookEntryEntity> {
        val idSet = pattern.matchingEntryIds.toSet()
        return entries.filter { it.id in idSet }
    }
}
