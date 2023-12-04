package com.own.smartsubtitle.ui.screens

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.own.smartsubtitle.domain.managers.translation.TranslationManager
import com.own.smartsubtitle.domain.model.Subtitle
import com.own.smartsubtitle.domain.model.TranslationItem
import com.own.smartsubtitle.domain.usecases.ReadFileUseCase
import com.own.smartsubtitle.domain.usecases.SplitSourceToSubtitlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val readFileUseCase: ReadFileUseCase,
    private val splitSourceToSubtitlesUseCase: SplitSourceToSubtitlesUseCase,
    private val translationManager: TranslationManager
): ViewModel() {

    private val _subtitles = mutableStateOf<List<Subtitle>>(emptyList())
    val subtitles = _subtitles as State<List<Subtitle>>

    private val _activeSubtitleIndex = MutableStateFlow<Int?>(null)
    val activeSubtitleIndex = _activeSubtitleIndex as Flow<Int?>

    private var timerJob: Job? = null

    val translation = translationManager.translationFlow

    fun onUriReceived(uri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            uri?.let {
                readFileUseCase(it).also { string ->
                    _subtitles.value =
                        splitSourceToSubtitlesUseCase(string.drop(1))
                    beginSession(0)
                }
            }
        }
    }

    fun onWordClicked(item: TranslationItem) {
        translationManager.translate(item)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        translationManager.close()
    }

    fun onStartTimePicked(hours: String, minutes: String, seconds: String) {
        beginSession(((hours.toInt() * 60 + minutes.toInt() * 60 + seconds.toInt()) * 1000L))
    }

    private fun beginSession(startFrom: Long) {
        timerJob?.cancel()
        val startIndex = _subtitles.value.indexOfFirst { it.startTime > startFrom }
        val subs = _subtitles.value

        timerJob = CoroutineScope(Dispatchers.IO).launch {
            _activeSubtitleIndex.emit(startIndex)
            var index = startIndex
            delay(subs[startIndex + 1].startTime - startFrom)

            while (index < subs.size) {
                index++
                Timber.d("check task. Emit index: $index")
                _activeSubtitleIndex.emit(index)
                delay((subs[index + 1].startTime - subs[index].startTime).also {
                    Timber.d("check task. delay: $it")
                })
            }
        }
    }

    private fun tickerFlow(millis: Duration) = flow<Long> {
        var start = 0L

        while (true) {
            emit(start)
            start += millis.inWholeMilliseconds
            delay(millis)
        }
    }
}