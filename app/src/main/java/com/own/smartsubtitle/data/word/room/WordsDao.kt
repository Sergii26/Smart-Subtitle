package com.own.smartsubtitle.data.word.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordsDao {

    @Query("SELECT * FROM words")
    fun getWords(): List<WordDto>

    @Insert
    fun saveWord(word: WordDto)

//    @Query("DELETE FROM words WHERE source = :source AND translation = :translation")
    @Delete
    fun deleteWord(word: WordDto)
}