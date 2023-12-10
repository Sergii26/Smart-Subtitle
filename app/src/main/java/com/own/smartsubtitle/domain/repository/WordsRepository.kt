package com.own.smartsubtitle.domain.repository

import com.own.smartsubtitle.domain.model.WordTranslation

interface WordsRepository {
    suspend fun saveWord(word: WordTranslation)
    suspend fun getWords(): List<WordTranslation>
    suspend fun deleteWord(word: WordTranslation)
}