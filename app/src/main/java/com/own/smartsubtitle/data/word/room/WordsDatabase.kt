package com.own.smartsubtitle.data.word.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordDto::class], version = 2)
abstract class WordsDatabase : RoomDatabase() {

    abstract fun wordsDao(): WordsDao

    companion object {

        fun getInstance(context: Context): WordsDatabase {
            return createDatabase(context)
        }

        private fun createDatabase(context: Context): WordsDatabase {
            return Room.databaseBuilder(
                context,
                WordsDatabase::class.java,
                "words"
            ).build()
        }
    }
}