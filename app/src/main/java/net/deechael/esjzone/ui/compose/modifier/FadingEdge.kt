package net.deechael.esjzone.ui.compose.modifier

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.deechael.esjzone.ui.compose.local.LocalColorScheme

fun Modifier.horizontalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: LocalColorScheme.current.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromStart = scrollState.value
        val scrollFromEnd = scrollState.maxValue - scrollState.value

        val startFadingEdgeStrength = lengthValue * (scrollFromStart / lengthValue).coerceAtMost(1f)

        val endFadingEdgeStrength = lengthValue * (scrollFromEnd / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startX = 0f,
                endX = startFadingEdgeStrength,
            ),
            size = Size(
                startFadingEdgeStrength,
                this.size.height,
            ),
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startX = size.width - endFadingEdgeStrength,
                endX = size.width,
            ),
            topLeft = Offset(x = size.width - endFadingEdgeStrength, y = 0f),
        )
    }
}

fun Modifier.verticalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: LocalColorScheme.current.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromTop = scrollState.value
        val scrollFromBottom = scrollState.maxValue - scrollState.value

        val topFadingEdgeStrength = lengthValue * (scrollFromTop / lengthValue).coerceAtMost(1f)

        val bottomFadingEdgeStrength =
            lengthValue * (scrollFromBottom / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startY = 0f,
                endY = topFadingEdgeStrength,
            ),
            size = Size(
                this.size.width,
                topFadingEdgeStrength
            ),
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startY = size.height - bottomFadingEdgeStrength,
                endY = size.height,
            ),
            topLeft = Offset(x = 0f, y = size.height - bottomFadingEdgeStrength),
        )
    }
}

@Preview
@Composable
fun HorizontalPreview() {
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalFadingEdge(scrollState, 400.dp)
        ) {
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
            Text(text = "testaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }
    }
}

@Preview
@Composable
fun VerticalPreview() {
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalFadingEdge(scrollState, 200.dp)
        ) {
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
            Text(text = "test")
        }
    }
}