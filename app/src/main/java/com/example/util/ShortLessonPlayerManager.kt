package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.ShortLessonEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

data class ShortLessonPlayerState(
    val activeLesson: ShortLessonEntity? = null,
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val durationSeconds: Int = 0,
    val playbackSpeed: Float = 1.0f,
    val isVideoMode: Boolean = false,
    val showTranscript: Boolean = false,
    val showChapters: Boolean = false,
    val isAmbientThetaEnabled: Boolean = true,
    val completionTriggered: Boolean = false
)

class ShortLessonPlayerManager(
    private val context: Context,
    private val onLessonAutoCompleted: (ShortLessonEntity) -> Unit
) : TextToSpeech.OnInitListener {

    private val tag = "ShortLessonPlayer"
    private val scope = CoroutineScope(Dispatchers.Main)

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    private val _playerState = MutableStateFlow(ShortLessonPlayerState())
    val playerState: StateFlow<ShortLessonPlayerState> = _playerState.asStateFlow()

    private var progressTickerJob: Job? = null
    private var ambientTrack: AudioTrack? = null
    private var ambientJob: Job? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize TTS for ShortLessonPlayer", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            tts?.setLanguage(Locale.US)
            tts?.setSpeechRate(_playerState.value.playbackSpeed)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    scope.launch {
                        handleNarrationFinished()
                    }
                }
                override fun onError(utteranceId: String?) {}
            })
        }
    }

    fun playLesson(lesson: ShortLessonEntity, startFromLastPosition: Boolean = true) {
        val startPos = if (startFromLastPosition && lesson.lastPlaybackPositionSeconds > 0 && lesson.lastPlaybackPositionSeconds < lesson.durationSeconds) {
            lesson.lastPlaybackPositionSeconds
        } else {
            0
        }

        _playerState.value = ShortLessonPlayerState(
            activeLesson = lesson,
            isPlaying = true,
            currentPositionSeconds = startPos,
            durationSeconds = lesson.durationSeconds,
            playbackSpeed = _playerState.value.playbackSpeed,
            isVideoMode = lesson.isVideo,
            isAmbientThetaEnabled = _playerState.value.isAmbientThetaEnabled,
            completionTriggered = lesson.isCompleted
        )

        startProgressTicker()
        startSpeechSynthesis(lesson, startPos)
        if (_playerState.value.isAmbientThetaEnabled) {
            startAmbientSynth()
        }
    }

    fun resume() {
        val state = _playerState.value
        val lesson = state.activeLesson ?: return
        _playerState.value = state.copy(isPlaying = true)
        startProgressTicker()
        startSpeechSynthesis(lesson, state.currentPositionSeconds)
        if (state.isAmbientThetaEnabled) {
            startAmbientSynth()
        }
    }

    fun pause() {
        _playerState.value = _playerState.value.copy(isPlaying = false)
        stopProgressTicker()
        tts?.stop()
        stopAmbientSynth()
    }

    fun stop() {
        stopProgressTicker()
        tts?.stop()
        stopAmbientSynth()
        _playerState.value = ShortLessonPlayerState()
    }

    fun seekTo(seconds: Int) {
        val state = _playerState.value
        val lesson = state.activeLesson ?: return
        val clamped = seconds.coerceIn(0, state.durationSeconds)
        _playerState.value = state.copy(currentPositionSeconds = clamped)
        if (state.isPlaying) {
            startSpeechSynthesis(lesson, clamped)
        }
    }

    fun seekRelative(deltaSeconds: Int) {
        val current = _playerState.value.currentPositionSeconds
        seekTo(current + deltaSeconds)
    }

    fun setSpeed(speed: Float) {
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
        tts?.setSpeechRate(speed)
    }

    fun toggleVideoMode() {
        _playerState.value = _playerState.value.copy(isVideoMode = !_playerState.value.isVideoMode)
    }

    fun toggleTranscript() {
        _playerState.value = _playerState.value.copy(showTranscript = !_playerState.value.showTranscript)
    }

    fun toggleChapters() {
        _playerState.value = _playerState.value.copy(showChapters = !_playerState.value.showChapters)
    }

    fun toggleAmbient() {
        val next = !_playerState.value.isAmbientThetaEnabled
        _playerState.value = _playerState.value.copy(isAmbientThetaEnabled = next)
        if (next && _playerState.value.isPlaying) {
            startAmbientSynth()
        } else {
            stopAmbientSynth()
        }
    }

    private fun startProgressTicker() {
        progressTickerJob?.cancel()
        progressTickerJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(1000L)
                val state = _playerState.value
                if (!state.isPlaying || state.activeLesson == null) break
                val newPos = state.currentPositionSeconds + 1
                val dur = state.durationSeconds
                
                if (newPos >= dur) {
                    _playerState.value = state.copy(
                        currentPositionSeconds = dur,
                        isPlaying = false,
                        completionTriggered = true
                    )
                    state.activeLesson.let { onLessonAutoCompleted(it) }
                    stopAmbientSynth()
                    break
                } else {
                    val willComplete = newPos >= (dur * 0.9f) && !state.completionTriggered
                    if (willComplete) {
                        state.activeLesson.let { onLessonAutoCompleted(it) }
                    }
                    _playerState.value = state.copy(
                        currentPositionSeconds = newPos,
                        completionTriggered = state.completionTriggered || willComplete
                    )
                }
            }
        }
    }

    private fun stopProgressTicker() {
        progressTickerJob?.cancel()
        progressTickerJob = null
    }

    private fun startSpeechSynthesis(lesson: ShortLessonEntity, startSeconds: Int) {
        if (!isTtsInitialized) return
        val transcript = lesson.transcript
        if (transcript.isBlank()) return
        
        // Calculate sub-portion based on fraction
        val fraction = if (lesson.durationSeconds > 0) startSeconds.toFloat() / lesson.durationSeconds.toFloat() else 0f
        val charsToSkip = (transcript.length * fraction.coerceIn(0f, 0.95f)).toInt()
        val textToSpeak = transcript.substring(charsToSkip).trim()
        
        tts?.stop()
        if (textToSpeak.isNotBlank()) {
            tts?.setSpeechRate(_playerState.value.playbackSpeed)
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "lesson_${lesson.id}")
        }
    }

    private fun handleNarrationFinished() {
        val state = _playerState.value
        val lesson = state.activeLesson ?: return
        if (state.isPlaying && !state.completionTriggered) {
            _playerState.value = state.copy(completionTriggered = true)
            onLessonAutoCompleted(lesson)
        }
    }

    private fun startAmbientSynth() {
        stopAmbientSynth()
        ambientJob = scope.launch(Dispatchers.Default) {
            try {
                val sampleRate = 44100
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = maxOf(minBufferSize, sampleRate / 2)
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                val audioFormat = AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()

                val track = AudioTrack(
                    audioAttributes,
                    audioFormat,
                    bufferSize,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
                )
                ambientTrack = track
                track.play()

                val buffer = ShortArray(bufferSize)
                var phaseCarrier = 0.0
                val freqCarrier = 108.0 // Warm golden base frequency
                val binauralBeat = 6.0 // 6Hz Theta frequency
                val twoPi = 2.0 * Math.PI

                while (isActive) {
                    for (i in buffer.indices) {
                        val carrier = sin(phaseCarrier)
                        val beatMod = 0.5 + 0.5 * sin(twoPi * binauralBeat * phaseCarrier / (twoPi * freqCarrier))
                        val sampleVal = (carrier * beatMod * 32767 * 0.06).toInt()
                        buffer[i] = sampleVal.coerceIn(-32767, 32767).toShort()
                        phaseCarrier += twoPi * freqCarrier / sampleRate
                        if (phaseCarrier > twoPi) phaseCarrier -= twoPi
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (e: Exception) {
                Log.w(tag, "Ambient synth interrupted: ${e.message}")
            }
        }
    }

    private fun stopAmbientSynth() {
        ambientJob?.cancel()
        ambientJob = null
        try {
            ambientTrack?.stop()
            ambientTrack?.release()
        } catch (_: Exception) {}
        ambientTrack = null
    }

    fun release() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
