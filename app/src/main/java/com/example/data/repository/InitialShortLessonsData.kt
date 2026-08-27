package com.example.data.repository

import com.example.data.model.ShortLessonEntity

object InitialShortLessonsData {

    fun getInitialShortLessons(): List<ShortLessonEntity> {
        return listOf(
            // --- VAULT 0: MONEY / WEALTH CONTEXT ---
            ShortLessonEntity(
                id = "lesson_0_1",
                moduleId = 0,
                order = 1,
                title = "The Consciousness of Capital",
                subtitle = "Transmuting Mental Energy into Physical Counter-Values",
                description = "Understand why money is an intangible energy state before it manifests in bank balances. Master the mindset shift required to attract sovereign capital.",
                durationText = "06:45",
                durationSeconds = 405,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_0_lesson_1",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "Wealth responds only to definiteness of purpose and emotionalized thought patterns, never to passive wishing.",
                keyBulletPoints = """
                    00:00 The Metaphysics of Money & Value
                    01:45 Moving Beyond the Scarcity Paradigm
                    03:30 Emotionalizing the Definite Financial Sum
                    05:15 Evening Inscription Protocol
                """.trimIndent(),
                transcript = """
                    Welcome to the First Vault of Riches. Before gold, land, or currency ever exchange hands in the physical marketplace, wealth exists as a concentrated mental vibration.
                    
                    Napoleon Hill discovered across 500 of the world's greatest fortunes that capital is fundamentally a psychological condition. Those who accumulate enduring sovereign riches do not merely chase money; they condition their consciousness to resonate at the exact frequency of abundance.
                    
                    When you hold a clear, unwavering image of your financial target and blend it with emotional certitude, your subconscious mind begins orchestrating physical actions, attracting resources, and recognizing opportunities that remained invisible during states of doubt.
                    
                    Remember: The universe does not respond to need or vague wishes. It responds with mathematical precision to definiteness of purpose backed by persistent application.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_0_2",
                moduleId = 0,
                order = 2,
                title = "The Definite Financial Blueprint",
                subtitle = "Structuring Concrete Wealth Accumulation Milestones",
                description = "A step-by-step masterclass on breaking your Definite Chief Aim into quantifiable quarterly targets, service pledges, and daily execution rituals.",
                durationText = "08:20",
                durationSeconds = 500,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_0_lesson_2",
                instructorName = "Andrew Carnegie Strategic Legacy",
                keyTakeaway = "An unwritten financial goal is merely a daydream. A written plan with exact dates and service pledges commands reality.",
                keyBulletPoints = """
                    00:00 The Carnegie Formula for Definiteness
                    02:10 Calculating Your Exact Transmutation Date
                    04:45 Defining the Unmatched Service to Deliver
                    07:00 Daily Ledger Affirmation Technique
                """.trimIndent(),
                transcript = """
                    In this video lecture, we deconstruct the exact formula Andrew Carnegie gave to young Napoleon Hill in 1908.
                    
                    To transmute desire into wealth, ambiguity must be banished entirely. You must know the exact dollar sum down to the cent, the exact calendar date of attainment, and most importantly, the precise specialized value or service you will render in reciprocal exchange.
                    
                    There is no such reality in nature as 'something for nothing.' The law of compensation demands that every dollar acquired represents a proportional outflow of creative genius, utility, or problem-solving capability delivered to fellow human beings.
                    
                    Inscribe your Chief Aim upon card stock today. Carry it in your pocket. Review it morning and night until it is permanently etched into the fabric of your subconscious mind.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 1: DESIRE ---
            ShortLessonEntity(
                id = "lesson_1_1",
                moduleId = 1,
                order = 1,
                title = "Igniting the Burning Obsession",
                subtitle = "From Lukewarm Hope to Unstoppable Momentum",
                description = "Discover how titans convert passive ambition into a furnace of burning desire that dissolves obstacles and ignores exhaustion.",
                durationText = "07:15",
                durationSeconds = 435,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_1_lesson_1",
                instructorName = "Napoleon Hill Masterclass Series",
                keyTakeaway = "Desire is the starting point of all achievement; not a hope, not a wish, but a pulsating obsession that supersedes everything else.",
                keyBulletPoints = """
                    00:00 The Six Steps of Transmutation
                    02:30 The Psychology of Burning Bridges
                    04:50 Emotionalizing the End State
                    06:10 Daily Fire Rekindling
                """.trimIndent(),
                transcript = """
                    Desire is the catalyst that transforms thoughts into physical reality. A mere wish has no driving power. It produces no heat, creates no momentum, and breaks under the slightest resistance.
                    
                    When Edwin C. Barnes stepped off the freight train in Orange, New Jersey to partner with Thomas Edison, he had no money, no influence, and no technical credentials. What he possessed was a burning desire so concentrated that he spent five relentless years sweeping floors and doing menial labor, knowing with absolute certainty that his day of partnership would arrive.
                    
                    When desire becomes an obsession, failure ceases to be an option. You must burn all bridges of retreat behind you, leaving only one possible outcome: victory.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_1_2",
                moduleId = 1,
                order = 2,
                title = "Burning the Bridges of Retreat",
                subtitle = "Eliminating the Safety Net to Guarantee Conquest",
                description = "Historical analysis of conquering commanders and modern billionaires who destroyed escape hatches to force unconditional triumph.",
                durationText = "09:10",
                durationSeconds = 550,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_1_lesson_2",
                instructorName = "Sovereign Executive Protocols",
                keyTakeaway = "When retreat is impossible, human ingenuity and mental horsepower expand tenfold.",
                keyBulletPoints = """
                    00:00 The Alexander the Great Command Protocol
                    02:50 The Toxic Trap of Conditional Commitment
                    05:30 Cutting Secondary Distractions
                    07:45 The All-In Declaration Ritual
                """.trimIndent(),
                transcript = """
                    When a great warrior landed his troops on enemy shores, he immediately ordered his men to burn the very ships that carried them. Standing on the beach with smoke rising into the sky, he said: 'You see the boats going up in smoke. That means we cannot leave these shores alive unless we win. We have no choice: we win or we perish!'
                    
                    Most people keep two or three backup plans ready, which subtly signals to their subconscious mind that retreat is acceptable. The moment true hardship arrives, the mind defaults to the escape route.
                    
                    Eliminate your fallback excuses today. Give yourself no path other than the total realization of your Definite Major Purpose.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 2: FAITH ---
            ShortLessonEntity(
                id = "lesson_2_1",
                moduleId = 2,
                order = 1,
                title = "The Head Chemist of the Mind",
                subtitle = "Inducing Voluntary Faith Through Autosuggestion",
                description = "Learn how to systematically synthesize unshakeable faith through repeated mental conditioning, sensory visualization, and emotionalized decree.",
                durationText = "06:50",
                durationSeconds = 410,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_2_lesson_1",
                instructorName = "Napoleon Hill Philosophy Vault",
                keyTakeaway = "Faith is the only known antidote for failure. It is an induced mental state created by repeated affirmation to the subconscious.",
                keyBulletPoints = """
                    00:00 The Alchemy of Mental Vibrations
                    02:15 Why Repetition Reconditions Neural Pathways
                    04:20 Visualizing the Possession of Wealth
                    05:40 Neutralizing Infiltrating Fears
                """.trimIndent(),
                transcript = """
                    Faith is the head chemist of the mind. When faith is blended with thought, the subconscious mind instantly picks up the vibration, translates it into its spiritual equivalent, and transmits it to Infinite Intelligence.
                    
                    Faith is not an emotional accident reserved for the fortunate few. It is a state of mind that can be voluntarily induced and developed through the application of the principle of autosuggestion.
                    
                    By daily reaffirming your ability to achieve your definite aim, and feeling the emotional reality of already possessing that wealth, you train your nervous system to operate from victory rather than anticipation of defeat.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_2_2",
                moduleId = 2,
                order = 2,
                title = "Subconscious Expectancy in Action",
                subtitle = "How Absolute Belief Attracts Tangible Capital",
                description = "A deep dive into the magnetic law of expectation and how titans like Charles M. Schwab built multi-billion dollar industrial empires through projected belief.",
                durationText = "08:40",
                durationSeconds = 520,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_2_lesson_2",
                instructorName = "Industrial Titans Archive",
                keyTakeaway = "Expectancy shapes perception. When you fully expect wealth, your mind perceives high-leverage opportunities invisible to the skeptic.",
                keyBulletPoints = """
                    00:00 Charles M. Schwab's Million Dollar Speech
                    03:00 How Expectancy Transmits Through Human Networks
                    05:40 Overcoming the Fear of Criticism
                    07:15 The Daily Faith Fortification Exercise
                """.trimIndent(),
                transcript = """
                    In 1900, Charles M. Schwab sat at a dinner table of Wall Street financiers and gave an impassioned speech outlining a vision for United States Steel. With zero capital of his own, his absolute faith in the merger compelled J.P. Morgan to fund the first billion-dollar corporation in human history.
                    
                    People are instinctively drawn to individuals who possess unshakeable conviction. Doubt is contagious, but so is supreme faith. When you walk into a room vibrating with certainty, investors, partners, and clients align with your vision.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 3: AUTOSUGGESTION ---
            ShortLessonEntity(
                id = "lesson_3_1",
                moduleId = 3,
                order = 1,
                title = "The Subconscious Gatekeeper",
                subtitle = "Directing Orders into the Inner Vault of Mind",
                description = "Master the five senses gateway to the subconscious mind. Discover how to filter out scarcity media and inject commands of sovereign wealth.",
                durationText = "07:30",
                durationSeconds = 450,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_3_lesson_1",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "The subconscious mind recognizes and acts only upon thoughts that have been well-mixed with emotion or feeling.",
                keyBulletPoints = """
                    00:00 The Five Senses Filter
                    02:20 Plain Words vs. Emotionalized Decrees
                    04:45 Creating the Alpha-State Command Window
                    06:15 The 30-Day Subconscious Reprogramming Protocol
                """.trimIndent(),
                transcript = """
                    Autosuggestion is the agency of control through which an individual may voluntarily feed their subconscious mind on thoughts of a creative nature, or by neglect, permit destructive thoughts to find their way into this rich garden of the mind.
                    
                    Your subconscious mind makes no distinction between constructive and destructive impulses. It works with the material we feed it through our thought-impulses. If you feed it fear and poverty, it produces poverty. If you feed it definiteness and wealth, it produces riches.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_3_2",
                moduleId = 3,
                order = 2,
                title = "Morning & Evening Incantations",
                subtitle = "The Six-Step Spoken Ritual for Daily Transmutation",
                description = "An interactive audio practice guide for performing the morning and bedtime Napoleon Hill wealth recitation with peak physiological conviction.",
                durationText = "05:45",
                durationSeconds = 345,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_3_lesson_2",
                instructorName = "Sovereign Mind Protocol",
                keyTakeaway = "Repetition of spoken decrees right before sleep and immediately upon waking embeds commands when subconscious receptivity is at its peak.",
                keyBulletPoints = """
                    00:00 The Hypnagogic Window Explained
                    01:45 Posture, Breathing & Vocal Tone
                    03:15 The Six-Step Recitation Guided Practice
                    04:50 Sealing the Subconscious Impression
                """.trimIndent(),
                transcript = """
                    Close your eyes. Breathe deeply from the diaphragm. As you prepare to sleep, the critical faculty of the conscious mind relaxes, opening a direct highway to your subconscious reservoir.
                    
                    Repeat your Definite Financial Sum aloud. See the money in your hands. Feel the texture of the assets. Envision the service you are providing. Seal this impression before you drift off to sleep, and your subconscious will work on practical realization strategies throughout the night.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 4: SPECIALIZED KNOWLEDGE ---
            ShortLessonEntity(
                id = "lesson_4_1",
                moduleId = 4,
                order = 1,
                title = "General vs Specialized Knowledge",
                subtitle = "Turning Raw Information into Market Leverage",
                description = "Why encyclopedic general knowledge rarely creates wealth, while organized, specialized knowledge directed toward a definite plan creates empires.",
                durationText = "08:30",
                durationSeconds = 510,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_4_lesson_1",
                instructorName = "Henry Ford Industrial Case Study",
                keyTakeaway = "Knowledge is only potential power. It becomes actual power only when organized into definite plans of action and directed to a definite end.",
                keyBulletPoints = """
                    00:00 Henry Ford's Famous Libel Trial & The Electric Buttons
                    03:15 The Difference Between a Savant and a Master Capitalist
                    05:40 Identifying High-Leverage Specialized Skills
                    07:10 Building an Expert Knowledge Network
                """.trimIndent(),
                transcript = """
                    During the First World War, a Chicago newspaper called Henry Ford an 'ignorant pacifist.' Ford sued the paper for libel. In court, the defense attorneys questioned him on historical facts and general trivia.
                    
                    Ford replied: 'If I should really want to answer the foolish question you have just asked, let me remind you that I have a row of electric push-buttons on my desk. By pushing the right button, I can summon men who can answer any question I desire. Now will you kindly tell me why I should clutter up my mind with general information when I have men around me who can supply any knowledge I need?'
                    
                    A master executive does not need to know everything. They must know how to organize and direct specialized knowledge toward practical accumulation.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_4_2",
                moduleId = 4,
                order = 2,
                title = "The Master Capitalist's Library",
                subtitle = "Curating and Synthesizing High-Value Intellect",
                description = "How to build a personal knowledge operating system that tracks market shifts, proprietary insights, and asymmetric opportunities.",
                durationText = "06:15",
                durationSeconds = 375,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_4_lesson_2",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "Specialized knowledge must be continually updated, organized into active playbooks, and applied with relentless speed.",
                keyBulletPoints = """
                    00:00 The Continuous Learning Imperative
                    01:50 Structuring Your Proprietary Skill Stack
                    03:45 Synthesizing Cross-Discipline Insights
                    05:00 The Weekly Knowledge Monetization Audit
                """.trimIndent(),
                transcript = """
                    The person who stops studying merely because they have finished school is forever doomed to mediocrity. The way of success is the continuous pursuit of specialized knowledge.
                    
                    Identify the two or three proprietary domains that sit at the intersection of your natural strengths and market demand. Master them deeper than 99% of your peers, and organize that knowledge into scalable systems.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 5: IMAGINATION ---
            ShortLessonEntity(
                id = "lesson_5_1",
                moduleId = 5,
                order = 1,
                title = "The Workshop of the Mind",
                subtitle = "Synthetic Imagination vs Creative Imagination",
                description = "Uncover the two faculties of imagination: synthesizing existing ideas into new combinations, and tuning into Infinite Intelligence for groundbreaking flashes.",
                durationText = "09:20",
                durationSeconds = 560,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_5_lesson_2",
                instructorName = "Napoleon Hill Philosophy Vault",
                keyTakeaway = "Imagination is the workshop wherein are fashioned all plans created by man. The impulse of desire is given shape and action through imagination.",
                keyBulletPoints = """
                    00:00 The Secret Behind the Coca-Cola Formula Purchase
                    03:00 Synthetic vs Creative Imagination Defined
                    05:30 Exercises to Reawaken Creative Dormancy
                    07:45 Transmuting Raw Ideas into Commercial Enterprises
                """.trimIndent(),
                transcript = """
                    An old country doctor drove to town, tied his horse, and walked quietly into a drugstore. He took out an old kettle and a wooden paddle, along with a secret formula written on a piece of paper, and sold it to young clerk Asa Candler for five hundred dollars.
                    
                    What Candler actually bought was not an old kettle. It was an idea! He added the magic ingredient of imagination, organized distribution, and transformed that kettle of syrup into the worldwide Coca-Cola empire.
                    
                    Ideas are intangible forces, but they have more power than the physical brains from which they spring. Awaken the workshop of your mind today.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_5_2",
                moduleId = 5,
                order = 2,
                title = "The Idea Incubator Protocol",
                subtitle = "From Fleeting Inspiration to Concrete Cash Flow",
                description = "A rapid-prototyping mental framework to capture, stress-test, and commercialize high-potential business ideas without analysis paralysis.",
                durationText = "07:05",
                durationSeconds = 425,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_5_lesson_2",
                instructorName = "Sovereign Innovation Studio",
                keyTakeaway = "An idea is a delicate infant when first conceived. It must be nursed, protected with secrecy, and quickly translated into tangible execution.",
                keyBulletPoints = """
                    00:00 Why Most Great Ideas Die in 24 Hours
                    02:15 The 3-Column Idea Feasibility Filter
                    04:20 Protecting Your Brainchild from Negative Influences
                    05:50 The 72-Hour Rapid Execution Sprint
                """.trimIndent(),
                transcript = """
                    Ideas are born with great promise, but if they are not immediately wedded to definite organized plans, they wither away. The moment inspiration strikes, write it down, analyze its core mechanism, and take at least one physical action within 24 hours to give it physical form.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 6: ORGANIZED PLANNING ---
            ShortLessonEntity(
                id = "lesson_6_1",
                moduleId = 6,
                order = 1,
                title = "The Faultless Battleplan",
                subtitle = "Structuring Practical Transmutation Systems",
                description = "Learn how to build resilient, multi-tiered plans that adapt when initial strategies fail, ensuring your Definite Major Purpose remains unstoppable.",
                durationText = "09:45",
                durationSeconds = 585,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_6_lesson_1",
                instructorName = "Napoleon Hill Masterclass Series",
                keyTakeaway = "No individual has sufficient experience, education, native ability, and knowledge to ensure the accumulation of a great fortune without cooperation with others.",
                keyBulletPoints = """
                    00:00 The 4 Golden Rules of Alliance & Planning
                    03:15 Why Temporary Defeat is Not Permanent Failure
                    06:00 Daily & Weekly Review Systems
                    08:10 The Sovereign Execution Matrix
                """.trimIndent(),
                transcript = """
                    You are engaged in an undertaking of major importance to you. To be sure of success, you must have plans which are faultless.
                    
                    Ally yourself with a group of as many people as you may need for the creation and carrying out of your plan. Meet with them regularly. If the first plan you adopt does not succeed successfully, replace it with a new plan. If this new plan fails to work, replace it again until you find a plan that works.
                    
                    This is the point where the majority of men meet with failure, because of their lack of persistence in creating new plans to take the place of those that fail.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_6_2",
                moduleId = 6,
                order = 2,
                title = "Defeat as Temporary Rerouting",
                subtitle = "The Resilient Architect's Mindset",
                description = "How Thomas Edison reframed 10,000 failed filament trials as the necessary data points that unlocked the electric incandescent lamp.",
                durationText = "06:30",
                durationSeconds = 390,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_6_lesson_2",
                instructorName = "Edison Research Laboratories Archive",
                keyTakeaway = "Temporary defeat should mean only one thing: the certain knowledge that there is something wrong with your plan.",
                keyBulletPoints = """
                    00:00 Edison's 10,000 Discoveries
                    02:00 The Emotional Separation of Ego from Tactics
                    03:45 Rebuilding the Operational Playbook in 24 Hours
                    05:15 Forging Unbreakable Strategic Flexibility
                """.trimIndent(),
                transcript = """
                    When Thomas Edison was asked by a reporter how it felt to fail 10,000 times before perfecting the light bulb, he calmly responded: 'I have not failed 10,000 times. I have successfully discovered 10,000 ways that will not work.'
                    
                    Defeat is merely a notification that your current plan contains an error. Reconstruct the plan, adjust the sails, and advance once more.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 7: DECISION ---
            ShortLessonEntity(
                id = "lesson_7_1",
                moduleId = 7,
                order = 1,
                title = "Instant Decision, Slow Reversal",
                subtitle = "The Sovereign Executive Habit of Titans",
                description = "Analysis of over 25,000 men and women who experienced failure: lack of decision (procrastination) was near the head of the list.",
                durationText = "07:50",
                durationSeconds = 470,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_7_lesson_1",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "Men who succeed reach decisions promptly and change them very slowly, if and when any change is made.",
                keyBulletPoints = """
                    00:00 The Universal Trait of Billionaire Decision Makers
                    02:30 Guarding Your Mind Against Unsolicited Opinions
                    04:50 The 60-Second Sovereign Executive Rule
                    06:20 Standing Firm in the Face of Criticism
                """.trimIndent(),
                transcript = """
                    Analysis of several hundred people who had accumulated fortunes well beyond the million-dollar mark disclosed the fact that every one of them had the habit of reaching decisions promptly and of changing these decisions slowly, if and when they were changed.
                    
                    People who fail to accumulate money, without exception, have the habit of reaching decisions, if at all, very slowly, and of changing these decisions quickly and often.
                    
                    Close your ears to opinions that do not originate from your trusted Master Mind counsel. Stand resolute in your purpose.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_7_2",
                moduleId = 7,
                order = 2,
                title = "The Lethal Cost of Procrastination",
                subtitle = "Eradicating Doubt & Outside Opinion Poisoning",
                description = "How 56 brave men signed the Declaration of Independence knowing full well they were committing their lives, fortunes, and sacred honor to a single irrevocable choice.",
                durationText = "08:15",
                durationSeconds = 495,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_7_lesson_2",
                instructorName = "Historical Statesmanship Archives",
                keyTakeaway = "A decision backed by supreme courage has the power to birth a nation or forge a financial empire in a single moment.",
                keyBulletPoints = """
                    00:00 July 4, 1776: The Supreme Decision That Birthed a Superpower
                    03:15 How Indecision Paralyzes Creative Momentum
                    05:30 Developing the Courage Muscle
                    07:00 The Irrevocable Commitment Signature
                """.trimIndent(),
                transcript = """
                    When John Hancock stepped forward and signed his name in large, bold letters on the Declaration of Independence, he knew that if the revolution failed, every signer would hang from the gallows for high treason.
                    
                    Yet they did not hesitate. Their decision was absolute, instantaneous, and permanent.
                    
                    Every great breakthrough in your business and personal wealth will demand a moment of total courage where you cross the threshold with zero turning back.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 8: PERSISTENCE ---
            ShortLessonEntity(
                id = "lesson_8_1",
                moduleId = 8,
                order = 1,
                title = "The Sustained Effort Equation",
                subtitle = "Forging Unbreakable Willpower in the Furnace of Time",
                description = "Persistence is to the character of man what carbon is to steel. Discover how to cultivate relentless staying power through mental conditioning.",
                durationText = "08:20",
                durationSeconds = 500,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_8_lesson_1",
                instructorName = "Napoleon Hill Philosophy Vault",
                keyTakeaway = "There is no substitute for persistence. It cannot be supplanted by any other quality. With persistence, comes success.",
                keyBulletPoints = """
                    00:00 The Eight Essential Factors of Persistence
                    03:00 The Story of Three Feet from Gold
                    05:30 Breaking the Wall of Mental Fatigue
                    07:00 Building an Unbreakable Daily Habit
                """.trimIndent(),
                transcript = """
                    One of the most common causes of failure is the habit of quitting when one is overtaken by temporary defeat.
                    
                    R.U. Darby's uncle had gold fever and staked a claim in Colorado. He mined rich ore, but suddenly the vein disappeared. After drilling desperately, they quit and sold the machinery to a junk man for a few hundred dollars. The junk man called in a mining engineer, who calculated that the vein was just three feet from where the Darbys had stopped drilling! The junk man took millions in gold from the mine.
                    
                    Most people quit three feet from gold. Never stop when defeat taps your shoulder; press forward one more stride.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_8_2",
                moduleId = 8,
                order = 2,
                title = "The 4-Step Habit of Persistence",
                subtitle = "The Formula to Outlast Any Economic Storm",
                description = "A practical 4-step framework requiring zero high intelligence or special schooling, capable of carrying you through any setback.",
                durationText = "10:00",
                durationSeconds = 600,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_8_lesson_2",
                instructorName = "Sovereign Executive Protocols",
                keyTakeaway = "1. Definite purpose. 2. Definite plan. 3. Mind closed against negativity. 4. Friendly alliance with one or more persons.",
                keyBulletPoints = """
                    00:00 The 4 Steps Deconstructed
                    03:00 Definite Purpose Backed by Burning Desire
                    05:15 Definite Plan Expressed in Continuous Action
                    07:30 Mental Firewall Against Discouraging Relatives
                    09:00 The Master Mind Alliance as an Anchor
                """.trimIndent(),
                transcript = """
                    There are four simple steps which lead to the habit of persistence. They call for no great amount of intelligence, no particular amount of education, and but little time or effort.
                    
                    Step one: A definite purpose backed by burning desire for its fulfillment.
                    Step two: A definite plan, expressed in continuous action.
                    Step three: A mind closed tightly against all negative and discouraging influences, including negative suggestions of relatives, friends, and acquaintances.
                    Step four: A friendly alliance with one or more persons who will encourage one to follow through with both plan and purpose.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 9: POWER OF THE MASTER MIND ---
            ShortLessonEntity(
                id = "lesson_9_1",
                moduleId = 9,
                order = 1,
                title = "The Third Mind Principle",
                subtitle = "Blending Synergistic Brainpower for Exponential Leverage",
                description = "When two or more minds coordinate in a spirit of harmony toward a definite purpose, a third, master mind is born with multiplied power.",
                durationText = "09:15",
                durationSeconds = 555,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_9_lesson_1",
                instructorName = "Andrew Carnegie Industrial Archive",
                keyTakeaway = "No two minds ever come together without thereby creating a third, invisible, intangible force which may be likened to a third mind.",
                keyBulletPoints = """
                    00:00 Andrew Carnegie's 50-Person Steel Master Mind
                    03:30 The Physics of Coordinated Mental Energy
                    06:00 The Requirement of Absolute Harmony
                    08:00 Weekly Mastermind Cadence & Agenda
                """.trimIndent(),
                transcript = """
                    Andrew Carnegie attributed his entire personal fortune to the Master Mind principle. He had around him a staff of approximately fifty men, with whom he allied himself for the definite purpose of manufacturing and marketing steel.
                    
                    He admitted that he personally knew very little about the technical manufacture of steel, but his ability to manage and harmonize this group made him the wealthiest industrialist on Earth.
                    
                    When you align with like-minded sovereigns who possess complementary strengths, your collective intellectual horsepower multiplies exponentially.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_9_2",
                moduleId = 9,
                order = 2,
                title = "Structuring Your Sovereign Circle",
                subtitle = "Selection Criteria and Harmony Protocols",
                description = "How to curate, vet, and conduct mastermind meetings with peak efficiency, eliminating ego conflicts and maximizing strategic breakthroughs.",
                durationText = "07:40",
                durationSeconds = 460,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_9_lesson_2",
                instructorName = "Sovereign Mastermind Council",
                keyTakeaway = "Harmony is the non-negotiable prerequisite. A single discordant mind destroys the entire mastermind battery.",
                keyBulletPoints = """
                    00:00 The 3 Selection Rules: Integrity, Drive, Complementary Skill
                    02:30 Structuring the Hot Seat & Accountability Review
                    04:50 Confidentiality & Reciprocal Benefit Contracts
                    06:20 Seeding Sovereign Growth
                """.trimIndent(),
                transcript = """
                    A mastermind circle must never be formed casually with casual acquaintances. Select only individuals whose integrity is beyond question, who possess a burning desire in their own right, and who bring skills complementary to your own.
                    
                    Guard the harmony of the circle with utmost vigilance. At the first sign of jealousy, deceit, or negativity, remove the offending member immediately to preserve the power of the alliance.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 10: TRANSMUTATION OF SEX ---
            ShortLessonEntity(
                id = "lesson_10_1",
                moduleId = 10,
                order = 1,
                title = "The Highest Creative Dynamo",
                subtitle = "Directing Primal Drive into Sovereign Empire",
                description = "Sex desire is the most powerful of human desires. Transmuted, this motivating force can lift ordinary men to the high status of genius.",
                durationText = "08:45",
                durationSeconds = 525,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_10_lesson_1",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "Transmutation means the changing or transferring of one element or form of energy into another. The emotion of sex contains the secret of creative ability.",
                keyBulletPoints = """
                    00:00 The Three Constructive Potentialities of Sex Emotion
                    03:00 The Correlation Between Sexual Vitality and Great Wealth
                    05:30 Channeling Primal Drive into Relentless Work Ethic
                    07:15 The Magnetism of Transmuted Energy
                """.trimIndent(),
                transcript = """
                    The emotion of sex has back of it the possibility of three constructive potentialities: the perpetuation of mankind, the maintenance of health, and the transformation of mediocrity into genius through transmutation.
                    
                    When driven by this desire, men develop keenness of imagination, courage, will-power, persistence, and of creative ability unknown to them at other times.
                    
                    The person who channels this powerful physical force into creative, financial, and strategic enterprise operates with a magnetic intensity that dominates their industry.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_10_2",
                moduleId = 10,
                order = 2,
                title = "The Genius Frequency",
                subtitle = "Stimulating the Creative Subconscious Reservoir",
                description = "How history's greatest artists, inventors, and statesmen harnessed the tenth principle of riches to reach mental states beyond ordinary human limitation.",
                durationText = "07:30",
                durationSeconds = 450,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_10_lesson_2",
                instructorName = "Creative Titans Masterclass",
                keyTakeaway = "A genius is one who has discovered how to step up the vibrations of thought until he can freely communicate with sources of knowledge not available through ordinary sensory perception.",
                keyBulletPoints = """
                    00:00 The 10 Mind Stimulants Ranked
                    02:45 Why Most Men Achieve Real Wealth Only After Age 40
                    05:15 Cultivating Personal Charisma and Presence
                    06:40 The Daily Creative Focus Routine
                """.trimIndent(),
                transcript = """
                    A genius is a man who has learned how to freely access the Sixth Sense through the proper stimulation of thought.
                    
                    Studies of thousands of successful executives demonstrate that the greatest fortunes and most enduring legacies are typically built between the ages of 40 and 60, after the individual has learned to transmute primal drives into structured, disciplined, long-term wealth creation.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 11: THE SUBCONSCIOUS MIND ---
            ShortLessonEntity(
                id = "lesson_11_1",
                moduleId = 11,
                order = 1,
                title = "The Connecting Link",
                subtitle = "Broadcasting Desires into Infinite Intelligence",
                description = "The subconscious mind works night and day. Discover how it acts as the intermediate transmitting station between finite human thought and Infinite Intelligence.",
                durationText = "07:10",
                durationSeconds = 430,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_11_lesson_1",
                instructorName = "Napoleon Hill Philosophy Vault",
                keyTakeaway = "You cannot entirely control your subconscious mind, but you can voluntarily hand over to it any plan, desire, or purpose you wish transformed into concrete reality.",
                keyBulletPoints = """
                    00:00 The Subconscious as the Infinite Transceiver
                    02:30 The 7 Major Positive Emotions
                    04:45 The 7 Major Negative Emotions to Banish
                    06:00 Handing Over Definite Commands
                """.trimIndent(),
                transcript = """
                    The subconscious mind is the connecting link between the finite mind of man and Infinite Intelligence. It is the intermediary through which one may draw upon the forces of the universe at will.
                    
                    It responds only to the language of emotion. Intellectual statements that lack deep feeling bounce off the surface. When you combine your Definite Major Purpose with enthusiasm, faith, and burning desire, the subconscious accepts the decree as immediate reality.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_11_2",
                moduleId = 11,
                order = 2,
                title = "The Emotional Citadel",
                subtitle = "Guarding the Portals Against Scarcity Vibrations",
                description = "A deep psychological training module on eliminating fear, jealousy, hatred, and revenge to keep your subconscious channel open to unlimited abundance.",
                durationText = "08:50",
                durationSeconds = 530,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_11_lesson_2",
                instructorName = "Sovereign Mind Protocol",
                keyTakeaway = "Positive and negative emotions cannot occupy the mind at the same time. One or the other must dominate. It is your responsibility to make sure positive emotions constitute the dominating influence.",
                keyBulletPoints = """
                    00:00 The Battle of Polar Emotions
                    02:50 The 7 Major Positives: Desire, Faith, Love, Sex, Enthusiasm, Romance, Hope
                    05:30 Building an Automatic Emotional Reset Trigger
                    07:15 The Evening Cleanse Meditation
                """.trimIndent(),
                transcript = """
                    Your mind is a fertile garden. If you do not deliberately plant seeds of purpose, faith, and prosperity, weeds of doubt, fear, and resentment will grow spontaneously.
                    
                    Make it your supreme daily duty to occupy your consciousness exclusively with positive emotions. The moment fear or frustration attempts to enter, immediately re-anchor your attention on your Definite Major Purpose.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 12: THE BRAIN ---
            ShortLessonEntity(
                id = "lesson_12_1",
                moduleId = 12,
                order = 1,
                title = "The Broadcasting & Receiving Station",
                subtitle = "Tuning Your Mental Apparatus to High-Frequency Thought",
                description = "Every human brain is capable of picking up vibrations of thought which are being released by other brains through the ether.",
                durationText = "06:55",
                durationSeconds = 415,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_12_lesson_1",
                instructorName = "Napoleon Hill Wisdom Archives",
                keyTakeaway = "The brain is both a broadcasting and receiving station for the vibration of thought. When stimulated to high vibration, it attracts corresponding creative thoughts.",
                keyBulletPoints = """
                    00:00 The Three Principles of the Brain Network
                    02:15 The Subconscious as Sending Apparatus
                    04:00 Creative Imagination as the Receiving Set
                    05:30 Stepping Up Your Operating Frequency
                """.trimIndent(),
                transcript = """
                    More than fifty years ago, the great scientist Alexander Graham Bell affirmed that the human brain operates identically to wireless radio broadcasting stations.
                    
                    When your mental apparatus is stepped up through emotion, enthusiasm, and concentrated desire, your brain broadcasts thoughts of wealth into the environment, drawing in collaborators, ideas, and solutions that vibrate at the identical frequency.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_12_2",
                moduleId = 12,
                order = 2,
                title = "The Mental Resonator Protocol",
                subtitle = "Practical Techniques to Amplify Intellectual Output",
                description = "How to organize your physical environment, morning rituals, and deep work blocks to maintain peak alpha and theta mental frequencies throughout the day.",
                durationText = "08:20",
                durationSeconds = 500,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_12_lesson_2",
                instructorName = "Cognitive Optimization Archives",
                keyTakeaway = "Peak intellectual breakthroughs occur when distraction is eliminated and the mental faculties operate without friction.",
                keyBulletPoints = """
                    00:00 The Cost of Mental Scatter & Task Switching
                    02:40 Engineering the Frictionless Sanctuary
                    05:10 Binaural Frequency & Subconscious Resonance
                    07:00 The 90-Minute Sovereign Focus Block
                """.trimIndent(),
                transcript = """
                    Protect your mental bandwidth as your most valuable sovereign asset. When you eliminate digital chatter and sit in focused contemplation of a single problem, your brain begins operating at its peak resonance, unlocking insights that normal scatter will never discover.
                """.trimIndent(),
                xpReward = 35
            ),

            // --- VAULT 13: THE SIXTH SENSE ---
            ShortLessonEntity(
                id = "lesson_13_1",
                moduleId = 13,
                order = 1,
                title = "The Apex of Philosophy",
                subtitle = "Accessing the Invisible Counselors and Infinite Intelligence",
                description = "The thirteenth principle is known as the Sixth Sense, through which Infinite Intelligence may and will communicate voluntarily, without any effort from or demands by the individual.",
                durationText = "09:00",
                durationSeconds = 540,
                mediaType = "AUDIO",
                mediaUrl = "audio_vault_13_lesson_1",
                instructorName = "Napoleon Hill Philosophy Vault",
                keyTakeaway = "The Sixth Sense is the apex of the philosophy. It can be assimilated, understood, and applied only by first mastering the other twelve principles.",
                keyBulletPoints = """
                    00:00 The Mystery of the Sixth Sense
                    02:45 Napoleon Hill's Fictional Invisible Counselors Technique
                    05:30 Receiving Flashes of Intuition & Premonition
                    07:30 Transcending the Fear of Poverty and Death
                """.trimIndent(),
                transcript = """
                    The Sixth Sense is that portion of the subconscious mind which has been referred to as the Creative Imagination. Through it, 'hunches,' inspirations, and flashes of genius are received.
                    
                    Every night before sleep, Napoleon Hill held an imaginary council table with his personal heroes: Emerson, Paine, Edison, Darwin, Lincoln, and Carnegie. He consulted with them on difficult decisions and found that his subconscious synthesized profound wisdom through their archetypes.
                    
                    When you reach this pinnacle of self-mastery, fear vanishes, intuition sharpens, and you walk as a true master of your fate.
                """.trimIndent(),
                xpReward = 35
            ),
            ShortLessonEntity(
                id = "lesson_13_2",
                moduleId = 13,
                order = 2,
                title = "Transcendent Intuition & Sovereignty",
                subtitle = "Living in Total Alignment with Infinite Riches",
                description = "The final graduation masterclass: integrating all thirteen principles into an effortless daily state of sovereign abundance, peace of mind, and financial empire.",
                durationText = "09:30",
                durationSeconds = 570,
                mediaType = "VIDEO",
                mediaUrl = "video_vault_13_lesson_2",
                instructorName = "The Grand Philosophy Finale",
                keyTakeaway = "The greatest riches in life are peace of mind, physical health, enduring friendships, and the freedom to chart your own sovereign destiny.",
                keyBulletPoints = """
                    00:00 The True Definition of Sovereign Wealth
                    03:00 The Synthesis of all 13 Principles
                    06:00 Guarding Against the Six Ghosts of Fear
                    08:15 The Sovereign Invictus Decree
                """.trimIndent(),
                transcript = """
                    You have now journeyed through all Thirteen Vaults of Think and Grow Rich. Riches do not begin with money, and they do not end with bank balances.
                    
                    True wealth is complete mental sovereignty, profound health, unyielding faith, loyal comrades in purpose, and the sublime freedom to shape physical reality according to the blueprint of your mind.
                    
                    Stand tall. You are the undisputed master of your fate. You are the captain of your soul. Step into your sovereignty today.
                """.trimIndent(),
                xpReward = 35
            )
        )
    }
}
