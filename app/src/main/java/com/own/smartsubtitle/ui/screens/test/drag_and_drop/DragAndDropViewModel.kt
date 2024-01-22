package com.own.smartsubtitle.ui.screens.test.drag_and_drop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DragAndDropViewModel @Inject constructor(

): ViewModel() {

    private val _drawerItems = mutableStateOf<List<Item>>(emptyList())
    val drawerItems = _drawerItems as State<List<Item>>

    private val _mainItem = mutableStateOf<Item?>(null)
    val mainItem = _mainItem as State<Item?>

    init {
        _mainItem.value = Item("0", "Item 0", 0x000000)

        _drawerItems.value = listOf(
            Item("1", "Item 1", 0xFFFFFFFF),
            Item("2", "Item 2", 0xFFF0F0F0),
            Item("3", "Item 3", 0xFF757575),
            Item("4", "Item 4", 0xFF161616),
            Item("5", "Item 5", 0xFF979797)
        )
    }
}

data class Item(val id: String, val name: String, val color: Long)

