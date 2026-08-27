package com.example.data.model

data class MoneyBlueprintQuestion(
    val id: Int,
    val categoryKey: String,
    val categoryName: String,
    val questionText: String,
    val contextHint: String,
    val lowLabel: String = "Strongly Disagree (0%)",
    val highLabel: String = "Strongly Agree (100%)",
    val options: List<Pair<String, Int>> = listOf(
        "Strongly Disagree — This belief does not limit me at all" to 0,
        "Disagree — Rare or mild occurrence" to 25,
        "Neutral / Sometimes — Occasional subconscious hesitation" to 50,
        "Agree — Frequently shapes my financial behavior" to 75,
        "Strongly Agree — Primary recurring mental ceiling" to 100
    )
)

object MoneyBlueprintQuizQuestions {
    val questions = listOf(
        MoneyBlueprintQuestion(
            id = 1,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_SCARCITY,
            categoryName = "Scarcity & Zero-Sum Mindset",
            questionText = "When I see others achieve immense wealth or scale, I subconsciously feel there is less opportunity left for me in the market.",
            contextHint = "Examines subconscious zero-sum programming vs. infinite abundance awareness."
        ),
        MoneyBlueprintQuestion(
            id = 2,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_GUILT,
            categoryName = "Guilt Around Wealth & Deservedness",
            questionText = "I often feel a subtle guilt, discomfort, or hesitation when charging premium prices or receiving large sums of money.",
            contextHint = "Evaluates subconscious deservedness and emotional friction around receiving value."
        ),
        MoneyBlueprintQuestion(
            id = 3,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE,
            categoryName = "Fear of Financial Loss & Failure",
            questionText = "The anxiety of losing capital or suffering financial setbacks frequently prevents me from taking calculated, high-leverage risks.",
            contextHint = "Identifies risk aversion rooted in fear of temporary defeat rather than strategic evaluation."
        ),
        MoneyBlueprintQuestion(
            id = 4,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT,
            categoryName = "Fear of Judgment & Social Rejection",
            questionText = "I worry that achieving extreme financial abundance will make family, friends, or peers judge, envy, or distance themselves from me.",
            contextHint = "Highlights subconscious self-sabotage to preserve peer group harmony and belonging."
        ),
        MoneyBlueprintQuestion(
            id = 5,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_SELF_WORTH,
            categoryName = "Self-Worth Tied to Money & Imposter Syndrome",
            questionText = "My self-esteem, mood, and sense of personal dignity fluctuate directly with my bank account balance and monthly income.",
            contextHint = "Measures externalization of sovereign worth onto financial metrics."
        ),
        MoneyBlueprintQuestion(
            id = 6,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_SCARCITY,
            categoryName = "Scarcity & Zero-Sum Mindset",
            questionText = "I experience visceral anxiety when spending money, even on critical investments in my own health, tools, education, or growth.",
            contextHint = "Distinguishes prudent stewardship from defensive scarcity hoarding."
        ),
        MoneyBlueprintQuestion(
            id = 7,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_GUILT,
            categoryName = "Guilt Around Wealth & Deservedness",
            questionText = "A subconscious voice tells me that truly moral, noble, or spiritual people should not focus intensely on accumulating immense riches.",
            contextHint = "Uncovers ethical conflict between financial aspiration and moral self-identity."
        ),
        MoneyBlueprintQuestion(
            id = 8,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE,
            categoryName = "Fear of Financial Loss & Failure",
            questionText = "I tend to over-analyze and delay key decisions for weeks or months, trying to eliminate every possible chance of error or loss.",
            contextHint = "Evaluates analysis paralysis and reluctance to transmute defeat into wisdom."
        ),
        MoneyBlueprintQuestion(
            id = 9,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT,
            categoryName = "Fear of Judgment & Social Rejection",
            questionText = "I downplay my financial targets and hide my true wealth ambitions to avoid sounding boastful, elitist, or threatening to others.",
            contextHint = "Reveals protective camouflaging that dims your vision and weakens autosuggestion."
        ),
        MoneyBlueprintQuestion(
            id = 10,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_SELF_WORTH,
            categoryName = "Self-Worth Tied to Money & Imposter Syndrome",
            questionText = "When a financial venture fails or revenue dips, I immediately feel like a personal failure rather than treating it as objective feedback.",
            contextHint = "Identifies conflation between market outcome and inner sovereign capability."
        ),
        MoneyBlueprintQuestion(
            id = 11,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_SCARCITY,
            categoryName = "Scarcity & Zero-Sum Mindset",
            questionText = "I secretly fear that whatever financial security or success I currently possess could vanish unexpectedly at any moment.",
            contextHint = "Measures subconscious precariousness and lack of trust in one's creative generative power."
        ),
        MoneyBlueprintQuestion(
            id = 12,
            categoryKey = MoneyBlueprintResultEntity.PATTERN_GUILT,
            categoryName = "Guilt Around Wealth & Deservedness",
            questionText = "When I experience a financial windfall or surplus, I feel a subconscious impulse to spend or give it away to return to a familiar baseline.",
            contextHint = "Uncovers the subconscious 'wealth thermostat' resetting to a lower comfort zone."
        )
    )
}
