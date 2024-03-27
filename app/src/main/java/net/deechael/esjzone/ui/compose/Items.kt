package net.deechael.esjzone.ui.compose

import androidx.compose.runtime.Composable

@Composable
fun <T> Items(
    items: List<T>,
    divider: @Composable () -> Unit,
    renderer: @Composable (T) -> Unit
) {
    val max = items.size
    for (i in items.indices) {
        renderer(items[i])
        if (i != max - 1)
            divider()
    }
}