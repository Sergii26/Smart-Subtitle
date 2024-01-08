package com.own.smartsubtitle.domain.managers.translation.impl

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.own.smartsubtitle.domain.managers.translation.TranslationManager
import com.own.smartsubtitle.domain.model.TranslationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class EnRuTranslationManager: TranslationManager {

    private val enUaTranslator = Translation.getClient(TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.RUSSIAN)
        .build())
    private val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    private val translationScope = CoroutineScope(Dispatchers.IO)
    private val _translationFlow = MutableStateFlow<TranslationItem>(TranslationItem(-1, ""))
    override val translationFlow = _translationFlow as StateFlow<TranslationItem>

    init {
        enUaTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Timber.d("Translation model successfully downloaded")
            }
            .addOnFailureListener { exception ->
                Timber.d("Failed to download translation model, error: ${exception.message}")
            }
    }

    override fun translate(item: TranslationItem) {
        enUaTranslator.translate(item.word)
            .addOnSuccessListener { translatedText ->
                translationScope.launch {
                    _translationFlow.emit(TranslationItem(item.id, translatedText))
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("Failed to translate, error: ${exception.message}")
            }
    }

    override fun close() {
        translationScope.cancel()
        enUaTranslator.close()
    }
}