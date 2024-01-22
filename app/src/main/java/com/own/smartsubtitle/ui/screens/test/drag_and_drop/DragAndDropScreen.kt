@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.own.smartsubtitle.ui.screens.test.drag_and_drop


import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.own.smartsubtitle.ui.theme.SmartSubtitleTheme


@Composable
fun DragAndDropScreen(
    viewModel: DragAndDropViewModel = hiltViewModel()
) {
    ShowDrawer(items = viewModel.drawerItems.value)
}

@Composable
private fun ShowDrawer(items: List<Item>) {
    LazyRow {
        items(items) {
            ShowDrawerItem(item = it)
        }
    }
}

@Composable
private fun ShowDrawerItem(item: Item) {

    Canvas(
        modifier = Modifier
            .height(80.dp)
            .width(40.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDragEnd = {}
                ) { change, dragAmount ->  }
            }
    ) {
        drawRect(
            color = Color(item.color),
            size = Size(width = 30.dp.toPx(), height = 70.dp.toPx()),
            topLeft = Offset(5.dp.toPx(), 5.dp.toPx()),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x010101)
@Composable
fun ShowDrawerPreview() {
    ShowDrawer(items = listOf(
        Item("1", "Item 1", 0xFFF32FFF),
        Item("2", "Item 2", 0xFF44F0F0),
        Item("3", "Item 3", 0xFF127575),
        Item("4", "Item 4", 0xFF1F1616),
        Item("5", "Item 5", 0xFF8A9797)
    ))
}

