package net.deechael.esjzone.ui.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NovelPage() {

}

@Preview
@Composable
fun DescriptionPreview() {
    var visible by remember {
        mutableStateOf(false)
    }
    val topFade = Brush.verticalGradient(0f to Color.Red, 1f to Color.Transparent)
    Card(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.clickable { visible = !visible }
        ) {
            AnimatedVisibility(visible = visible) {
                Box(
                    modifier = Modifier
                        .fadingEdge(topFade)
                        .background(Color.Transparent)
                ) {
                    Text(text = "test\nadada\nadqeqeq\nadasdaqeqwwe\nadsasdqweq")
                }
            }
            AnimatedVisibility(visible = !visible) {
                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                ) {
                    Text(text = "test\nadada\nadqeqeq\nadasdaqeqwwe\nadsasdqweq\nadada\nadqeqeq\nadasdaqeqwwe\nadsasdqweq")
                }
            }
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }