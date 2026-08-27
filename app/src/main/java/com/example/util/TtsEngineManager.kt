package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.ModuleAudioScript
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

enum class TtsPlaybackStatus {
    IDLE,
    PREPARING,
    PLAYING,
    PAUSED,
    COMPLETED,
    ERROR
}

data class TtsPlayerState(
    val status: TtsPlaybackStatus = TtsPlaybackStatus.IDLE,
    val currentScript: ModuleAudioScript? = null,
    val currentSentenceIndex: Int = 0,
    val totalSentences: Int = 0,
    val currentSentenceText: String = "",
    val speechRate: Float = 1.0f,
    val speechPitch: Float = 1.0f,
    val isAmbientSoundEnabled: Boolean = false,
    val errorMessage: String? = null,
    val estimatedRemainingSeconds: Int = 0
)

class TtsEngineManager(private val context: Context) : TextToSpeech.OnInitListener {

    private val tag = "TtsEngineManager"
    private val scope = CoroutineScope(Dispatchers.Main)

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _playerState = MutableStateFlow(TtsPlayerState())
    val playerState: StateFlow<TtsPlayerState> = _playerState.asStateFlow()

    // Ambient Synth Background (Theta 6Hz / 432Hz binaural frequency generator)
    private var ambientTrack: AudioTrack? = null
    private var ambientJob: Job? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize TextToSpeech", e)
            _playerState.value = _playerState.value.copy(
                status = TtsPlaybackStatus.ERROR,
                errorMessage = "TTS initialization failed: ${e.localizedMessage}"
            )
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(_playerState.value.speechRate)
            tts?.setPitch(_playerState.value.speechPitch)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    scope.launch {
                        _playerState.value = _playerState.value.copy(status = TtsPlaybackStatus.PLAYING)
                    }
                }

                override fun onDone(utteranceId: String?) {
                    scope.launch {
                        handleSentenceCompleted()
                    }
                }

                override fun onError(utteranceId: String?) {
                    scope.launch {
                        Log.e(tag, "Utterance playback error for id: $utteranceId")
                    }
                }
            })

            isInitialized = true
            Log.i(tag, "TTS Engine successfully initialized")
        } else {
            isInitialized = false
            _playerState.value = _playerState.value.copy(
                status = TtsPlaybackStatus.ERROR,
                errorMessage = "Text-to-Speech service unavailable on this device."
            )
        }
    }

    fun playScript(script: ModuleAudioScript, startSentenceIndex: Int = 0) {
        val sentences = script.sentences
        if (sentences.isEmpty()) return

        val safeIndex = startSentenceIndex.coerceIn(0, sentences.size - 1)

        _playerState.value = _playerState.value.copy(
            status = TtsPlaybackStatus.PREPARING,
            currentScript = script,
            currentSentenceIndex = safeIndex,
            totalSentences = sentences.size,
            currentSentenceText = sentences[safeIndex],
            errorMessage = null
        )

        speakCurrentSentence()
    }

    fun resume() {
        val state = _playerState.value
        if (state.currentScript != null && state.status == TtsPlaybackStatus.PAUSED) {
            speakCurrentSentence()
        }
    }

    fun pause() {
        tts?.stop()
        _playerState.value = _playerState.value.copy(status = TtsPlaybackStatus.PAUSED)
    }

    fun stop() {
        tts?.stop()
        stopAmbientSoundInternal()
        _playerState.value = _playerState.value.copy(
            status = TtsPlaybackStatus.IDLE,
            currentSentenceIndex = 0,
            currentSentenceText = ""
        )
    }

    fun nextSentence() {
        val state = _playerState.value
        val script = state.currentScript ?: return
        val nextIdx = state.currentSentenceIndex + 1
        if (nextIdx < script.sentences.size) {
            seekToSentence(nextIdx)
        } else {
            // Reached end
            stop()
            _playerState.value = _playerState.value.copy(status = TtsPlaybackStatus.COMPLETED)
        }
    }

    fun previousSentence() {
        val state = _playerState.value
        val prevIdx = (state.currentSentenceIndex - 1).coerceAtLeast(0)
        seekToSentence(prevIdx)
    }

    fun seekToSentence(index: Int) {
        val script = _playerState.value.currentScript ?: return
        val sentences = script.sentences
        if (index in sentences.indices) {
            tts?.stop()
            _playerState.value = _playerState.value.copy(
                currentSentenceIndex = index,
                currentSentenceText = sentences[index]
            )
            speakCurrentSentence()
        }
    }

    fun setSpeechRate(rate: Float) {
        val clamped = rate.coerceIn(0.5f, 2.0f)
        tts?.setSpeechRate(clamped)
        _playerState.value = _playerState.value.copy(speechRate = clamped)
    }

    fun setSpeechPitch(pitch: Float) {
        val clamped = pitch.coerceIn(0.5f, 1.8f)
        tts?.setPitch(clamped)
        _playerState.value = _playerState.value.copy(speechPitch = clamped)
    }

    fun toggleAmbientSound() {
        val newState = !_playerState.value.isAmbientSoundEnabled
        _playerState.value = _playerState.value.copy(isAmbientSoundEnabled = newState)
        if (newState) {
            startAmbientSoundInternal()
        } else {
            stopAmbientSoundInternal()
        }
    }

    private fun speakCurrentSentence() {
        val state = _playerState.value
        val script = state.currentScript ?: return
        val sentences = script.sentences
        val idx = state.currentSentenceIndex

        if (idx !in sentences.indices) {
            stop()
            _playerState.value = _playerState.value.copy(status = TtsPlaybackStatus.COMPLETED)
            return
        }

        val text = sentences[idx]
        _playerState.value = _playerState.value.copy(
            status = TtsPlaybackStatus.PLAYING,
            currentSentenceText = text,
            currentSentenceIndex = idx
        )

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sentence_$idx")
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "sentence_$idx")
    }

    private fun handleSentenceCompleted() {
        val state = _playerState.value
        val script = state.currentScript ?: return
        val nextIndex = state.currentSentenceIndex + 1

        if (nextIndex < script.sentences.size) {
            _playerState.value = _playerState.value.copy(
                currentSentenceIndex = nextIndex,
                currentSentenceText = script.sentences[nextIndex]
            )
            speakCurrentSentence()
        } else {
            _playerState.value = _playerState.value.copy(
                status = TtsPlaybackStatus.COMPLETED,
                currentSentenceIndex = 0,
                currentSentenceText = ""
            )
        }
    }

    // --- High-Performance Soothing Theta Frequency Ambient Audio Generator ---
    private fun startAmbientSoundInternal() {
        stopAmbientSoundInternal()
        ambientJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                val sampleRate = 22050
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(4096)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                ambientTrack = track
                track.play()

                val baseFreq = 136.1 // Om / Earth cosmic frequency
                val buffer = ShortArray(bufferSize / 2)
                var angle = 0.0

                while (isActive) {
                    for (i in buffer.indices) {
                        // Smooth sine wave with gentle amplitude modulation (theta frequency ~5.5Hz)
                        val thetaMod = 0.85 + 0.15 * sin(2.0 * Math.PI * 5.5 * angle / sampleRate)
                        val sampleVal = sin(2.0 * Math.PI * baseFreq * angle / sampleRate) * 0.12 * thetaMod
                        buffer[i] = (sampleVal * Short.MAX_VALUE).toInt().toShort()
                        angle += 1.0
                        if (angle >= sampleRate) angle -= sampleRate
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (e: Exception) {
                Log.w(tag, "Ambient synthesis audio exception: ${e.message}")
            }
        }
    }

    private fun stopAmbientSoundInternal() {
        ambientJob?.cancel()
        ambientJob = null
        try {
            ambientTrack?.stop()
            ambientTrack?.release()
        } catch (_: Exception) {}
        ambientTrack = null
    }

    fun speakRawText(text: String) {
        if (text.isBlank()) return
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "raw_affirmation_speech")
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "raw_affirmation_speech")
    }

    fun shutdown() {
        stopAmbientSoundInternal()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
