package com.example.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

enum class VoiceMemoState {
    IDLE,
    RECORDING,
    PLAYING,
    PAUSED
}

data class VoiceMemoUiState(
    val state: VoiceMemoState = VoiceMemoState.IDLE,
    val durationSeconds: Int = 0,
    val currentPositionSeconds: Int = 0,
    val hasRecording: Boolean = false,
    val audioFilePath: String? = null,
    val errorMessage: String? = null
)

class VoiceMemoRecorder(private val context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var timerJob: Job? = null

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentOutputFile: File? = null

    private val _uiState = MutableStateFlow(VoiceMemoUiState())
    val uiState: StateFlow<VoiceMemoUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "VoiceMemoRecorder"
        private const val MEMO_FILE_NAME = "definite_chief_aim_memo.m4a"

        fun getDefaultMemoFile(context: Context): File {
            return File(context.filesDir, MEMO_FILE_NAME)
        }
    }

    init {
        checkExistingRecording()
    }

    fun checkExistingRecording() {
        val defaultFile = getDefaultMemoFile(context)
        if (defaultFile.exists() && defaultFile.length() > 0) {
            _uiState.value = _uiState.value.copy(
                hasRecording = true,
                audioFilePath = defaultFile.absolutePath,
                errorMessage = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                hasRecording = false,
                audioFilePath = null
            )
        }
    }

    fun startRecording(): Boolean {
        try {
            stopPlaying()

            val outputFile = getDefaultMemoFile(context)
            if (outputFile.exists()) {
                outputFile.delete()
            }
            currentOutputFile = outputFile

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder

            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.RECORDING,
                durationSeconds = 0,
                errorMessage = null
            )

            startRecordingTimer()
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error starting voice recording: ${e.message}", e)
            mediaRecorder?.release()
            mediaRecorder = null
            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.IDLE,
                errorMessage = "Could not start recording. Check microphone permission."
            )
            return false
        }
    }

    fun stopRecording(): String? {
        try {
            timerJob?.cancel()
            timerJob = null

            mediaRecorder?.apply {
                try {
                    stop()
                } catch (e: RuntimeException) {
                    Log.w(TAG, "Stop failed or recorded too short: ${e.message}")
                }
                release()
            }
            mediaRecorder = null

            val file = currentOutputFile ?: getDefaultMemoFile(context)
            val hasFile = file.exists() && file.length() > 0

            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.IDLE,
                hasRecording = hasFile,
                audioFilePath = if (hasFile) file.absolutePath else null,
                errorMessage = if (!hasFile) "Recording too short" else null
            )

            return if (hasFile) file.absolutePath else null
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping recording: ${e.message}", e)
            mediaRecorder?.release()
            mediaRecorder = null
            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.IDLE,
                errorMessage = "Failed to save voice recording."
            )
            return null
        }
    }

    fun startPlaying(filePath: String? = null): Boolean {
        try {
            stopPlaying()

            val path = filePath ?: _uiState.value.audioFilePath ?: getDefaultMemoFile(context).absolutePath
            val file = File(path)
            if (!file.exists() || file.length() == 0L) {
                _uiState.value = _uiState.value.copy(errorMessage = "No recording found to play.")
                return false
            }

            val player = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                setOnCompletionListener {
                    stopPlaying()
                }
                start()
            }

            mediaPlayer = player

            val totalDurationSec = (player.duration / 1000).coerceAtLeast(1)
            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.PLAYING,
                durationSeconds = totalDurationSec,
                currentPositionSeconds = 0,
                errorMessage = null
            )

            startPlaybackTimer()
            return true
        } catch (e: IOException) {
            Log.e(TAG, "Error playing audio file: ${e.message}", e)
            mediaPlayer?.release()
            mediaPlayer = null
            _uiState.value = _uiState.value.copy(
                state = VoiceMemoState.IDLE,
                errorMessage = "Could not playback recording."
            )
            return false
        }
    }

    fun stopPlaying() {
        timerJob?.cancel()
        timerJob = null

        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing media player: ${e.message}")
        }
        mediaPlayer = null

        _uiState.value = _uiState.value.copy(
            state = VoiceMemoState.IDLE,
            currentPositionSeconds = 0
        )
    }

    fun deleteRecording() {
        stopPlaying()
        if (_uiState.value.state == VoiceMemoState.RECORDING) {
            stopRecording()
        }

        val file = getDefaultMemoFile(context)
        if (file.exists()) {
            file.delete()
        }

        _uiState.value = _uiState.value.copy(
            hasRecording = false,
            audioFilePath = null,
            durationSeconds = 0,
            currentPositionSeconds = 0,
            state = VoiceMemoState.IDLE
        )
    }

    private fun startRecordingTimer() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch {
            var seconds = 0
            while (isActive && _uiState.value.state == VoiceMemoState.RECORDING) {
                delay(1000)
                seconds++
                _uiState.value = _uiState.value.copy(durationSeconds = seconds)
                // Cap max recording at 3 minutes
                if (seconds >= 180) {
                    stopRecording()
                    break
                }
            }
        }
    }

    private fun startPlaybackTimer() {
        timerJob?.cancel()
        timerJob = coroutineScope.launch {
            while (isActive && _uiState.value.state == VoiceMemoState.PLAYING) {
                val posSec = (mediaPlayer?.currentPosition ?: 0) / 1000
                _uiState.value = _uiState.value.copy(currentPositionSeconds = posSec)
                delay(500)
            }
        }
    }

    fun release() {
        timerJob?.cancel()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
