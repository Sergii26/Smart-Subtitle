package com.own.smartsubtitle.data.word

import com.own.smartsubtitle.data.word.room.WordDto
import com.own.smartsubtitle.data.word.room.WordsDao
import com.own.smartsubtitle.domain.model.WordTranslation
import com.own.smartsubtitle.domain.repository.WordsRepository

class WordsDBRepository(
    private val wordsDao: WordsDao
): WordsRepository {
    override suspend fun saveWord(word: WordTranslation) {
        wordsDao.saveWord(WordDto(word.source, word.translation))
    }

    override suspend fun getWords(): List<WordTranslation> {
        return wordsDao.getWords().map { WordTranslation(it.source, it.translation) }
    }

    override suspend fun deleteWord(word: WordTranslation) {
        wordsDao.deleteWord(WordDto(word.source, word.translation))
    }

}