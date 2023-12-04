package com.own.smartsubtitle.ui.di

import android.content.Context
import com.own.smartsubtitle.domain.managers.translation.TranslationManager
import com.own.smartsubtitle.domain.managers.translation.impl.EnRuTranslationManager
import com.own.smartsubtitle.domain.managers.translation.impl.EnUaTranslationManager
import com.own.smartsubtitle.domain.usecases.ReadFileUseCase
import com.own.smartsubtitle.domain.usecases.SplitSourceToSubtitlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
}