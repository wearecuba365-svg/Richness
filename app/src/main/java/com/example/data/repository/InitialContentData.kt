package com.example.data.repository

import com.example.data.model.BadgeEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.VisionBoardItemEntity
import java.util.Calendar

object InitialContentData {

    fun getInitialModules(): List<ModuleEntity> {
        return listOf(
            ModuleEntity(
                id = 0,
                order = 0,
                title = "The First Vault",
                originalPrinciple = "Money / Wealth Context",
                subtitle = "The philosophy of transmuting intangible mental impulses into tangible physical sovereignty.",
                isUnlocked = true, // Free module
                isCompleted = false,
                xpReward = 150,
                videoTitle = "The Architecture of Riches: Transmutation Over Accumulation",
                videoDuration = "12:40",
                excerptTitle = "The Metaphysics of Value Creation",
                excerptText = """
                    Wealth is not paper, gold, or digits in a ledger. Wealth is an energy pattern born in the mind that compels the physical universe to render physical counter-values.
                    
                    Every sovereign empire in history began not in capital markets, but within an intense internal furnace—a state of consciousness so definite and concentrated that doubt could find no anchor.
                    
                    The First Vault demands a fundamental departure from passive hoping: riches do not respond to wishes. They respond only to definite plans, supported by definite desires, through a persistence that recognizes no permanent defeat.
                """.trimIndent(),
                keyTakeaways = "1. Wealth is a psychological condition before it is a balance sheet.\n2. Riches respond only to definiteness of purpose, not vague hopes.\n3. The mind holds the exact blueprint of your external financial reality.",
                templateTitle = "The Wealth Context Declaration",
                templatePrompt = "Define your current financial baseline, your definitive target, and what value you pledge in return.",
                templateFieldLabel1 = "Exact Capital Sum Desired (e.g. $1,000,000)",
                templateFieldLabel2 = "Exact Service / Value You Will Render In Return",
                templateFieldLabel3 = "Definite Date of Realization",
                questTitle = "Vault Awakening Quest",
                questDescription = "Write your Definite Financial Target on physical paper and read it aloud with intense conviction.",
                questActionPrompt = "Confirm you have declared your target aloud with unyielding focus.",
                notebookPrompt = "What psychological barrier or scarcity conditioning has previously limited your financial ceiling?"
            ),
            ModuleEntity(
                id = 1,
                order = 1,
                title = "The Ignition",
                originalPrinciple = "Desire",
                subtitle = "The starting point of all achievement. Turning a mere wish into an obsession.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 200,
                videoTitle = "Igniting the Burning Obsession",
                videoDuration = "15:10",
                excerptTitle = "The Six Steps to Transmute Desire",
                excerptText = """
                    A burning desire to be and to do is the starting point from which the dreamer must take off. Dreams are not born of indifference, laziness, or lack of ambition.
                    
                    Fix in your mind the exact amount of wealth you desire. It is not sufficient merely to say 'I want plenty of money.' Be definite as to the amount.
                    
                    Determine exactly what you intend to give in return for the money you desire. There is no such reality as 'something for nothing.' Establish a definite date when you intend to possess the money.
                """.trimIndent(),
                keyTakeaways = "1. Definiteness of purpose eliminates mental scatter.\n2. The six-step transmutation protocol turns mental impulse into physical asset.\n3. Burning desire burns all bridges of retreat.",
                templateTitle = "Definite Major Purpose (DMP) Worksheet",
                templatePrompt = "Articulate your Definite Major Purpose with absolute clarity and zero ambiguity.",
                templateFieldLabel1 = "My Singular Paramount Objective",
                templateFieldLabel2 = "Sacrifices & Non-Negotiable Trade-Offs",
                templateFieldLabel3 = "My Daily Non-Negotiable Rituals",
                questTitle = "Bridge Burning Action",
                questDescription = "Identify one safety-net excuse or secondary distraction and formally eliminate it today.",
                questActionPrompt = "Eliminate a major distraction and seal your commitment.",
                notebookPrompt = "If failure was mathematically impossible, what grand enterprise would you initiate today?"
            ),
            ModuleEntity(
                id = 2,
                order = 2,
                title = "Unshakeable Belief",
                originalPrinciple = "Faith",
                subtitle = "Visualizing and believing in the attainment of your desire until it becomes reality.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 200,
                videoTitle = "Faith as the Supreme Alchemy of Mind",
                videoDuration = "14:25",
                excerptTitle = "The Law of State Inducement",
                excerptText = """
                    Faith is the head chemist of the mind. When faith is blended with the vibration of thought, the subconscious mind instantly picks up the vibration, translates it into its spiritual equivalent, and transmits it to Infinite Intelligence.
                    
                    Repetition of affirmation of orders to your subconscious mind is the only known method of voluntary development of the emotion of faith.
                    
                    Belief is not an emotional accident; it is an engineered mental state built brick by brick through daily repeated autosuggestion.
                """.trimIndent(),
                keyTakeaways = "1. Faith is an induced mental state, not passive luck.\n2. Subconscious programming translates your deepest expectation into real opportunities.\n3. Eliminate self-limiting doubt through systematic daily conditioning.",
                templateTitle = "The Unshakeable Belief Audit",
                templatePrompt = "Deconstruct 3 limiting assumptions and replace them with Sovereign Convictions.",
                templateFieldLabel1 = "Limiting Core Assumption Being Replaced",
                templateFieldLabel2 = "New Sovereign Conviction (Stated in Present Tense)",
                templateFieldLabel3 = "Physical Evidence Supporting My New Reality",
                questTitle = "Conviction Immersion Quest",
                questDescription = "Spend 5 minutes in pure silent visualization seeing your goal as already fully realized.",
                questActionPrompt = "Complete the 5-minute sovereign visualization session.",
                notebookPrompt = "What would you do today if you had 100% certainty that your sovereign wealth is guaranteed?"
            ),
            ModuleEntity(
                id = 3,
                order = 3,
                title = "Mind Programming",
                originalPrinciple = "Autosuggestion",
                subtitle = "The medium for influencing the subconscious mind and commanding your inner faculties.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 200,
                videoTitle = "The Command Architecture of Autosuggestion",
                videoDuration = "13:50",
                excerptTitle = "The Mirror & Evening Command Protocol",
                excerptText = """
                    Autosuggestion is the agency of control through which an individual may voluntarily feed his subconscious mind on thoughts of a creative nature, or by neglect, permit destructive thoughts to find their way into this rich garden of the mind.
                    
                    Plain, unemotional words do not influence the subconscious mind. You must mix emotion and feeling into your mental commands.
                    
                    Speak to your subconscious as a commanding Sovereign. It knows no difference between truth and vivid suggestion.
                """.trimIndent(),
                keyTakeaways = "1. The subconscious mind accepts commands backed by emotional conviction.\n2. Morning and night are the two prime windows of subconscious receptivity.\n3. Autosuggestion bridges conscious desire to subconscious automated execution.",
                templateTitle = "Daily Sovereign Decree Script",
                templatePrompt = "Craft your personalized morning and evening proclamation.",
                templateFieldLabel1 = "Morning Awakening Sovereign Decree",
                templateFieldLabel2 = "Mid-Day Calibration Mantra",
                templateFieldLabel3 = "Nocturnal Subconscious Seed Command",
                questTitle = "Mirror Command Ritual",
                questDescription = "Stand before a mirror and recite your Sovereign Decree with absolute eye contact for 2 minutes.",
                questActionPrompt = "Execute the 2-minute mirror command ritual.",
                notebookPrompt = "What internal self-talk patterns do you catch yourself repeating when under pressure?"
            ),
            ModuleEntity(
                id = 4,
                order = 4,
                title = "The Edge",
                originalPrinciple = "Specialized Knowledge",
                subtitle = "Personal experiences or observations organized and intelligently directed toward definite wealth.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 220,
                videoTitle = "Skill Stacking & High-Leverage Competence",
                videoDuration = "16:05",
                excerptTitle = "Knowledge Is Only Potential Power",
                excerptText = """
                    Knowledge will not attract money, unless it is organized, and intelligently directed, through practical plans of action, to the definite end of accumulation of money.
                    
                    Lack of understanding of this fact has been the source of confusion to millions of people who falsely believe that 'knowledge is power.' It is nothing of the sort! Knowledge is only potential power. It becomes power only when, and if, it is organized into definite plans of action, and directed to a definite end.
                """.trimIndent(),
                keyTakeaways = "1. General knowledge accumulates trivia; specialized knowledge builds leverage.\n2. True mastery lies in knowing how to organize and apply high-value information.\n3. Continual upskilling creates an insurmountable competitive moat.",
                templateTitle = "Specialized Leverage Blueprint",
                templatePrompt = "Map your unique skill stack and competitive edge.",
                templateFieldLabel1 = "Primary Domain of Specialized Mastery",
                templateFieldLabel2 = "Secondary High-Leverage Complementary Skill",
                templateFieldLabel3 = "The Unfair Market Advantage Created By This Combination",
                questTitle = "Deep Study Sprint",
                questDescription = "Commit to 45 minutes of uninterrupted deep learning in your primary specialized edge.",
                questActionPrompt = "Complete 45 minutes of deep specialized study.",
                notebookPrompt = "What single high-value skill, if mastered to the top 1%, would 10x your financial leverage?"
            ),
            ModuleEntity(
                id = 5,
                order = 5,
                title = "The Architect's Mind",
                originalPrinciple = "Imagination",
                subtitle = "The workshop of the mind wherein fashioned are all plans created by man.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 220,
                videoTitle = "Synthetic vs Creative Imagination",
                videoDuration = "14:40",
                excerptTitle = "Constructing Empires in the Ether",
                excerptText = """
                    The imagination is literally the workshop wherein are fashioned all plans created by man. The impulse, the desire, is given shape, form, and action through the aid of the imaginative faculty of the mind.
                    
                    Synthetic imagination arranges old concepts, ideas, or plans into new combinations. Creative imagination is where finite man has direct communication with Infinite Intelligence.
                """.trimIndent(),
                keyTakeaways = "1. Synthetic imagination reorganizes existing concepts; creative imagination channels original breakthroughs.\n2. Exercise the imaginative muscle daily to prevent mental atrophy.\n3. All tangible fortunes were first built in mental blueprint form.",
                templateTitle = "Creative Invention Matrix",
                templatePrompt = "Take 3 disparate concepts in your industry and synthesize a revolutionary solution.",
                templateFieldLabel1 = "Existing Friction Point in Your Market",
                templateFieldLabel2 = "Unorthodox Cross-Industry Solution",
                templateFieldLabel3 = "The Resulting High-Value Offer / Architecture",
                questTitle = "Architect's Ideation Sprint",
                questDescription = "Generate 10 radical value-creation concepts in 10 minutes without judging or filtering.",
                questActionPrompt = "Log and review your 10 rapid ideation concepts.",
                notebookPrompt = "What bold enterprise would you construct if you had unlimited access to resources and talent?"
            ),
            ModuleEntity(
                id = 6,
                order = 6,
                title = "The Blueprint",
                originalPrinciple = "Organized Planning",
                subtitle = "The crystallization of desire into definite action through systematic strategy.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 250,
                videoTitle = "Organized Planning & Flawless Execution",
                videoDuration = "17:15",
                excerptTitle = "The Definite Action Board",
                excerptText = """
                    You have learned that everything man creates or acquires begins in the form of desire. You are now being introduced to the methods by which desire is crystallized into its physical equivalent.
                    
                    No individual has sufficient experience, education, native ability, and knowledge to ensure the accumulation of a great fortune without the cooperation of other people.
                    
                    When defeat comes, accept it as a signal that your plans are not sound; rebuild those plans, and set sail once more toward your coveted goal.
                """.trimIndent(),
                keyTakeaways = "1. An unorganized desire is merely an idle dream.\n2. Temporary defeat is merely feedback to refine the blueprint.\n3. Build modular, resilient execution systems that adapt to friction.",
                templateTitle = "90-Day Sovereign Action Plan",
                templatePrompt = "Architect your structured execution milestones for the next quarter.",
                templateFieldLabel1 = "30-Day Critical Keystone Milestone",
                templateFieldLabel2 = "60-Day Momentum Compounder",
                templateFieldLabel3 = "90-Day Fortress Objective",
                questTitle = "Friction Elimination Audit",
                questDescription = "Review your current schedule and eliminate 2 low-leverage time drains.",
                questActionPrompt = "Remove 2 non-essential tasks from your daily routine.",
                notebookPrompt = "Where in your daily workflow are you mistaking busywork for meaningful high-leverage execution?"
            ),
            ModuleEntity(
                id = 7,
                order = 7,
                title = "The Verdict",
                originalPrinciple = "Decision",
                subtitle = "The mastery of procrastination. Reaching decisions promptly and changing them slowly.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 220,
                videoTitle = "Decisiveness as Sovereign Command",
                videoDuration = "13:30",
                excerptTitle = "Speed of Decision, Slowness of Revision",
                excerptText = """
                    Analysis of several hundred people who had accumulated fortunes well beyond the million-dollar mark disclosed the fact that every one of them had the habit of reaching decisions promptly, and of changing these decisions slowly, if and when they were changed.
                    
                    People who fail to accumulate money, without exception, have the habit of reaching decisions, if at all, very slowly, and of changing these decisions quickly and often.
                """.trimIndent(),
                keyTakeaways = "1. Indecision is the silent killer of compounding momentum.\n2. Make bold decisions promptly; refine them with disciplined patience.\n3. Guard your inner council from the unvetted opinions of bystanders.",
                templateTitle = "The 10-Second Decision Filter",
                templatePrompt = "Structure your personal decision-making heuristics to eliminate hesitation.",
                templateFieldLabel1 = "Pending High-Stakes Decision",
                templateFieldLabel2 = "The 10-Second Gut Verdict (Binary Yes/No)",
                templateFieldLabel3 = "Immediate First Action Step",
                questTitle = "Instant Decision Protocol",
                questDescription = "Resolve one lingering decision you have delayed for over 48 hours within the next 5 minutes.",
                questActionPrompt = "Execute the resolution of your delayed decision.",
                notebookPrompt = "What major decision are you currently postponing, and what is the real hidden cost of delay?"
            ),
            ModuleEntity(
                id = 8,
                order = 8,
                title = "The Long Game",
                originalPrinciple = "Persistence",
                subtitle = "The sustained effort necessary to induce faith and outlast all resistance.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 250,
                videoTitle = "The Compound Mechanics of Persistence",
                videoDuration = "15:50",
                excerptTitle = "The Fortress of Persistence",
                excerptText = """
                    Persistence is an essential factor in the procedure of transmuting the desire for money into its monetary equivalent. The basis of persistence is the power of will.
                    
                    Willpower and desire, when properly combined, make an irresistible pair. Men who accumulate great fortunes are generally known as cold-blooded, and sometimes ruthless. Often they are misunderstood. What they have is willpower, which they blend with persistence.
                """.trimIndent(),
                keyTakeaways = "1. Persistence is to the character of man what carbon is to steel.\n2. Defeat cannot withstand sustained, unrelenting applied willpower.\n3. Cultivate habit loops that make giving up physically unnatural.",
                templateTitle = "The Resistance Response Matrix",
                templatePrompt = "Anticipate failure modes and pre-program your sovereign response.",
                templateFieldLabel1 = "Primary Anticipated Obstacle / Resistance Point",
                templateFieldLabel2 = "Automatic Conditioned Counter-Move",
                templateFieldLabel3 = "Non-Negotiable Commitment Statement",
                questTitle = "Friction Push Challenge",
                questDescription = "Perform one task you have felt strong resistance toward doing today until completed.",
                questActionPrompt = "Overcome the resistance and finish the difficult task.",
                notebookPrompt = "Recall a time when outlasting temporary defeat unlocked an unexpected breakthrough."
            ),
            ModuleEntity(
                id = 9,
                order = 9,
                title = "The Inner Circle",
                originalPrinciple = "Power of the Mastermind",
                subtitle = "The driving force. Coordination of knowledge and effort in a spirit of harmony.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 250,
                videoTitle = "Synthesizing Mastermind Synergy",
                videoDuration = "16:20",
                excerptTitle = "The Third Mind Principle",
                excerptText = """
                    The Mastermind may be defined as: 'Coordination of knowledge and effort, in a spirit of harmony, between two or more people, for the attainment of a definite purpose.'
                    
                    No two minds ever come together without thereby creating a third, invisible, intangible force which may be likened to a third mind.
                    
                    Economic power is created through the harmonious alliance of brains directed toward a definite objective.
                """.trimIndent(),
                keyTakeaways = "1. A mastermind creates an emergent intelligence greater than the sum of its members.\n2. Harmony of spirit is non-negotiable; friction dissipates psychic power.\n3. Surround yourself with minds operating at higher frequencies of achievement.",
                templateTitle = "3-Chair Advisory Council Blueprint",
                templatePrompt = "Structure your ideal Mastermind alliance (real or historical mentors).",
                templateFieldLabel1 = "Mastermind Seat 1: The Strategic Titan",
                templateFieldLabel2 = "Mastermind Seat 2: The Tactical Innovator",
                templateFieldLabel3 = "Mastermind Seat 3: The Sovereign Philosopher",
                questTitle = "Inner Circle Calibration",
                questDescription = "Audit your closest 5 peer influences and identify one high-caliber relationship to cultivate.",
                questActionPrompt = "Reach out or schedule a high-value dialogue with a trusted mentor/ally.",
                notebookPrompt = "What specific value and perspective do you uniquely bring to an elite mastermind table?"
            ),
            ModuleEntity(
                id = 10,
                order = 10,
                title = "Channeled Energy",
                originalPrinciple = "Sex Transmutation",
                subtitle = "The redirection of primary vital drive into creative and financial dominance.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 280,
                videoTitle = "Transmuting Vital Energy into Sovereign Output",
                videoDuration = "18:00",
                excerptTitle = "The High-State Creative Reservoir",
                excerptText = """
                    Transmutation means the changing, or transferring of one element, or form of energy, into another.
                    
                    The desire for sexual expression is by far the strongest and most driving of all human emotions. When harnessed and redirected along other lines, this motivating force maintains all of its attributes of keenness of imagination, courage, etc., which may be used as powerful creative forces in literature, art, or in any other profession or calling, including, of course, the accumulation of riches.
                """.trimIndent(),
                keyTakeaways = "1. Raw creative vitality is the fuel behind all charismatic leadership.\n2. Channel instinctual biological drives into high-focus intellectual creation.\n3. Master emotional control to prevent energetic leakage.",
                templateTitle = "Vital Energy Redirection Protocol",
                templatePrompt = "Define your daily energy stewardship practices.",
                templateFieldLabel1 = "Daily High-State Peak Focus Window (e.g. 6 AM - 9 AM)",
                templateFieldLabel2 = "Primary Creative / Financial Output Channel",
                templateFieldLabel3 = "Energy Conservation Rules (Sleep, Fasting, Digital Detox)",
                questTitle = "90-Minute Sovereign Focus Block",
                questDescription = "Perform a 90-minute hyper-focused creative work block with zero phone/social interruptions.",
                questActionPrompt = "Complete 90 minutes of pure creative transmutation.",
                notebookPrompt = "How does your physical vitality and focus level directly impact the quality of your financial decisions?"
            ),
            ModuleEntity(
                id = 11,
                order = 11,
                title = "The Silent Engine",
                originalPrinciple = "Subconscious Mind",
                subtitle = "The connecting link between finite human intelligence and Infinite Intelligence.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 280,
                videoTitle = "Programming the Subconscious Operating System",
                videoDuration = "15:45",
                excerptTitle = "Planting Seeds in the Fertile Garden",
                excerptText = """
                    The subconscious mind consists of a field of consciousness, in which each impulse of thought that reaches the objective mind through any of the five senses, is classified and recorded, and from which thoughts may be recalled or withdrawn as letters may be taken from a filing cabinet.
                    
                    It works day and night. Through a method of procedure, unknown to man, the subconscious mind draws upon the forces of Infinite Intelligence for the power with which it voluntarily transfigures its desires into their physical equivalent.
                """.trimIndent(),
                keyTakeaways = "1. The subconscious never sleeps; it runs whatever code you feed it.\n2. Guard the gates of your mind against toxic inputs and fearful programming.\n3. Evening programming seeds solutions that surface during morning cognition.",
                templateTitle = "Nocturnal Subconscious Seed Matrix",
                templatePrompt = "Formulate your pre-sleep query to command your subconscious overnight.",
                templateFieldLabel1 = "Current Complex Problem Requiring Breakthrough",
                templateFieldLabel2 = "Pre-Sleep Subconscious Command Formulation",
                templateFieldLabel3 = "Morning Intuitive Download Capture Zone",
                questTitle = "Nocturnal Priming Ritual",
                questDescription = "Review your Definite Purpose for 3 minutes immediately prior to sleeping tonight.",
                questActionPrompt = "Complete your nocturnal purpose priming ritual.",
                notebookPrompt = "What repeated subconscious patterns or synchronicities have you noticed in your life recently?"
            ),
            ModuleEntity(
                id = 12,
                order = 12,
                title = "The Broadcast Station",
                originalPrinciple = "The Brain",
                subtitle = "A broadcasting and receiving station for the vibrations of thought.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 300,
                videoTitle = "Vibrational Resonance & Mental Transmission",
                videoDuration = "16:50",
                excerptTitle = "Tuning into the Sovereign Frequency",
                excerptText = """
                    Every human brain is capable of picking up vibrations of thought which are being released by other brains.
                    
                    When stimulated, or elevated to a high rate of vibration, the mind becomes more receptive to the vibration of thought which reaches it through the ether from outside sources.
                    
                    Operate your broadcast station at the highest frequency of optimism, power, and abundance.
                """.trimIndent(),
                keyTakeaways = "1. The brain acts as both transmitter and receiver of mental vibrations.\n2. Elevate your emotional state to tune into higher-order ideas.\n3. Silence mental noise to receive subtle strategic insights.",
                templateTitle = "Mental Frequency Calibration Sheet",
                templatePrompt = "Audit your mental transmission and eliminate noisy interference.",
                templateFieldLabel1 = "Mental Static / Noise Sources to Eliminate",
                templateFieldLabel2 = "High-Frequency Thought Patterns to Broadcast",
                templateFieldLabel3 = "Daily Resonance Anchor Ritual",
                questTitle = "Digital Silence Hour",
                questDescription = "Spend 60 minutes in complete silence with zero screens, music, or speech.",
                questActionPrompt = "Complete 60 minutes of mental frequency tuning in silence.",
                notebookPrompt = "What intuitive insights or hunches have you dismissed in the past that turned out to be accurate?"
            ),
            ModuleEntity(
                id = 13,
                order = 13,
                title = "The Inner Compass",
                originalPrinciple = "Sixth Sense",
                subtitle = "The apex of the philosophy. The temple of wisdom where Infinite Intelligence talks to man.",
                isUnlocked = false,
                isCompleted = false,
                xpReward = 350,
                videoTitle = "Activating the Sovereign Sixth Sense",
                videoDuration = "20:00",
                excerptTitle = "The Temple of Wisdom",
                excerptText = """
                    The thirteenth principle is known as the Sixth Sense, through which Infinite Intelligence may, and will communicate voluntarily, without any effort from, or demands by, the individual.
                    
                    This is the apex of the philosophy. It can be assimilated, understood, and applied only by first mastering the other twelve principles.
                    
                    The Sixth Sense is that portion of the subconscious mind which has been referred to as the Creative Imagination. By aid of the sixth sense, you will be warned of impending dangers in time to avoid them, and notified of opportunities in time to embrace them.
                """.trimIndent(),
                keyTakeaways = "1. The Sixth Sense is the culmination of all preceding 12 principles.\n2. Flashes of inspiration are direct transmissions from the Infinite.\n3. Live in total congruence with your Sovereign Code.",
                templateTitle = "The Sovereign Apex Synthesis",
                templatePrompt = "Synthesize the entire 13-principle protocol into your life constitution.",
                templateFieldLabel1 = "My Sovereign Life Constitution",
                templateFieldLabel2 = "My Generational Wealth Legacy Blueprint",
                templateFieldLabel3 = "The Daily Inviolable Sovereign Code",
                questTitle = "The Sovereign Ascension Quest",
                questDescription = "Commit your Sovereign Code to memory and review all 13 modules completed.",
                questActionPrompt = "Claim your Sovereign Legacy initiation.",
                notebookPrompt = "Reflecting on your full journey through the 13 vaults: Who have you transformed into?"
            )
        )
    }

    fun getInitialBadges(): List<BadgeEntity> {
        return listOf(
            BadgeEntity(
                id = "badge_consistent_ritualist",
                title = "Consistent Ritualist",
                description = "Completed 7 days of daily entries and rituals to forge an unbreakable mindset.",
                iconKey = "consistent_ritualist",
                isUnlocked = false,
                tierRequired = "Builder",
                progress = 0,
                maxProgress = 7,
                category = "Ritual & Streak",
                xpReward = 200
            ),
            BadgeEntity(
                id = "badge_first_reflection",
                title = "First Reflection",
                description = "Authored your very first entry in the Sovereign Notebook.",
                iconKey = "notebook",
                isUnlocked = false,
                tierRequired = "Novice",
                progress = 0,
                maxProgress = 1,
                category = "Notebook",
                xpReward = 100
            ),
            BadgeEntity(
                id = "badge_prolific_scribe",
                title = "Prolific Scribe",
                description = "Penned 10 or more deep reflections across the 13 Vaults.",
                iconKey = "scribe",
                isUnlocked = false,
                tierRequired = "Architect",
                progress = 0,
                maxProgress = 10,
                category = "Notebook",
                xpReward = 250
            ),
            BadgeEntity(
                id = "badge_assessment",
                title = "Sovereign Diagnosis",
                description = "Completed the Comprehensive Wealth Mindset Assessment.",
                iconKey = "diagnosis",
                isUnlocked = false,
                tierRequired = "Novice",
                progress = 0,
                maxProgress = 1,
                category = "Mindset",
                xpReward = 250
            ),
            BadgeEntity(
                id = "badge_vault_0",
                title = "First Vault Unlocked",
                description = "Completed Module 0 and established your wealth context baseline.",
                iconKey = "vault",
                isUnlocked = false,
                tierRequired = "Novice",
                progress = 0,
                maxProgress = 1,
                category = "Vaults",
                xpReward = 150
            ),
            BadgeEntity(
                id = "badge_streak_3",
                title = "3-Day Discipline Spark",
                description = "Forged a 3-day unbroken chain of daily rituals & mindset transmutation.",
                iconKey = "flame_3",
                isUnlocked = false,
                tierRequired = "Novice",
                progress = 0,
                maxProgress = 3,
                category = "Ritual & Streak",
                xpReward = 150
            ),
            BadgeEntity(
                id = "badge_flame_7",
                title = "7-Day Sovereign Flame",
                description = "Maintained a 7-day uninterrupted learning and journaling ritual streak.",
                iconKey = "streak",
                isUnlocked = false,
                tierRequired = "Builder",
                progress = 0,
                maxProgress = 7,
                category = "Ritual & Streak",
                xpReward = 250
            ),
            BadgeEntity(
                id = "badge_ignition",
                title = "Master of Desire",
                description = "Authored your Definite Major Purpose worksheet in Vault 1.",
                iconKey = "ignition",
                isUnlocked = false,
                tierRequired = "Builder",
                progress = 0,
                maxProgress = 1,
                category = "Vaults",
                xpReward = 200
            ),
            BadgeEntity(
                id = "badge_mastermind",
                title = "Inner Circle Architect",
                description = "Consulted the Master Mind AI Council and designed your advisory seats.",
                iconKey = "mastermind",
                isUnlocked = false,
                tierRequired = "Architect",
                progress = 0,
                maxProgress = 1,
                category = "Council",
                xpReward = 250
            ),
            BadgeEntity(
                id = "badge_transmutation",
                title = "Alchemist of Energy",
                description = "Mastered the redirection of primary vital forces into creative output.",
                iconKey = "transmutation",
                isUnlocked = false,
                tierRequired = "Sovereign",
                progress = 0,
                maxProgress = 1,
                category = "Vaults",
                xpReward = 300
            ),
            BadgeEntity(
                id = "badge_fortress_14",
                title = "14-Day Fortress of Habit",
                description = "Maintained an unbroken 14-day streak of daily discipline and rituals.",
                iconKey = "fortress",
                isUnlocked = false,
                tierRequired = "Architect",
                progress = 0,
                maxProgress = 14,
                category = "Ritual & Streak",
                xpReward = 350
            ),
            BadgeEntity(
                id = "badge_streak_30",
                title = "30-Day Transmutation Ironclad",
                description = "Completed an indomitable 30-day streak of daily rituals into subconscious mastery.",
                iconKey = "flame_30",
                isUnlocked = false,
                tierRequired = "Sovereign",
                progress = 0,
                maxProgress = 30,
                category = "Ritual & Streak",
                xpReward = 500
            ),
            BadgeEntity(
                id = "achievement_section_1",
                title = "Section I: Mental Foundation Master",
                description = "Mastered all modules in Section I (Vaults 0 to 3): Transmutation, Desire, Faith & Autosuggestion.",
                iconKey = "section_1",
                isUnlocked = false,
                tierRequired = "Builder",
                progress = 0,
                maxProgress = 4,
                category = "Section Mastery",
                xpReward = 350
            ),
            BadgeEntity(
                id = "achievement_section_2",
                title = "Section II: Strategic Architecture Master",
                description = "Mastered all modules in Section II (Vaults 4 to 8): Specialized Knowledge, Imagination, Planning, Decision & Persistence.",
                iconKey = "section_2",
                isUnlocked = false,
                tierRequired = "Architect",
                progress = 0,
                maxProgress = 5,
                category = "Section Mastery",
                xpReward = 500
            ),
            BadgeEntity(
                id = "achievement_section_3",
                title = "Section III: Higher Synergies Master",
                description = "Mastered all modules in Section III (Vaults 9 to 11): Master Mind, Sex Transmutation & Subconscious Mind.",
                iconKey = "section_3",
                isUnlocked = false,
                tierRequired = "Sovereign",
                progress = 0,
                maxProgress = 3,
                category = "Section Mastery",
                xpReward = 600
            ),
            BadgeEntity(
                id = "achievement_section_4",
                title = "Section IV: Sovereign Apex Master",
                description = "Mastered all modules in Section IV (Vaults 12 & 13): Brain Vibration Broadcast & The Sixth Sense.",
                iconKey = "section_4",
                isUnlocked = false,
                tierRequired = "Legacy",
                progress = 0,
                maxProgress = 2,
                category = "Section Mastery",
                xpReward = 750
            ),
            BadgeEntity(
                id = "badge_apex_legacy",
                title = "Apex Sovereign Grand Master",
                description = "Completed all 13 Vaults and integrated the Sixth Sense Inner Compass.",
                iconKey = "apex",
                isUnlocked = false,
                tierRequired = "Legacy",
                progress = 0,
                maxProgress = 13,
                category = "Mastery",
                xpReward = 1000
            ),
            BadgeEntity(
                id = "badge_giving_first",
                title = "Generous Heart",
                description = "Logged your first act of giving, activating the Law of Increasing Returns.",
                iconKey = "giving_heart",
                isUnlocked = false,
                tierRequired = "Novice",
                progress = 0,
                maxProgress = 1,
                category = "Giving & Abundance",
                xpReward = 150
            ),
            BadgeEntity(
                id = "badge_giving_5",
                title = "Seed Planter",
                description = "Logged 5 acts of generosity, planting seeds of abundance in the lives of others.",
                iconKey = "giving_seed",
                isUnlocked = false,
                tierRequired = "Builder",
                progress = 0,
                maxProgress = 5,
                category = "Giving & Abundance",
                xpReward = 200
            ),
            BadgeEntity(
                id = "badge_giving_15",
                title = "Abundant Conduit",
                description = "Logged 15 acts of giving, embodying continuous circulation of value and goodwill.",
                iconKey = "giving_stream",
                isUnlocked = false,
                tierRequired = "Architect",
                progress = 0,
                maxProgress = 15,
                category = "Giving & Abundance",
                xpReward = 300
            ),
            BadgeEntity(
                id = "badge_giving_streak_3",
                title = "Sovereign Benefactor",
                description = "Achieved a 3-week giving streak, anchoring generosity into your regular discipline.",
                iconKey = "giving_streak",
                isUnlocked = false,
                tierRequired = "Sovereign",
                progress = 0,
                maxProgress = 3,
                category = "Giving & Abundance",
                xpReward = 250
            ),
            BadgeEntity(
                id = "badge_giving_titan",
                title = "Titan of Benevolence",
                description = "Logged 25 or more acts of giving, mastering Napoleon Hill's principle of giving more than received.",
                iconKey = "giving_titan",
                isUnlocked = false,
                tierRequired = "Legacy",
                progress = 0,
                maxProgress = 25,
                category = "Giving & Abundance",
                xpReward = 500
            )
        )
    }

    fun getInitialDailyHabits(): List<DailyHabitEntity> {
        return listOf(
            DailyHabitEntity(
                id = "habit_visualization",
                title = "Definite Chief Aim Visualization",
                principle = "Desire & Autosuggestion",
                description = "Read your written Definite Major Purpose aloud twice daily (upon waking and before sleeping), visualizing and feeling yourself already in possession of the wealth.",
                category = "Mindset",
                iconKey = "visualization",
                targetMinutes = 10,
                xpReward = 35,
                isPredefined = true,
                orderIndex = 1
            ),
            DailyHabitEntity(
                id = "habit_reading",
                title = "Daily Master Key Philosophy Reading",
                principle = "Specialized Knowledge & Persistence",
                description = "Immerse in at least 15 minutes of foundational wealth literature or specialized skill accumulation to expand your intellectual sovereign capital.",
                category = "Knowledge",
                iconKey = "reading",
                targetMinutes = 15,
                xpReward = 30,
                isPredefined = true,
                orderIndex = 2
            ),
            DailyHabitEntity(
                id = "habit_meditation",
                title = "Subconscious Meditation & Auto-Suggestion",
                principle = "The Subconscious Mind & Faith",
                description = "Sit in uninterrupted stillness, quieting the analytical faculties and directing intense, positive thought vibrations directly to Infinite Intelligence.",
                category = "Spiritual",
                iconKey = "meditation",
                targetMinutes = 12,
                xpReward = 35,
                isPredefined = true,
                orderIndex = 3
            ),
            DailyHabitEntity(
                id = "habit_affirmation",
                title = "Self-Confidence Formula Affirmation",
                principle = "Auto-Suggestion & Belief",
                description = "Repeat the 5-point Self-Confidence decree with total conviction to dismantle fear, doubt, and scarcity thinking from your mental blueprint.",
                category = "Mindset",
                iconKey = "affirmation",
                targetMinutes = 8,
                xpReward = 25,
                isPredefined = true,
                orderIndex = 4
            ),
            DailyHabitEntity(
                id = "habit_transmutation",
                title = "Creative Energy Transmutation Sprint",
                principle = "Sex Transmutation & Action",
                description = "Channel your raw vital physical and mental energy into an unbroken 45-minute deep focus block dedicated to your primary enterprise.",
                category = "Action",
                iconKey = "transmutation",
                targetMinutes = 45,
                xpReward = 50,
                isPredefined = true,
                orderIndex = 5
            ),
            DailyHabitEntity(
                id = "habit_mastermind",
                title = "Master Mind Advisory Alignment",
                principle = "The Master Mind Principle",
                description = "Harmonize with your trusted council, mentors, or AI Master Mind advisors to evaluate strategic moves, review progress, and refine execution.",
                category = "Knowledge",
                iconKey = "mastermind",
                targetMinutes = 15,
                xpReward = 35,
                isPredefined = true,
                orderIndex = 6
            ),
            DailyHabitEntity(
                id = "habit_gratitude",
                title = "Evening Gratitude & Ledger Review",
                principle = "Applied Faith & Self-Discipline",
                description = "Record your daily victories in your notebook, release all negativity, and prime your subconscious mind with clear nocturnal instructions before rest.",
                category = "Spiritual",
                iconKey = "gratitude",
                targetMinutes = 10,
                xpReward = 25,
                isPredefined = true,
                orderIndex = 7
            )
        )
    }

    fun getInitialMastermindGroups(): List<MastermindGroupEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            MastermindGroupEntity(
                id = "grp_architects",
                name = "The Sovereign Architects Circle",
                motto = "Definite Purpose & Relentless Execution",
                inviteCode = "ARCH-9104",
                targetTier = "Architect",
                minLevelOrModule = 3,
                maxMembers = 6,
                groupStreakWeeks = 5,
                combinedXpThisWeek = 2150,
                isUserMember = true,
                createdAtTimestamp = now - 35L * 24 * 3600 * 1000
            ),
            MastermindGroupEntity(
                id = "grp_apex",
                name = "The Apex Syndicate",
                motto = "Transmuting Vision Into Capital & Empire",
                inviteCode = "APEX-7742",
                targetTier = "Sovereign",
                minLevelOrModule = 6,
                maxMembers = 6,
                groupStreakWeeks = 8,
                combinedXpThisWeek = 3450,
                isUserMember = false,
                createdAtTimestamp = now - 60L * 24 * 3600 * 1000
            ),
            MastermindGroupEntity(
                id = "grp_carnegie",
                name = "The Carnegie Alliance",
                motto = "Harmonious Mind Power & Strategic Alliances",
                inviteCode = "CARN-3301",
                targetTier = "Builder",
                minLevelOrModule = 1,
                maxMembers = 6,
                groupStreakWeeks = 3,
                combinedXpThisWeek = 1680,
                isUserMember = false,
                createdAtTimestamp = now - 21L * 24 * 3600 * 1000
            ),
            MastermindGroupEntity(
                id = "grp_irondiscipline",
                name = "Iron Discipline Collective",
                motto = "Relentless Consistency Over Temporary Motivation",
                inviteCode = "IRON-5520",
                targetTier = "Novice",
                minLevelOrModule = 1,
                maxMembers = 6,
                groupStreakWeeks = 2,
                combinedXpThisWeek = 1120,
                isUserMember = false,
                createdAtTimestamp = now - 14L * 24 * 3600 * 1000
            )
        )
    }

    fun getInitialMastermindMembers(): List<MastermindMemberEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            // Sovereign Architects Circle Members
            MastermindMemberEntity(
                id = "mem_user_arch",
                groupId = "grp_architects",
                displayName = "You (Initiate)",
                avatarInitial = "YOU",
                avatarColorHex = "#D4AF37",
                tierTitle = "Builder",
                currentModuleTitle = "Vault 4: Auto-Suggestion",
                weeklyXp = 450,
                isCurrentUser = true,
                joinedTimestamp = now - 28L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_julian",
                groupId = "grp_architects",
                displayName = "Julian Hayes",
                avatarInitial = "JH",
                avatarColorHex = "#E5A93C",
                tierTitle = "Architect",
                currentModuleTitle = "Vault 6: Organized Planning",
                weeklyXp = 520,
                isCurrentUser = false,
                joinedTimestamp = now - 35L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_elena",
                groupId = "grp_architects",
                displayName = "Elena Rostova",
                avatarInitial = "ER",
                avatarColorHex = "#AB47BC",
                tierTitle = "Sovereign",
                currentModuleTitle = "Vault 8: Persistence",
                weeklyXp = 610,
                isCurrentUser = false,
                joinedTimestamp = now - 35L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_marcus",
                groupId = "grp_architects",
                displayName = "Marcus Vance",
                avatarInitial = "MV",
                avatarColorHex = "#2E7D32",
                tierTitle = "Architect",
                currentModuleTitle = "Vault 5: Specialized Knowledge",
                weeklyXp = 390,
                isCurrentUser = false,
                joinedTimestamp = now - 32L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_alistair",
                groupId = "grp_architects",
                displayName = "Dr. Alistair Chen",
                avatarInitial = "AC",
                avatarColorHex = "#1565C0",
                tierTitle = "Builder",
                currentModuleTitle = "Vault 4: Auto-Suggestion",
                weeklyXp = 380,
                isCurrentUser = false,
                joinedTimestamp = now - 25L * 24 * 3600 * 1000
            ),

            // Apex Syndicate Members
            MastermindMemberEntity(
                id = "mem_victoria",
                groupId = "grp_apex",
                displayName = "Victoria Sterling",
                avatarInitial = "VS",
                avatarColorHex = "#FFA000",
                tierTitle = "Sovereign",
                currentModuleTitle = "Vault 11: Subconscious Mind",
                weeklyXp = 950,
                isCurrentUser = false,
                joinedTimestamp = now - 60L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_damian",
                groupId = "grp_apex",
                displayName = "Damian Cross",
                avatarInitial = "DC",
                avatarColorHex = "#00897B",
                tierTitle = "Legacy",
                currentModuleTitle = "Vault 13: Sixth Sense",
                weeklyXp = 1020,
                isCurrentUser = false,
                joinedTimestamp = now - 60L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_seraphina",
                groupId = "grp_apex",
                displayName = "Seraphina Lin",
                avatarInitial = "SL",
                avatarColorHex = "#8E24AA",
                tierTitle = "Sovereign",
                currentModuleTitle = "Vault 10: Transmutation",
                weeklyXp = 780,
                isCurrentUser = false,
                joinedTimestamp = now - 50L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_gabriel",
                groupId = "grp_apex",
                displayName = "Gabriel Thorne",
                avatarInitial = "GT",
                avatarColorHex = "#D81B60",
                tierTitle = "Architect",
                currentModuleTitle = "Vault 9: Master Mind",
                weeklyXp = 700,
                isCurrentUser = false,
                joinedTimestamp = now - 45L * 24 * 3600 * 1000
            ),

            // Carnegie Alliance Members
            MastermindMemberEntity(
                id = "mem_lucas",
                groupId = "grp_carnegie",
                displayName = "Lucas Wright",
                avatarInitial = "LW",
                avatarColorHex = "#F57C00",
                tierTitle = "Builder",
                currentModuleTitle = "Vault 3: Faith",
                weeklyXp = 380,
                isCurrentUser = false,
                joinedTimestamp = now - 21L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_amara",
                groupId = "grp_carnegie",
                displayName = "Amara Okafor",
                avatarInitial = "AO",
                avatarColorHex = "#388E3C",
                tierTitle = "Builder",
                currentModuleTitle = "Vault 4: Auto-Suggestion",
                weeklyXp = 440,
                isCurrentUser = false,
                joinedTimestamp = now - 20L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_nikolai",
                groupId = "grp_carnegie",
                displayName = "Nikolai Berg",
                avatarInitial = "NB",
                avatarColorHex = "#1976D2",
                tierTitle = "Builder",
                currentModuleTitle = "Vault 2: Faith",
                weeklyXp = 320,
                isCurrentUser = false,
                joinedTimestamp = now - 18L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_maya",
                groupId = "grp_carnegie",
                displayName = "Maya Patel",
                avatarInitial = "MP",
                avatarColorHex = "#E64A19",
                tierTitle = "Novice",
                currentModuleTitle = "Vault 1: Desire",
                weeklyXp = 290,
                isCurrentUser = false,
                joinedTimestamp = now - 15L * 24 * 3600 * 1000
            ),

            // Iron Discipline Collective Members
            MastermindMemberEntity(
                id = "mem_kai",
                groupId = "grp_irondiscipline",
                displayName = "Kai Takahashi",
                avatarInitial = "KT",
                avatarColorHex = "#0288D1",
                tierTitle = "Novice",
                currentModuleTitle = "Vault 1: Desire",
                weeklyXp = 380,
                isCurrentUser = false,
                joinedTimestamp = now - 14L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_zara",
                groupId = "grp_irondiscipline",
                displayName = "Zara Novak",
                avatarInitial = "ZN",
                avatarColorHex = "#8E24AA",
                tierTitle = "Novice",
                currentModuleTitle = "Vault 1: Desire",
                weeklyXp = 410,
                isCurrentUser = false,
                joinedTimestamp = now - 12L * 24 * 3600 * 1000
            ),
            MastermindMemberEntity(
                id = "mem_devon",
                groupId = "grp_irondiscipline",
                displayName = "Devon Miller",
                avatarInitial = "DM",
                avatarColorHex = "#43A047",
                tierTitle = "Novice",
                currentModuleTitle = "Vault 0: Wealth Context",
                weeklyXp = 330,
                isCurrentUser = false,
                joinedTimestamp = now - 10L * 24 * 3600 * 1000
            )
        )
    }

    fun getInitialMastermindCheckins(): List<MastermindCheckinEntity> {
        val cal = Calendar.getInstance()
        val currentWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val currentYear = cal.get(Calendar.YEAR)
        val prevWeek = if (currentWeek > 1) currentWeek - 1 else 52
        val prevYear = if (currentWeek > 1) currentYear else currentYear - 1
        val now = System.currentTimeMillis()

        return listOf(
            // Current Week Check-ins for Sovereign Architects Circle
            MastermindCheckinEntity(
                id = "chk_cur_julian",
                groupId = "grp_architects",
                memberId = "mem_julian",
                memberDisplayName = "Julian Hayes",
                memberAvatarInitial = "JH",
                memberAvatarColorHex = "#E5A93C",
                memberTier = "Architect",
                isCurrentUser = false,
                weekNumber = currentWeek,
                year = currentYear,
                goalTitle = "Execute 5 high-leverage client proposals & 7-day affirmation ritual",
                status = "YES",
                note = "Hit 100% of pipeline targets and kept daily affirmations unbroken. Transmuted afternoon distraction into deep client work.",
                timestamp = now - 2L * 24 * 3600 * 1000,
                fireCount = 4,
                clapCount = 3,
                diamondCount = 2,
                userReactedFire = true
            ),
            MastermindCheckinEntity(
                id = "chk_cur_elena",
                groupId = "grp_architects",
                memberId = "mem_elena",
                memberDisplayName = "Elena Rostova",
                memberAvatarInitial = "ER",
                memberAvatarColorHex = "#AB47BC",
                memberTier = "Sovereign",
                isCurrentUser = false,
                weekNumber = currentWeek,
                year = currentYear,
                goalTitle = "Complete Vault 8 worksheet & close $25,000 sovereign contract",
                status = "YES",
                note = "Signed agreement on Thursday! The morning meditation on the Definite Chief Aim anchored total clarity.",
                timestamp = now - 1L * 24 * 3600 * 1000,
                fireCount = 5,
                clapCount = 5,
                diamondCount = 4,
                userReactedFire = true,
                userReactedDiamond = true
            ),
            MastermindCheckinEntity(
                id = "chk_cur_marcus",
                groupId = "grp_architects",
                memberId = "mem_marcus",
                memberDisplayName = "Marcus Vance",
                memberAvatarInitial = "MV",
                memberAvatarColorHex = "#2E7D32",
                memberTier = "Architect",
                isCurrentUser = false,
                weekNumber = currentWeek,
                year = currentYear,
                goalTitle = "Draft strategic capital allocation framework & finish Vault 5",
                status = "PARTIAL",
                note = "Finished 85% of framework and finalized risk model. Will push through the remaining 2 modules over the weekend.",
                timestamp = now - 3L * 24 * 3600 * 1000,
                fireCount = 2,
                clapCount = 4,
                diamondCount = 1,
                userReactedClap = true
            ),
            MastermindCheckinEntity(
                id = "chk_cur_alistair",
                groupId = "grp_architects",
                memberId = "mem_alistair",
                memberDisplayName = "Dr. Alistair Chen",
                memberAvatarInitial = "AC",
                memberAvatarColorHex = "#1565C0",
                memberTier = "Builder",
                isCurrentUser = false,
                weekNumber = currentWeek,
                year = currentYear,
                goalTitle = "Maintain 100% daily habit tracker completion without zero days",
                status = "YES",
                note = "Hit 7 out of 7 daily habit cycles. Mindset score climbed past 75.",
                timestamp = now - 4L * 24 * 3600 * 1000,
                fireCount = 3,
                clapCount = 3,
                diamondCount = 2
            ),

            // Previous Week Historical Check-ins for Sovereign Architects Circle
            MastermindCheckinEntity(
                id = "chk_prev_julian",
                groupId = "grp_architects",
                memberId = "mem_julian",
                memberDisplayName = "Julian Hayes",
                memberAvatarInitial = "JH",
                memberAvatarColorHex = "#E5A93C",
                memberTier = "Architect",
                isCurrentUser = false,
                weekNumber = prevWeek,
                year = prevYear,
                goalTitle = "Inscribe 7 daily notebook reflections & outline master plan",
                status = "YES",
                note = "Full 7 days logged. Clear clarity on Q3 asset strategy.",
                timestamp = now - 9L * 24 * 3600 * 1000,
                fireCount = 4,
                clapCount = 2,
                diamondCount = 3
            ),
            MastermindCheckinEntity(
                id = "chk_prev_elena",
                groupId = "grp_architects",
                memberId = "mem_elena",
                memberDisplayName = "Elena Rostova",
                memberAvatarInitial = "ER",
                memberAvatarColorHex = "#AB47BC",
                memberTier = "Sovereign",
                isCurrentUser = false,
                weekNumber = prevWeek,
                year = prevYear,
                goalTitle = "Launch private client advisory group & complete Vault 7",
                status = "YES",
                note = "Advisory cohort filled in 48 hours. Transmutation principle in full effect.",
                timestamp = now - 8L * 24 * 3600 * 1000,
                fireCount = 6,
                clapCount = 4,
                diamondCount = 5
            ),
            MastermindCheckinEntity(
                id = "chk_prev_marcus",
                groupId = "grp_architects",
                memberId = "mem_marcus",
                memberDisplayName = "Marcus Vance",
                memberAvatarInitial = "MV",
                memberAvatarColorHex = "#2E7D32",
                memberTier = "Architect",
                isCurrentUser = false,
                weekNumber = prevWeek,
                year = prevYear,
                goalTitle = "Finalize auto-suggestion recording ritual & 5 workouts",
                status = "YES",
                note = "Recorded voice audio decree and listened morning and night.",
                timestamp = now - 10L * 24 * 3600 * 1000,
                fireCount = 3,
                clapCount = 3,
                diamondCount = 1
            ),
            MastermindCheckinEntity(
                id = "chk_prev_alistair",
                groupId = "grp_architects",
                memberId = "mem_alistair",
                memberDisplayName = "Dr. Alistair Chen",
                memberAvatarInitial = "AC",
                memberAvatarColorHex = "#1565C0",
                memberTier = "Builder",
                isCurrentUser = false,
                weekNumber = prevWeek,
                year = prevYear,
                goalTitle = "Complete Module 3 worksheet and eliminate evening procrastination",
                status = "PARTIAL",
                note = "Completed the worksheet with deep insights; evening routine had 1 slip.",
                timestamp = now - 11L * 24 * 3600 * 1000,
                fireCount = 2,
                clapCount = 2,
                diamondCount = 1
            )
        )
    }

    fun getInitialVisionBoardItems(): List<VisionBoardItemEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            VisionBoardItemEntity(
                id = 1L,
                title = "\$10,000,000 Liquid Sovereign Reserve",
                category = "Wealth & Abundance",
                imageUrl = "stock_vault_gold",
                targetTimeline = "By Dec 2027",
                affirmation = "Money flows to me in avalanches of abundance as I deliver massive value to the world.",
                orderIndex = 0,
                createdAt = now - 5000L,
                isPinned = true
            ),
            VisionBoardItemEntity(
                id = 2L,
                title = "Private Aviation & Sovereign Freedom",
                category = "Wealth & Abundance",
                imageUrl = "stock_private_aviation",
                targetTimeline = "By 2028",
                affirmation = "I command my time, my destination, and my state of being with complete autonomy.",
                orderIndex = 1,
                createdAt = now - 4000L,
                isPinned = false
            ),
            VisionBoardItemEntity(
                id = 3L,
                title = "Alpine Sanctuary & Mastermind Villa",
                category = "Serene Travel & Retreats",
                imageUrl = "stock_swiss_alps_chalet",
                targetTimeline = "By Q4 2027",
                affirmation = "In serenity and pristine nature, my highest strategic insights crystallize into reality.",
                orderIndex = 2,
                createdAt = now - 3000L,
                isPinned = false
            ),
            VisionBoardItemEntity(
                id = 4L,
                title = "Global Enterprise Headquarters",
                category = "Empire & Career",
                imageUrl = "stock_penthouse_hq",
                targetTimeline = "By 2028",
                affirmation = "My enterprise creates generational wealth and transforms the lives of millions worldwide.",
                orderIndex = 3,
                createdAt = now - 2000L,
                isPinned = false
            ),
            VisionBoardItemEntity(
                id = 5L,
                title = "Peak Bio-Vitality & 100-Year Longevity",
                category = "Health & Vitality",
                imageUrl = "stock_elite_vitality",
                targetTimeline = "Daily Practice",
                affirmation = "My body is a temple of boundless energy, cellular youthfulness, and athletic power.",
                orderIndex = 4,
                createdAt = now - 1000L,
                isPinned = false
            ),
            VisionBoardItemEntity(
                id = 6L,
                title = "Total Mental Sovereignty & Self-Mastery",
                category = "Mind & Mastery",
                imageUrl = "stock_sovereign_crown",
                targetTimeline = "Always Present",
                affirmation = "I am the undisputed master of my fate, the sovereign captain of my soul.",
                orderIndex = 5,
                createdAt = now,
                isPinned = false
            )
        )
    }
}
