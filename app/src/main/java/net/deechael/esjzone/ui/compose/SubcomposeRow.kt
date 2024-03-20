package net.deechael.esjzone.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

@Composable
fun SubcomposeRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {

    SubcomposeLayout(modifier = modifier) { constraints ->
        var recompositionIndex = 0

        var placeables: List<Placeable> = subcompose(recompositionIndex++, content).map {
            it.measure(constraints)
        }

        var rowSize =
            placeables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = currentMax.width + placeable.width,
                    height = maxOf(currentMax.height, placeable.height)
                )
            }

        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(recompositionIndex, content).map { measurable: Measurable ->
                measurable.measure(
                    Constraints(
                        minHeight = rowSize.height,
                        maxHeight = constraints.maxHeight
                    )
                )
            }

            rowSize =
                placeables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                    IntSize(
                        width = currentMax.width + placeable.width,
                        height = maxOf(currentMax.height, placeable.height)
                    )
                }
        }

        layout(rowSize.width, rowSize.height) {
            var xPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(xPos, 0)
                xPos += placeable.width
            }

        }
    }
}