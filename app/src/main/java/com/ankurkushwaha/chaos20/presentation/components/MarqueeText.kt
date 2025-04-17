package com.ankurkushwaha.chaos20.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun MarqueeText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    color: Color = Color.White,
    fontWeight: FontWeight = FontWeight.Bold,
    scrollSpeed: Int = 30 // Lower is faster
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = fontSize,
        color = color,
        fontWeight = fontWeight
    )

    var containerWidth by remember { mutableStateOf(0) }
    var textWidth by remember { mutableStateOf(0) }

    // Measure text width
    LaunchedEffect(text, textStyle) {
        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(text),
            style = textStyle
        )
        textWidth = textLayoutResult.size.width
    }

    // Create scrolling animation
    val scrollPosition = remember { Animatable(0f) }
    LaunchedEffect(textWidth, containerWidth) {
        if (textWidth > 0 && containerWidth > 0) {
            // Reset to start position
            scrollPosition.snapTo(containerWidth.toFloat())

            // Start infinite scrolling animation
            scrollPosition.animateTo(
                targetValue = -textWidth.toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = (textWidth + containerWidth) * scrollSpeed,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                containerWidth = size.width
            }
            .clipToBounds(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.offset(x = with(LocalDensity.current) { scrollPosition.value.toDp() })
        )
    }
}