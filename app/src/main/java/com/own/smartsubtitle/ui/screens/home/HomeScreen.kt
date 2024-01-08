package com.own.smartsubtitle.ui.screens.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.own.smartsubtitle.domain.model.Subtitle
import com.own.smartsubtitle.domain.model.TranslationItem
import com.own.smartsubtitle.ui.navigation.NavDest
import com.own.smartsubtitle.ui.theme.GreenText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val fileUri = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            viewModel.onUriReceived(uri)
        }
    )

    if (viewModel.subtitles.value.isEmpty()) {
        Timber.d("subtitles is empty")
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Button(
                modifier = Modifier
                    .alpha(if (viewModel.subtitles.value.isEmpty()) 1f else 0f)
                    .wrapContentSize()
                    .requiredSize(if (viewModel.subtitles.value.isEmpty()) 166.dp else 0.dp),
                onClick = { fileUri.launch(arrayOf("application/x-subrip")) }
            ) {
                Text(text = "Pick File")
            }

            Button(
                modifier = Modifier
                    .alpha(if (viewModel.subtitles.value.isEmpty()) 1f else 0f)
                    .wrapContentSize()
                    .requiredSize(if (viewModel.subtitles.value.isEmpty()) 166.dp else 0.dp),
                onClick = { navController.navigate(NavDest.Word.route) }
            ) {
                Text(text = "Show Words")
            }

            Button(
                modifier = Modifier
                    .alpha(if (viewModel.subtitles.value.isEmpty()) 1f else 0f)
                    .wrapContentSize()
                    .requiredSize(if (viewModel.subtitles.value.isEmpty()) 166.dp else 0.dp),
                onClick = { navController.navigate(NavDest.Test.route) }
            ) {
                Text(text = "Test")
            }
        }
    } else {
        Timber.d("subtitles is not empty")
        if (viewModel.isSessionRunning.value) {
            Timber.d("subtitles is not empty. isStartTimePicked true")
            ShowContent(viewModel)
        } else {
            Timber.d("subtitles is not empty. isStartTimePicked false")
            ShowStartTimePicker { hours, minutes, seconds ->
                viewModel.onStartTimePicked(hours, minutes, seconds)
            }
        }
    }
}

@Composable
private fun ShowContent(viewModel: HomeViewModel) {
    BoxWithConstraints {
        Timber.d("max width: $maxWidth")
        if (maxWidth > 400.dp) {
            Row {
                Box(modifier = Modifier.weight(1f)) {
                    ShowSubtitles(
                        viewModel.subtitles,
                        viewModel.translation,
                        viewModel.activeSubtitleIndex,
                        onClicked = { viewModel.onWordClicked(it) },
                        onSaveClicked = { source, translated ->
                            viewModel.onSaveWordClicked(source, translated)
                        }
                    )
                }

                Spacer(modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight())

                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(
                            text = "Translated Words",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth())
                        ShowTranslatedWords(translatedWords = viewModel.translatedWords)
                    }
                }
            }
        } else {
            ShowSubtitles(
                viewModel.subtitles,
                viewModel.translation,
                viewModel.activeSubtitleIndex,
                onClicked = { viewModel.onWordClicked(it) },
                onSaveClicked = { source, translated ->
                    viewModel.onSaveWordClicked(source, translated)
                }
            )
        }
    }
}

@Composable
private fun ShowStartTimePicker(onClicked: (String, String, String) -> Unit) {
    var hours by remember { mutableStateOf("0") }
    var minutes by remember { mutableStateOf("0") }
    var seconds by remember { mutableStateOf("0") }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp)
                .wrapContentSize()
                .imePadding()
        ) {
            TextField(
                value = hours,
                onValueChange = { hours = it },
                label = { Text("Hours") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            TextField(
                value = minutes,
                onValueChange = { minutes = it },
                label = { Text("Minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            TextField(
                value = seconds,
                onValueChange = { seconds = it },
                label = { Text("Seconds") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
                .requiredSize(88.dp),
            onClick = { onClicked(hours, minutes, seconds) }
        ) {
            Text(text = "Start")
        }
    }
}

@Composable
private fun ShowSubtitles(
    subtitles: State<List<Subtitle>>,
    translation: Flow<TranslationItem>,
    activeSubtitleIndex: Flow<ActiveSubtitle?>,
    onClicked: (TranslationItem) -> Unit,
    onSaveClicked: (String, String) -> Unit
) {
    val activeIndex = activeSubtitleIndex.collectAsState(initial = ActiveSubtitle(0, false))
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val minSpaceForSubtitle = 350.0
    val scrollOffset = LocalDensity.current.run {
        val heightPx = LocalConfiguration.current.screenHeightDp.dp.toPx()
        if (heightPx * 0.2 >= minSpaceForSubtitle) heightPx * 0.8
        else heightPx - minSpaceForSubtitle
    }

    Timber.d("offset: $scrollOffset")

    LaunchedEffect(key1 = activeIndex.value) {
        scope.launch {
            activeIndex.value?.let {
                if (it.withAnim) {
                    listState.animateScrollToItem(it.index, -scrollOffset.toInt())
                } else {
                    listState.scrollToItem(it.index, -scrollOffset.toInt())
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(subtitles.value) {index, subtitle ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (index == activeIndex.value?.index) Color.LightGray
                        else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                ShowSubtitle(
                    subtitle,
                    translation.map { if (it.id == subtitle.position) it else null},
                    onClicked = { onClicked(it) },
                    onSaveClicked = { str1, str2 -> onSaveClicked(str1, str2) }
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(12.dp))
        }
    }
}

@Composable
private fun ShowSubtitle(
    subtitle: Subtitle,
    translation: Flow<TranslationItem?>,
    onClicked: (TranslationItem) -> Unit,
    onSaveClicked: (String, String) -> Unit
) {
    val trans = translation.collectAsState(initial = null)
    val isClicked = remember { mutableStateOf(false) }
    val clickedText = remember { mutableStateOf("") }
    val translatedText = if (trans.value != null) {
        "${clickedText.value} = ${trans.value?.word}"
    } else ""
    val isSaved = remember { mutableStateOf(false) }

    isClicked.value = trans.value != null

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(if (trans.value == null) 1f else 0f)
            .padding(4.dp)
    ) {
        Text(
            color = Color.Blue,
            text = translatedText
        )
        Text(
            color = if (isSaved.value) Color.LightGray else Color.Green,
            text = if (isSaved.value) "Done" else "Save",
            modifier = Modifier
                .alpha(if (translatedText.isNotEmpty()) 1f else 0f)
                .clickable {
                    if (!isSaved.value && translatedText.isNotEmpty()) {
                        isSaved.value = true
                        trans.value?.word?.let {
                            if (it.isNotEmpty()) onSaveClicked(clickedText.value, it)
                        }
                    }
                }
        )
    }

    subtitle.textLines.forEach {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            it.split(" ").forEach { word ->
                Text(
                    color = if (isClicked.value && clickedText.value == word) Color.Blue else Color.Black,
                    text = word.removeSuffix("\n"),
                    modifier = Modifier
                        .clickable {
                            isClicked.value = true
                            clickedText.value = word
                            onClicked(TranslationItem(subtitle.position, word))
                        }
                        .padding(end = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ShowTranslatedWords(translatedWords: Flow<List<String>>) {
    val translatedWordsAsState = translatedWords.collectAsState(initial = emptyList())

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(translatedWordsAsState.value) {
            ShowTranslatedWord(word = it)
        }
    }
}

@Composable
private fun ShowTranslatedWord(word: String) {
    Text(
        text = word,
        color = GreenText,
        textAlign = TextAlign.Center
    )
}
