package com.own.smartsubtitle.ui.screens.word

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.own.smartsubtitle.domain.model.WordTranslation


@Composable
fun WordScreen(
    viewModel: WordViewModel = hiltViewModel()
) {

    val words = remember { viewModel.words }

    LazyColumn(modifier = Modifier.padding(4.dp)) {
        items(words.value) {
            ShowWord(word = it, viewModel::deleteWord)
        }
    }
}

@Composable
private fun ShowWord(word: WordTranslation, onDeleteClicked: (WordTranslation) -> Unit) {
    val isRemoved = remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(2.dp)
    ) {
        Text(
            text = "${word.source} = ${word.translation}",
            color = if (isRemoved.value) Color.LightGray else Color.Black
        )
        Text(
            text = if (isRemoved.value) "Done" else "Delete",
            color = if (isRemoved.value) Color.LightGray else Color.Red,
            modifier = Modifier.clickable {
                if (!isRemoved.value) {
                    isRemoved.value = true
                    onDeleteClicked(word)
                }
            }
        )
    }
}

