package com.own.smartsubtitle.domain.managers.translation

import com.own.smartsubtitle.domain.model.TranslationItem
import kotlinx.coroutines.flow.Flow

interface TranslationManager {
    val translationFlow: Flow<TranslationItem>
    fun translate(item: TranslationItem)
    fun close()
}