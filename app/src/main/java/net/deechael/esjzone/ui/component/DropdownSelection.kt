package net.deechael.esjzone.ui.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelection(
    label: String,
    items: List<T>,
    current: T,
    onChange: (T) -> Unit,
    exposed: Boolean,
    onExposeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    nameProvider: @Composable T.() -> String = { toString() }
) {
    ExposedDropdownMenuBox(
        expanded = exposed,
        onExpandedChange = onExposeChanged
    ) {
        TextField(
            readOnly = true,
            value = current.nameProvider(),
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = exposed)
            },
            modifier = modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = exposed,
            onDismissRequest = {
                onExposeChanged(false)
            }
        ) {
            for (item in items) {
                DropdownMenuItem(
                    text = {
                        Text(text = item.nameProvider())
                    },
                    onClick = {
                        onChange(item)
                        onExposeChanged(false)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DropdownSelectionPreview() {
    DropdownSelection(
        label = "Selection",
        items = listOf("Name", "test", "test111", "test222"),
        current = "Name",
        onChange = {},
        exposed = false,
        onExposeChanged = {}
    )
}

@Preview
@Composable
fun DropdownSelectionPreviewExposed() {
    DropdownSelection(
        label = "Selection",
        items = listOf("Name", "test", "test111", "test222"),
        current = "Name",
        onChange = {},
        exposed = true,
        onExposeChanged = {}
    )
}