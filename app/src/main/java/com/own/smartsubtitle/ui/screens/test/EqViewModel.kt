package com.own.smartsubtitle.ui.screens.test

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.own.smartsubtitle.domain.model.WordTranslation
import com.own.smartsubtitle.domain.repository.WordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class EqViewModel @Inject constructor(

): ViewModel() {

    private val _presets = mutableStateOf<List<Preset>>(emptyList())
    val presets = _presets as State<List<Preset>>

    init {
        _presets.value = generatePresets()
    }

    private suspend fun setPreset(): Flow<Boolean> {
       return flow {
           delay(50)
           emit(Random.nextInt(0, 99) != 34)
       }
    }

    private fun generatePresets(): List<Preset> {
        return listOf(
            Preset("Preset A", 50, 120, 0, 255, 70),
            Preset("Preset B", 50, 50, 50, 50, 50),
            Preset("Preset C", 80, 80, 80, 80, 80),
            Preset("Preset D", 120, 120, 120, 120, 120),
            Preset("Preset E", 200, 200, 200, 200, 200),
            Preset("Preset F", 255, 255, 255, 255, 255),
        )
    }
}

data class Preset(
    val name: String,
    val bass: Int,
    val midBass: Int,
    val mid: Int,
    val midTreble: Int,
    val treble: Int
)