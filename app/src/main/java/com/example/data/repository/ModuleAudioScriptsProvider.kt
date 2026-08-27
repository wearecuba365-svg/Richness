package com.example.data.repository

import com.example.data.model.AudioScriptType
import com.example.data.model.ModuleAudioScript

object ModuleAudioScriptsProvider {

    fun getScriptsForModule(moduleId: Int): List<ModuleAudioScript> {
        val all = getAllScripts()
        return all.filter { it.moduleId == moduleId }
    }

    fun getDefaultScriptForModule(moduleId: Int): ModuleAudioScript {
        return getScriptsForModule(moduleId).firstOrNull() ?: getAllScripts().first()
    }

    fun getAllScripts(): List<ModuleAudioScript> {
        return listOf(
            // --- VAULT 0: Money / Wealth Context ---
            ModuleAudioScript(
                id = "vault_0_affirmation",
                moduleId = 0,
                moduleTitle = "The First Vault",
                principleName = "Wealth Context",
                title = "The Sovereign Wealth Context Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Establish mental clarity and transmute passive hoping into definite financial conviction.",
                textToSpeak = """
                    I recognize that wealth is first an internal condition of the mind before it manifests in physical reality. 
                    I eliminate all poverty consciousness, doubt, and mental scatter. 
                    My purpose is crystal clear, my conviction is unshakable, and I demand excellence of myself daily. 
                    I attract abundant financial counter-values by delivering supreme service and unconditional value to the world.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),
            ModuleAudioScript(
                id = "vault_0_meditation",
                moduleId = 0,
                moduleTitle = "The First Vault",
                principleName = "Wealth Context",
                title = "The Metaphysics of Value Creation Meditation",
                type = AudioScriptType.MEDITATION,
                description = "A deep grounding meditation to align your consciousness with the universal law of exchange.",
                textToSpeak = """
                    Close your eyes and breathe deeply. 
                    Release all tension in your body and surrender every lingering fear of scarcity. 
                    Visualize your mind as a vast, fertile field of pure potential. 
                    Every thought you hold with emotion is a seed planted into this infinite soil. 
                    Feel the profound truth that money is simply energy in circulation. 
                    As you elevate the quality of your thoughts, you align with the flow of limitless prosperity. 
                    Rest in this sovereign awareness now.
                """.trimIndent(),
                estimatedDurationSeconds = 75
            ),

            // --- VAULT 1: Desire ---
            ModuleAudioScript(
                id = "vault_1_affirmation",
                moduleId = 1,
                moduleTitle = "The Ignition",
                principleName = "Desire",
                title = "The Definite Chief Aim Decree",
                type = AudioScriptType.AFFIRMATION,
                description = "The core 6-step transmutation formula to fix your burning obsession into reality.",
                textToSpeak = """
                    I have fixed in my mind the exact amount of financial independence I will attain. 
                    I know exactly what supreme value and relentless service I will render in return. 
                    I have established a definite date of attainment and I hold a definite plan. 
                    I read my written statement aloud twice daily with burning conviction, faith, and gratitude. 
                    I can already see and feel the wealth in my possession.
                """.trimIndent(),
                estimatedDurationSeconds = 55
            ),
            ModuleAudioScript(
                id = "vault_1_meditation",
                moduleId = 1,
                moduleTitle = "The Ignition",
                principleName = "Desire",
                title = "The White-Hot Furnace Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Ignite an unquenchable fire of purpose and burn all bridges of retreat.",
                textToSpeak = """
                    Breathe in vitality, and exhale all hesitation. 
                    Bring your attention to the center of your chest. 
                    Imagine a brilliant golden flame burning brightly within your heart. 
                    This flame is your burning desire. 
                    Watch it consume every excuse, every doubt, and every compromise you have ever made. 
                    The flame grows taller, illuminating your definite chief aim. 
                    You are unstoppable. You have burned all bridges of retreat behind you.
                """.trimIndent(),
                estimatedDurationSeconds = 70
            ),

            // --- VAULT 2: Faith ---
            ModuleAudioScript(
                id = "vault_2_affirmation",
                moduleId = 2,
                moduleTitle = "The Inner Shield",
                principleName = "Faith",
                title = "The Unconditional Faith Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Affirmation to induce unconditional belief in the subconscious mind through autosuggestion.",
                textToSpeak = """
                    Faith is the eternal elixir which gives life, power, and action to the impulse of thought. 
                    I possess unwavering faith in my ability to fulfill my Definite Chief Aim. 
                    I completely eliminate fear, doubt, and negative anticipation from my consciousness. 
                    I believe that infinite intelligence is guiding every step of my journey toward total victory.
                """.trimIndent(),
                estimatedDurationSeconds = 50
            ),
            ModuleAudioScript(
                id = "vault_2_meditation",
                moduleId = 2,
                moduleTitle = "The Inner Shield",
                principleName = "Faith",
                title = "Sanctuary of Total Conviction Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Step into the sanctuary of the higher mind and cultivate unbreakable certainty.",
                textToSpeak = """
                    Take a slow, deep breath in through your nose, and let it out gently. 
                    Picture yourself walking into a luminous sanctuary of quiet strength. 
                    Here, no doubt can touch you. 
                    Hear the steady rhythm of your heartbeat. 
                    Know with every fiber of your being that what you seek is already seeking you. 
                    Faith is not hoping for the best; it is knowing that your victory is already accomplished in spirit.
                """.trimIndent(),
                estimatedDurationSeconds = 65
            ),

            // --- VAULT 3: Autosuggestion ---
            ModuleAudioScript(
                id = "vault_3_affirmation",
                moduleId = 3,
                moduleTitle = "The Code Injector",
                principleName = "Autosuggestion",
                title = "The Subconscious Reprogramming Autosuggestion",
                type = AudioScriptType.AFFIRMATION,
                description = "Voluntary injection of constructive thoughts into the seat of your emotional engine.",
                textToSpeak = """
                    Day by day, in every way, I am becoming more powerful, focused, and prosperous. 
                    I guard the gateway of my mind against every negative suggestion. 
                    I impress upon my subconscious mind only pictures of triumph, abundance, health, and mastery. 
                    My subconscious mind accepts these commands and immediately translates them into their physical equivalents.
                """.trimIndent(),
                estimatedDurationSeconds = 50
            ),
            ModuleAudioScript(
                id = "vault_3_meditation",
                moduleId = 3,
                moduleTitle = "The Code Injector",
                principleName = "Autosuggestion",
                title = "Theta Gateway Reprogramming Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Access the deep receptive state between waking and sleeping to plant master commands.",
                textToSpeak = """
                    Sink deeply into relaxation. 
                    Feel your eyelids grow pleasantly heavy. 
                    As your breathing slows, the conscious barrier opens. 
                    Your subconscious mind is now listening with total clarity. 
                    Speak to it in silence: 'I am the master of my fate. I am the captain of my soul. Riches flow to me in avalanches of abundance.' 
                    Let this truth absorb into every cell.
                """.trimIndent(),
                estimatedDurationSeconds = 80
            ),

            // --- VAULT 4: Specialized Knowledge ---
            ModuleAudioScript(
                id = "vault_4_affirmation",
                moduleId = 4,
                moduleTitle = "The Precision Lens",
                principleName = "Specialized Knowledge",
                title = "The Sovereign Mastery Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Transform raw information into organized, directed, highly lucrative specialized power.",
                textToSpeak = """
                    Knowledge is only potential power. It becomes actual power only when organized into definite plans and directed to a definite end. 
                    I continuously acquire highly specialized wisdom in my chosen craft. 
                    I synthesize knowledge into actionable value that solves great problems and commands premium rewards.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),

            // --- VAULT 5: Imagination ---
            ModuleAudioScript(
                id = "vault_5_affirmation",
                moduleId = 5,
                moduleTitle = "The Blueprint Chamber",
                principleName = "Imagination",
                title = "Synthetic & Creative Imagination Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Activate the workshop wherein are fashioned all plans for human achievement.",
                textToSpeak = """
                    My imagination is the workshop of my soul. 
                    I combine established concepts into groundbreaking solutions through synthetic imagination. 
                    I tune into the infinite reservoir of creative imagination to receive sudden flashes of inspiration, insight, and invention. 
                    Whatever my mind can conceive and believe, my mind can achieve.
                """.trimIndent(),
                estimatedDurationSeconds = 50
            ),
            ModuleAudioScript(
                id = "vault_5_meditation",
                moduleId = 5,
                moduleTitle = "The Blueprint Chamber",
                principleName = "Imagination",
                title = "The Astral Workshop Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Visit your inner architectural studio to design your empire in vivid sensory detail.",
                textToSpeak = """
                    Breathe in, and imagine stepping into a high glass laboratory overlooking the horizon. 
                    On the drafting table before you lies the master blueprint of your life. 
                    See yourself living in total freedom. 
                    Feel the textures, hear the conversations of congratulations, and see the numbers in your accounts. 
                    Hold this mental picture until it feels as tangible as the air you breathe.
                """.trimIndent(),
                estimatedDurationSeconds = 70
            ),

            // --- VAULT 6: Organized Planning ---
            ModuleAudioScript(
                id = "vault_6_affirmation",
                moduleId = 6,
                moduleTitle = "The Master Strategy",
                principleName = "Organized Planning",
                title = "The Flawless Execution Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "The QQS formula: Quality plus Quantity plus Spirit of Harmony equals sovereign leadership.",
                textToSpeak = """
                    I build practical, definite, and continuous plans for the achievement of my chief aim. 
                    When a plan fails, I recognize temporary defeat as a signal to reconstruct a stronger plan. 
                    I deliver the highest quality of service, in generous quantity, with a radiant spirit of harmony.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),

            // --- VAULT 7: Decision ---
            ModuleAudioScript(
                id = "vault_7_affirmation",
                moduleId = 7,
                moduleTitle = "The Iron Will",
                principleName = "Decision",
                title = "The Decisive Commander Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Master prompt, unyielding decision-making and eliminate procrastination forever.",
                textToSpeak = """
                    I reach decisions promptly and change them very slowly, if at all. 
                    I do not allow the opinions of others to sway my course. 
                    I have a mind of my own and I make my own decisions. 
                    Procrastination is destroyed by my immediate and resolute action.
                """.trimIndent(),
                estimatedDurationSeconds = 40
            ),

            // --- VAULT 8: Persistence ---
            ModuleAudioScript(
                id = "vault_8_affirmation",
                moduleId = 8,
                moduleTitle = "The Unyielding Spine",
                principleName = "Persistence",
                title = "The Unbreakable Persistence Vow",
                type = AudioScriptType.AFFIRMATION,
                description = "The essential factor in transmuting desire into monetary counter-value.",
                textToSpeak = """
                    Persistence is to the character of man what carbon is to steel. 
                    No amount of temporary defeat will ever turn me back. 
                    I endure every trial, I outlast every storm, and I march through every valley until victory is secured. 
                    Riches yield inevitably to those who will not quit.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),
            ModuleAudioScript(
                id = "vault_8_meditation",
                moduleId = 8,
                moduleTitle = "The Unyielding Spine",
                principleName = "Persistence",
                title = "The Diamond Pillar Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Cultivate the unbreakable resilience of forged carbon through deep rhythmic breathing.",
                textToSpeak = """
                    Ground your posture. Feel your spine erect, like a pillar of solid diamond. 
                    With every breath in, absorb the timeless resilience of ancient mountains. 
                    Life's challenges are merely winds blowing against the stone. 
                    They polish you, refine you, and make you indomitable. 
                    Affirm silently: 'I persist. I conquer. I prevail.'
                """.trimIndent(),
                estimatedDurationSeconds = 65
            ),

            // --- VAULT 9: Power of the Master Mind ---
            ModuleAudioScript(
                id = "vault_9_affirmation",
                moduleId = 9,
                moduleTitle = "The Sovereign Council",
                principleName = "Master Mind",
                title = "The Master Mind Synergy Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Coordination of knowledge and effort in a spirit of absolute harmony.",
                textToSpeak = """
                    No two minds ever come together without thereby creating a third, invisible, intangible force. 
                    I surround myself with individuals of supreme competence, integrity, and shared vision. 
                    Together in absolute harmony, our collective genius multiplies our power ten-thousand fold.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),

            // --- VAULT 10: Mystery of Sex Transmutation ---
            ModuleAudioScript(
                id = "vault_10_affirmation",
                moduleId = 10,
                moduleTitle = "The High Dynamo",
                principleName = "Transmutation",
                title = "The Vital Energy Transmutation Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Channel the most powerful human impulse into creative genius and boundless vitality.",
                textToSpeak = """
                    I harness and transmute the primordial creative life force within me into laser-focused ambition, radiant magnetism, and masterpiece creation. 
                    My energy is pure, elevated, and directed entirely toward monumental achievement.
                """.trimIndent(),
                estimatedDurationSeconds = 40
            ),
            ModuleAudioScript(
                id = "vault_10_meditation",
                moduleId = 10,
                moduleTitle = "The High Dynamo",
                principleName = "Transmutation",
                title = "The Golden Dynamo Kundalini Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Ascend vital energy from the base chakra up the spinal column into the high centers of creative genius.",
                textToSpeak = """
                    Breathe deeply from the lower abdomen. 
                    Feel a warm, concentrated vortex of golden vital energy stirring at the base of your spine. 
                    As you draw a deep breath upward, visualize this golden river ascending through your heart, through your throat, directly into your third eye and crown. 
                    Feel your mind illuminate with electrifying clarity and sovereign enthusiasm.
                """.trimIndent(),
                estimatedDurationSeconds = 75
            ),

            // --- VAULT 11: The Subconscious Mind ---
            ModuleAudioScript(
                id = "vault_11_affirmation",
                moduleId = 11,
                moduleTitle = "The Deep Engine",
                principleName = "Subconscious Mind",
                title = "The Infinite Intelligence Conduit Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "The connecting link between finite man and Infinite Intelligence.",
                textToSpeak = """
                    My subconscious mind works day and night to translate my dominant thoughts into physical reality. 
                    I feed it only faith, confidence, courage, and clear instructions. 
                    It is an open conduit to Infinite Intelligence, providing me with infallible guidance, solutions, and boundless wealth.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),

            // --- VAULT 12: The Brain ---
            ModuleAudioScript(
                id = "vault_12_affirmation",
                moduleId = 12,
                moduleTitle = "The Quantum Transmitter",
                principleName = "The Brain",
                title = "The Broadcasting Station of Thought Affirmation",
                type = AudioScriptType.AFFIRMATION,
                description = "Tune your brain's broadcasting and receiving station to higher frequencies of wealth.",
                textToSpeak = """
                    My brain is both a broadcasting and receiving station for the vibrations of thought. 
                    I broadcast high-voltage impulses of passion, purpose, and certainty. 
                    I receive harmonious frequencies of breakthrough insights and opportunities from the universal ether.
                """.trimIndent(),
                estimatedDurationSeconds = 45
            ),

            // --- VAULT 13: The Sixth Sense ---
            ModuleAudioScript(
                id = "vault_13_affirmation",
                moduleId = 13,
                moduleTitle = "The Temple of Wisdom",
                principleName = "The Sixth Sense",
                title = "The Infallible Intuition Decree",
                type = AudioScriptType.AFFIRMATION,
                description = "The apex of the philosophy: communication with Infinite Intelligence via the Sixth Sense.",
                textToSpeak = """
                    The Sixth Sense is that portion of the subconscious mind which is referred to as Creative Imagination. 
                    Through it, Infinite Intelligence communicates instantaneously without effort. 
                    I trust my gut, I listen to the subtle whispers of intuition, and I walk in absolute divine confidence. 
                    The apex of mastery is mine.
                """.trimIndent(),
                estimatedDurationSeconds = 50
            ),
            ModuleAudioScript(
                id = "vault_13_meditation",
                moduleId = 13,
                moduleTitle = "The Temple of Wisdom",
                principleName = "The Sixth Sense",
                title = "The Invisible Counselors Chamber Meditation",
                type = AudioScriptType.MEDITATION,
                description = "Convene your nightly council of historic master minds to receive transcendent counsel.",
                textToSpeak = """
                    Enter the silent, sacred council chamber of your higher mind. 
                    Seated around an oval mahogany table are the great masters of history who inspire you most. 
                    Look them in the eyes. Ask them your deepest strategic question. 
                    Silence all mental chatter and listen. 
                    Feel the quiet flash of intuition dawn upon your awareness. 
                    Accept this divine guidance with reverence and gratitude.
                """.trimIndent(),
                estimatedDurationSeconds = 85
            )
        )
    }
}
