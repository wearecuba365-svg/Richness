package com.example

import com.example.data.model.AudioScriptType
import com.example.data.repository.ModuleAudioScriptsProvider
import com.example.util.TtsPlaybackStatus
import com.example.util.TtsPlayerState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ModuleAudioScriptTest {

    @Test
    fun testModuleAudioScriptsExistForAllVaults() {
        val allScripts = ModuleAudioScriptsProvider.getAllScripts()
        assertTrue("Should have audio scripts for the 13 core vaults", allScripts.isNotEmpty())
        
        // Ensure every vault from 0 to 13 has valid scripts
        for (moduleId in 0..13) {
            val scriptsForMod = ModuleAudioScriptsProvider.getScriptsForModule(moduleId)
            assertTrue("Vault $moduleId should have audio scripts", scriptsForMod.isNotEmpty())
            
            val defaultScript = ModuleAudioScriptsProvider.getDefaultScriptForModule(moduleId)
            assertNotNull("Vault $moduleId should have a default audio script", defaultScript)
            assertEquals("Default script moduleId should match", moduleId, defaultScript.moduleId)
            assertTrue("Script text should not be empty", defaultScript.textToSpeak.isNotBlank())
        }
    }

    @Test
    fun testAudioScriptTypes() {
        val desireScript = ModuleAudioScriptsProvider.getDefaultScriptForModule(1)
        assertEquals(AudioScriptType.AFFIRMATION, desireScript.type)
        assertEquals("The Definite Chief Aim Decree", desireScript.title)

        val scripts = ModuleAudioScriptsProvider.getScriptsForModule(1)
        val meditation = scripts.firstOrNull { it.type == AudioScriptType.MEDITATION }
        assertNotNull("Vault 1 should contain a guided meditation", meditation)
        assertTrue("Meditation script text should not be blank", meditation?.textToSpeak?.isNotBlank() == true)
    }

    @Test
    fun testTtsPlayerStateModel() {
        val script = ModuleAudioScriptsProvider.getDefaultScriptForModule(1)
        val state = TtsPlayerState(
            status = TtsPlaybackStatus.PLAYING,
            currentScript = script,
            currentSentenceIndex = 2,
            totalSentences = 5,
            speechRate = 1.0f,
            speechPitch = 1.0f,
            isAmbientSoundEnabled = true
        )

        assertEquals(TtsPlaybackStatus.PLAYING, state.status)
        assertEquals(script, state.currentScript)
        assertEquals(2, state.currentSentenceIndex)
        assertEquals(5, state.totalSentences)
        assertTrue(state.isAmbientSoundEnabled)
    }
}
