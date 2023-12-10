package com.own.smartsubtitle.di

import android.content.Context
import com.own.smartsubtitle.data.word.WordsDBRepository
import com.own.smartsubtitle.data.word.room.WordsDao
import com.own.smartsubtitle.data.word.room.WordsDatabase
import com.own.smartsubtitle.domain.managers.translation.TranslationManager
import com.own.smartsubtitle.domain.managers.translation.impl.EnRuTranslationManager
import com.own.smartsubtitle.domain.repository.WordsRepository
import com.own.smartsubtitle.domain.usecases.ReadFileUseCase
import com.own.smartsubtitle.domain.usecases.SplitSourceToSubtitlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideReadFileUseCase(@ApplicationContext context: Context): ReadFileUseCase {
        return ReadFileUseCase(context)
    }

    @Provides
    fun provideSplitSourceToSubtitlesUseCase(): SplitSourceToSubtitlesUseCase {
        return SplitSourceToSubtitlesUseCase()
    }

    @Provides
    fun provideTranslationManager(): TranslationManager {
        return EnRuTranslationManager()
    }

    @Singleton
    @Provides
    fun provideWordsDao(@ApplicationContext context: Context): WordsDao {
        return WordsDatabase.getInstance(context).wordsDao()
    }

    @Provides
    fun provideWordsRepository(wordsDao: WordsDao): WordsRepository {
        return WordsDBRepository(wordsDao)
    }
}