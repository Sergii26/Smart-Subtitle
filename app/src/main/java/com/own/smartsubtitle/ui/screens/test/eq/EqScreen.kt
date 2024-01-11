@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.own.smartsubtitle.ui.screens.test.eq


import android.graphics.PointF
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.own.smartsubtitle.ui.theme.SmartSubtitleTheme


@Composable
fun EqScreen(
    viewModel: EqViewModel = hiltViewModel()
) {
    val presets = viewModel.presets.value

    val pagerState = rememberPagerState(pageCount = { presets.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) { page ->
            // Our page content
            ShowPreset(preset = presets[page])
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Composable
private fun ShowPreset(preset: Preset) {
    var bassSlider by remember { mutableFloatStateOf(preset.bass.toFloat()) }
    var midBassSlider by remember { mutableFloatStateOf(preset.midBass.toFloat()) }
    var midSlider by remember { mutableFloatStateOf(preset.mid.toFloat()) }
    var midTrebleSlider by remember { mutableFloatStateOf(preset.midTreble.toFloat()) }
    var trebleSlider by remember { mutableFloatStateOf(preset.treble.toFloat()) }

    val dd = Dp(8f)
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = preset.name)

        Box {
            DrawPath(
                listOf(
                    Balance(0, bassSlider.toInt().toFloat()),
                    Balance(1, midBassSlider.toInt().toFloat()),
                    Balance(2, midSlider.toInt().toFloat()),
                    Balance(3, midTrebleSlider.toInt().toFloat()),
                    Balance(4, trebleSlider.toInt().toFloat())
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                VerticalSlider(value = bassSlider) {
                    bassSlider = it
                }

                VerticalSlider(value = midBassSlider) {
                    midBassSlider = it
                }

                VerticalSlider(value = midSlider) {
                    midSlider = it
                }

                VerticalSlider(value = midTrebleSlider) {
                    midTrebleSlider = it
                }

                VerticalSlider(value = trebleSlider) {
                    trebleSlider = it
                }
            }
        }


    }
}
@Composable
private fun VerticalSlider(value: Float, onValueChanged: (Float) -> Unit) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val showValueMark = remember { mutableStateOf(false) }

    Slider(
        value = value,
        onValueChange = {
            if (!showValueMark.value) showValueMark.value = true
            onValueChanged(it)
        },
        onValueChangeFinished = {
            showValueMark.value = false
        },
        steps = 256,
        valueRange = 0f..255f,
        interactionSource = mutableInteractionSource,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.Gray,
            inactiveTrackColor = Color.Gray,
        ),
        track = { sliderPositions ->
            SliderDefaults.Track(
                colors = SliderDefaults.colors(
                    activeTrackColor = Color(0xFFD1D1D1),
                    inactiveTrackColor = Color(0xFFD1D1D1),
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                ),
                enabled = true,
                sliderPositions = sliderPositions,
                modifier = Modifier
                    .scale(scaleX = 1f, scaleY = 0.3f),
            )
        },
        thumb = {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value.toInt().toString(),
                    modifier = Modifier
                        .alpha(if (showValueMark.value) 1f else 0f)
                        .height(70.dp)
                        .rotate(90f)
                        .width(30.dp),
                    textAlign = TextAlign.Center
                )
                SliderDefaults.Thumb(
                    interactionSource = mutableInteractionSource,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White
                    )
                )
            }



        },
        modifier = Modifier
            .graphicsLayer {
                rotationZ = 270f
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(
                    Constraints(
                        minWidth = constraints.minHeight,
                        maxWidth = constraints.maxHeight,
                        minHeight = constraints.minWidth,
                        maxHeight = constraints.maxWidth,
                    )
                )
                layout(placeable.height, placeable.width) {
                    placeable.place(-placeable.width, 0)
                }
            }
    )
}

@Composable
private fun DrawPath(graphData: List<Balance>) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(vertical = 13.dp, horizontal = 34.dp)
            .fillMaxSize()
    ){
        Spacer(
            modifier = Modifier
            .fillMaxSize()
                .drawWithCache {
                    val path = generateSmoothPath(graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.lineTo(size.width, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()
                    onDrawBehind {
                        drawPath(path, Color.Red, style = Stroke(2.dp.toPx()))
                        drawPath(
                            filledPath,
                            Brush.verticalGradient(
                                listOf(Color.Gray.copy(alpha = 0.4f), Color.Gray.copy(alpha = 0.1f))
                            ),
                            style = Fill
                        )
                    }
                }
        )
    }
}

fun generateSmoothPath(data: List<Balance>, size: Size): Path {
    val path = Path()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries
    val range = 256f
    val heightPxPerAmount = size.height / range

    var previousBalanceX = 0f
    var previousBalanceY = size.height

    data.forEachIndexed { index, balance ->
        if (index == 0) {
            path.moveTo(
                0f,
                size.height - (balance.value) * heightPxPerAmount
            )
            previousBalanceX = 0f
            previousBalanceY = size.height - (balance.value) * heightPxPerAmount
        } else {
            val balanceX = index * weekWidth
            val balanceY = size.height - (balance.value) * heightPxPerAmount
            val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
            val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)

            path.cubicTo(
                controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
                balanceX, balanceY
            )

            previousBalanceX = balanceX
            previousBalanceY = balanceY
        }
    }

    return path
}

data class Balance(val column: Int, val value: Float)

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun ShowPresetsPreview() {
    SmartSubtitleTheme {
        Surface {
            ShowPreset(preset = Preset("Preview", 0, 255, 255, 50, 50))
        }
    }
}

