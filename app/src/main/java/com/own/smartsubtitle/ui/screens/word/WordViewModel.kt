package com.own.smartsubtitle.ui.screens.word

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.own.smartsubtitle.domain.model.WordTranslation
import com.own.smartsubtitle.domain.repository.WordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val wordsRepository: WordsRepository
): ViewModel() {

    private val _words = mutableStateOf<List<WordTranslation>>(emptyList())
    val words = _words as State<List<WordTranslation>>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val words = wordsRepository.getWords()
            withContext(Dispatchers.Main) {
                _words.value = words
            }
        }
    }

    fun deleteWord(word: WordTranslation) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsRepository.deleteWord(word)
        }
    }
}