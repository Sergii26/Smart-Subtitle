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
import timber.log.Timber
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

    private val _translatedWords = MutableStateFlow<List<String>>(emptyList())
    val translatedWords = _translatedWords as Flow<List<String>>

    private var timerJob: Job? = null

    private val awaitTranslationList = mutableListOf<TranslationItem>()

    private val _translation = MutableStateFlow(TranslationItem(-1, ""))
    val translation = _translation as Flow<TranslationItem>

    private val _isSessionRunning = mutableStateOf(false)
    val isSessionRunning = _isSessionRunning as State<Boolean>

    init {
        Timber.d("Init view model. active index: ${_activeSubtitleIndex.value}")
        viewModelScope.launch {
            translationManager.translationFlow.collect { item ->
                _translation.value = item
                awaitTranslationList.find { it.id ==  item.id }?.let { awaitItem ->
                    val newList = _translatedWords.value.toMutableList()
                    newList.add("${awaitItem.word} = ${item.word}")
                    awaitTranslationList.remove(awaitItem)
                    _translatedWords.value = newList
                }
            }
        }
    }

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
        awaitTranslationList.add(item)
        translationManager.translate(item)
    }

    fun onSaveWordClicked(source: String, translated: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.saveWord(WordTranslation(source, translated))
        }
    }

    fun onStartTimePicked(hours: String, minutes: String, seconds: String) {
        val startTimeMillis = (((hours.toInt() * 60 + minutes.toInt()) * 60 + seconds.toInt()) * 1000L)
        _isSessionRunning.value = true
        beginSession(startTimeMillis)
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

            while (index < subs.size - 1) {
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