package com.own.smartsubtitle.ui.screens.test

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EqViewModel @Inject constructor(

): ViewModel() {

    private val _presets = mutableStateOf<List<Preset>>(emptyList())
    val presets = _presets as State<List<Preset>>

    init {
        _presets.value = generatePresets()
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