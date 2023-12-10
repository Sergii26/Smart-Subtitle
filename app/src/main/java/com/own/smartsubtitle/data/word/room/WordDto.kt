package com.own.smartsubtitle.data.word.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordDto(
    val source: String,
    val translation: String,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)




