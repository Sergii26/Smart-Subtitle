package com.own.smartsubtitle.ui.screens.home

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.own.smartsubtitle.domain.managers.translation.TranslationManager
import com.own.smartsubtitle.domain.model.Subtitle
import com.own.smartsubtitle.domain.model.TranslationItem
import com.own.smartsubtitle.domain.model.WordTranslation
import com.own.smartsubtitle.domain.repository.WordsRepository
import com.own.smartsubtitle.domain.usecases.ReadFileUseCase
import com.own.smartsubtitle.domain.usecases.SplitSourceToSubtitlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val readFileUseCase: ReadFileUseCase,
    private val splitSourceToSubtitlesUseCase: SplitSourceToSubtitlesUseCase,
    private val translationManager: TranslationManager,
    private val wordsRepository: WordsRepository
): ViewModel() {

    private val _subtitles = mutableStateOf<List<Subtitle>>(emptyList())
    val subtitles = _subtitles as State<List<Subtitle>>

    private val _activeSubtitleIndex = MutableStateFlow<ActiveSubtitle?>(null)
    val activeSubtitleIndex = _activeSubtitleIndex as Flow<ActiveSubtitle?>

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

    fun onSaveWordClicked(source: String, translated: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.saveWord(WordTranslation(source, translated))
        }
    }

    fun onStartTimePicked(hours: String, minutes: String, seconds: String) {
        beginSession((((hours.toInt() * 60 + minutes.toInt()) * 60 + seconds.toInt()) * 1000L))
    }

    private fun beginSession(startFrom: Long) {
        timerJob?.cancel()
        val startIndex = _subtitles.value.indexOfFirst { it.startTime > startFrom }
        val subs = _subtitles.value
        val startedTime = System.currentTimeMillis() - startFrom

        timerJob = CoroutineScope(Dispatchers.IO).launch {
            _activeSubtitleIndex.emit(ActiveSubtitle(startIndex, false))
            var index = startIndex
            delay(subs[startIndex + 1].startTime - startFrom)

            while (index < subs.size) {
                index++
                _activeSubtitleIndex.emit(ActiveSubtitle(index, true))
                if (index + 1 != subs.size) {
                    delay((startedTime + subs[index + 1].startTime - System.currentTimeMillis()))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        translationManager.close()
    }
}

data class ActiveSubtitle(val index: Int, val withAnim: Boolean)