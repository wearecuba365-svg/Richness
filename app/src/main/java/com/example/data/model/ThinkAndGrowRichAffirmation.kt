package com.example.data.model

import java.util.Random

/**
 * Data model representing a curated Think and Grow Rich principle quote and daily affirmation.
 */
data class ThinkAndGrowRichAffirmation(
    val id: Int,
    val principleNumber: Int, // 1 to 13, 0 for Core Transmutation / Definiteness
    val principleName: String,
    val quote: String,
    val author: String = "Napoleon Hill",
    val source: String = "Think and Grow Rich",
    val actionPractice: String,
    val coreTheme: String,
    val tags: List<String> = listOf("Think and Grow Rich", "Affirmation")
)

object ThinkAndGrowRichQuotes {

    val quotes: List<ThinkAndGrowRichAffirmation> = listOf(
        // --- Principle 1: DESIRE ---
        ThinkAndGrowRichAffirmation(
            id = 1,
            principleNumber = 1,
            principleName = "Principle 1: Burning Desire",
            quote = "The starting point of all achievement is DESIRE. Keep this constantly in mind. Weak desire brings weak results, just as a small fire makes a small amount of heat.",
            actionPractice = "Visualize your Definite Major Purpose right now as an existing physical reality with intense emotional hunger.",
            coreTheme = "Transmutation of Desire"
        ),
        ThinkAndGrowRichAffirmation(
            id = 2,
            principleNumber = 1,
            principleName = "Principle 1: Burning Desire",
            quote = "There are no limitations to the mind except those we acknowledge. Both poverty and riches are the offspring of thought.",
            actionPractice = "Identify one perceived limitation in your financial horizon and consciously revoke its authority over your actions today.",
            coreTheme = "Mental Freedom"
        ),
        ThinkAndGrowRichAffirmation(
            id = 3,
            principleNumber = 1,
            principleName = "Principle 1: Burning Desire",
            quote = "Every person who wins in any undertaking must be willing to burn his ships and cut off all sources of retreat. Only by so doing can one be sure of maintaining that state of mind known as a burning desire to win.",
            actionPractice = "Decide on your non-negotiables today. Cut away safety-net excuses that dilute your resolve.",
            coreTheme = "Uncompromising Commitment"
        ),

        // --- Principle 2: FAITH ---
        ThinkAndGrowRichAffirmation(
            id = 4,
            principleNumber = 2,
            principleName = "Principle 2: Unshakeable Faith",
            quote = "Faith is the head chemist of the mind. When faith is blended with the vibration of thought, the subconscious mind instantly picks up the vibration, translates it into its spiritual equivalent, and transmits it to Infinite Intelligence.",
            actionPractice = "Recite your daily aim aloud with total certainty, feeling the gratitude of having already attained it.",
            coreTheme = "Spiritual Alchemy"
        ),
        ThinkAndGrowRichAffirmation(
            id = 5,
            principleNumber = 2,
            principleName = "Principle 2: Unshakeable Faith",
            quote = "Whatever the mind can conceive and believe, the mind can achieve.",
            actionPractice = "Conceive the highest expression of your craft and allow zero space for doubt to enter your thoughts.",
            coreTheme = "Uncapped Potential"
        ),
        ThinkAndGrowRichAffirmation(
            id = 6,
            principleNumber = 2,
            principleName = "Principle 2: Unshakeable Faith",
            quote = "Faith is the only known antidote for FAILURE! It is the element which transforms the ordinary vibration of thought into the spiritual equivalent.",
            actionPractice = "Transform any recent setback into an asset by viewing it as an indispensable lesson tailored for your elevation.",
            coreTheme = "Triumph Over Failure"
        ),

        // --- Principle 3: AUTO-SUGGESTION ---
        ThinkAndGrowRichAffirmation(
            id = 7,
            principleNumber = 3,
            principleName = "Principle 3: Auto-Suggestion",
            quote = "Your subconscious mind recognizes and acts only upon thoughts which have been well-mixed with emotion or feeling. It accepts orders given in a state of absolute faith.",
            actionPractice = "Close your eyes for 60 seconds and feel the emotional frequency of your wealth target realized.",
            coreTheme = "Subconscious Programming"
        ),
        ThinkAndGrowRichAffirmation(
            id = 8,
            principleNumber = 3,
            principleName = "Principle 3: Auto-Suggestion",
            quote = "You are the master of your destiny. You can influence, direct and control your own environment, making your life what you want it to be.",
            actionPractice = "Affirm: 'I command my attention, my discipline, and my physical destiny. I yield to no external negativity.'",
            coreTheme = "Sovereign Self-Mastery"
        ),

        // --- Principle 4: SPECIALIZED KNOWLEDGE ---
        ThinkAndGrowRichAffirmation(
            id = 9,
            principleNumber = 4,
            principleName = "Principle 4: Specialized Knowledge",
            quote = "Knowledge is only potential power. It becomes power only when, and if, it is organized into definite plans of action, and directed to a definite end.",
            actionPractice = "Apply one piece of specialized domain knowledge today toward executing a high-leverage project.",
            coreTheme = "Actionable Mastery"
        ),
        ThinkAndGrowRichAffirmation(
            id = 10,
            principleNumber = 4,
            principleName = "Principle 4: Specialized Knowledge",
            quote = "The person who stops studying merely because he has finished school is forever hopelessly doomed to mediocrity, no matter what may be his calling.",
            actionPractice = "Dedicate 20 uninterrupted minutes today to mastering the leading edge of your primary craft.",
            coreTheme = "Continuous Elevation"
        ),

        // --- Principle 5: IMAGINATION ---
        ThinkAndGrowRichAffirmation(
            id = 11,
            principleNumber = 5,
            principleName = "Principle 5: Synthetic & Creative Imagination",
            quote = "The imagination is literally the workshop wherein are fashioned all plans created by man. Ideas are the beginning points of all fortunes.",
            actionPractice = "Synthesize two seemingly disparate ideas in your market into a single, high-value breakthrough solution.",
            coreTheme = "Creative Architecture"
        ),
        ThinkAndGrowRichAffirmation(
            id = 12,
            principleNumber = 5,
            principleName = "Principle 5: Synthetic & Creative Imagination",
            quote = "Cherish your visions and your dreams as they are the children of your soul, the blueprints of your ultimate achievements.",
            actionPractice = "Sketch or inscribe the architecture of your grandest five-year goal in your sovereign notebook.",
            coreTheme = "Visionary Blueprint"
        ),

        // --- Principle 6: ORGANIZED PLANNING ---
        ThinkAndGrowRichAffirmation(
            id = 13,
            principleNumber = 6,
            principleName = "Principle 6: Organized Planning",
            quote = "A goal is a dream with a deadline. When defeat comes, accept it as a signal that your plans are not sound, rebuild those plans, and sail once more toward your coveted goal.",
            actionPractice = "Review your top 3 tactical priorities for today and assign rigorous, uncompromising completion deadlines.",
            coreTheme = "Strategic Execution"
        ),
        ThinkAndGrowRichAffirmation(
            id = 14,
            principleNumber = 6,
            principleName = "Principle 6: Organized Planning",
            quote = "Tell the world what you intend to do, but first show it. Deeds, and not words, are what count most.",
            actionPractice = "Let tangible progress speak for you today before sharing your plans with anyone.",
            coreTheme = "Silent Dominance"
        ),

        // --- Principle 7: DECISION ---
        ThinkAndGrowRichAffirmation(
            id = 15,
            principleNumber = 7,
            principleName = "Principle 7: Masterful Decision",
            quote = "Procrastination, the opposite of decision, is a common enemy which practically every person must conquer. Reach decisions promptly and change them slowly, if and when any are changed.",
            actionPractice = "Identify one pending decision you have delayed, analyze the facts immediately, and execute without hesitation.",
            coreTheme = "Decisive Velocity"
        ),
        ThinkAndGrowRichAffirmation(
            id = 16,
            principleNumber = 7,
            principleName = "Principle 7: Masterful Decision",
            quote = "Opinions are the cheapest commodities on the earth. Everyone has a flock of opinions ready to be wished upon anyone who will accept them. Follow your own counsel.",
            actionPractice = "Filter out casual opinions and rely strictly on proven principles and your Definite Major Purpose.",
            coreTheme = "Internal Authority"
        ),

        // --- Principle 8: PERSISTENCE ---
        ThinkAndGrowRichAffirmation(
            id = 17,
            principleNumber = 8,
            principleName = "Principle 8: Sustained Persistence",
            quote = "Persistence is to the character of man what carbon is to steel. There is no substitute for persistence! It cannot be supplanted by any other quality.",
            actionPractice = "When friction arises today, lean in with double intensity. Persistence converts resistance into momentum.",
            coreTheme = "Unbreakable Resolve"
        ),
        ThinkAndGrowRichAffirmation(
            id = 18,
            principleNumber = 8,
            principleName = "Principle 8: Sustained Persistence",
            quote = "Effort only fully releases its reward after a person refuses to quit. Riches do not respond to wishes; they respond only to definite plans, supported by definite desires, through constant persistence.",
            actionPractice = "Recommit to your daily rituals even when fatigue whispers. Sovereign standards are upheld every day.",
            coreTheme = "Relentless Follow-Through"
        ),

        // --- Principle 9: THE MASTER MIND ---
        ThinkAndGrowRichAffirmation(
            id = 19,
            principleNumber = 9,
            principleName = "Principle 9: Power of the Master Mind",
            quote = "No two minds ever come together without thereby creating a third, invisible, intangible force, which may be likened to a third mind. Power is organized and intelligently directed knowledge.",
            actionPractice = "Seek council with high-caliber mentors or align with peers who elevate your standard of expectation.",
            coreTheme = "Synergistic Force"
        ),
        ThinkAndGrowRichAffirmation(
            id = 20,
            principleNumber = 9,
            principleName = "Principle 9: Power of the Master Mind",
            quote = "Men take on the nature and the habits and the power of thought of those with whom they associate in a spirit of sympathy and harmony.",
            actionPractice = "Audit your immediate inner circle and guard your mental environment as your most valuable asset.",
            coreTheme = "Environment Alignment"
        ),

        // --- Principle 10: SEX TRANSMUTATION ---
        ThinkAndGrowRichAffirmation(
            id = 21,
            principleNumber = 10,
            principleName = "Principle 10: Transmutation of Energy",
            quote = "Transmutation means the changing of one element or form of energy into another. The emotion of energy contains the secret of creative ability, genius, and supreme drive.",
            actionPractice = "Channel raw vitality and physical drive directly into your deep creative and strategic work.",
            coreTheme = "Creative Drive"
        ),

        // --- Principle 11: THE SUBCONSCIOUS MIND ---
        ThinkAndGrowRichAffirmation(
            id = 22,
            principleNumber = 11,
            principleName = "Principle 11: The Subconscious Mind",
            quote = "The subconscious mind will translate into its physical equivalent, by the most direct and practical method available, any desire which is transmuted into a state of faith.",
            actionPractice = "Feed your subconscious mind with clean, ambitious, constructive visual commands before rest and upon waking.",
            coreTheme = "Subconscious Command"
        ),
        ThinkAndGrowRichAffirmation(
            id = 23,
            principleNumber = 11,
            principleName = "Principle 11: The Subconscious Mind",
            quote = "You may voluntarily plant in your subconscious mind any plan, thought, or purpose which you desire to translate into its physical or monetary equivalent.",
            actionPractice = "Inscribe a direct command to your subconscious in your notebook today, stating your target with clarity.",
            coreTheme = "Subconscious Imprinting"
        ),

        // --- Principle 12: THE BRAIN ---
        ThinkAndGrowRichAffirmation(
            id = 24,
            principleNumber = 12,
            principleName = "Principle 12: The Brain as Broadcasting Station",
            quote = "The human brain is both a broadcasting and receiving station for the vibration of thought. When stepped up to a high rate of vibration, the mind becomes more receptive to the vibration of thought.",
            actionPractice = "Radiate thoughts of abundance, leadership, and calm sovereignty. What you broadcast returns amplified.",
            coreTheme = "Vibrational Leadership"
        ),

        // --- Principle 13: THE SIXTH SENSE ---
        ThinkAndGrowRichAffirmation(
            id = 25,
            principleNumber = 13,
            principleName = "Principle 13: The Sixth Sense",
            quote = "The sixth sense is the apex of the philosophy. It can be assimilated, understood, and applied only by first mastering the other twelve principles.",
            actionPractice = "Honor your intuitive flashes. Deep wisdom communicates through calm inner stillness.",
            coreTheme = "Intuitive Genius"
        ),
        ThinkAndGrowRichAffirmation(
            id = 26,
            principleNumber = 13,
            principleName = "Principle 13: The Sixth Sense",
            quote = "Through the sixth sense, you will be warned of impending dangers in time to avoid them, and notified of opportunities in time to embrace them.",
            actionPractice = "Practice 3 minutes of total silence today to listen to the whispers of your higher intelligence.",
            coreTheme = "Inner Guidance"
        ),

        // --- CORE NAPOLEON HILL AXIOMS ---
        ThinkAndGrowRichAffirmation(
            id = 27,
            principleNumber = 0,
            principleName = "Definiteness of Purpose",
            quote = "Definiteness of purpose is the starting point of all achievement. Without a purpose and a plan, people drift into trouble.",
            actionPractice = "State your primary target for this week in one crisp sentence and let all actions point toward it.",
            coreTheme = "Laser Precision"
        ),
        ThinkAndGrowRichAffirmation(
            id = 28,
            principleNumber = 0,
            principleName = "The Seed of Benefit",
            quote = "Every adversity, every failure, every heartache carries with it the seed of an equal or greater benefit.",
            actionPractice = "Find the golden opportunity hidden inside whatever challenge is testing you today.",
            coreTheme = "Transmuting Adversity"
        ),
        ThinkAndGrowRichAffirmation(
            id = 29,
            principleNumber = 0,
            principleName = "Mastery in Action",
            quote = "If you cannot do great things, do small things in a great way. Greatness is the sum of relentless daily excellence.",
            actionPractice = "Execute every small routine today with the precision and grace of a master craftsman.",
            coreTheme = "Excellence in Small Things"
        ),
        ThinkAndGrowRichAffirmation(
            id = 30,
            principleNumber = 0,
            principleName = "The Extra Mile",
            quote = "Render more service and better service than that for which you are paid, and sooner or later on the law of increasing returns, you will receive compound reward.",
            actionPractice = "Deliver unexpected, disproportionate value to a client, peer, or partner today without being asked.",
            coreTheme = "Law of Increasing Returns"
        )
    )

    /**
     * Returns a deterministic daily affirmation for each morning based on epoch day.
     * Every morning as the day advances, a new quote from the principles is automatically drawn.
     */
    fun getDailyQuote(epochDay: Long): ThinkAndGrowRichAffirmation {
        if (quotes.isEmpty()) {
            return ThinkAndGrowRichAffirmation(
                id = 0,
                principleNumber = 1,
                principleName = "Principle 1: Burning Desire",
                quote = "The starting point of all achievement is DESIRE.",
                actionPractice = "Anchor your focus.",
                coreTheme = "Desire"
            )
        }
        // Pseudo-random deterministic hash based on epochDay
        val index = (Math.abs(epochDay * 37 + 13) % quotes.size).toInt()
        return quotes[index]
    }

    /**
     * Pulls a random quote, optionally excluding the currently displayed one.
     */
    fun getRandomQuote(excludeId: Int? = null): ThinkAndGrowRichAffirmation {
        val candidates = if (excludeId != null && quotes.size > 1) {
            quotes.filter { it.id != excludeId }
        } else {
            quotes
        }
        return candidates.random()
    }

    fun getQuoteById(id: Int): ThinkAndGrowRichAffirmation? {
        return quotes.firstOrNull { it.id == id }
    }
}
