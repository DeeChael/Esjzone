package net.deechael.esjzone.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBar(
    title: String,
    divider: Boolean = true,
    onBack: () -> Unit = {},
    extraContent: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .size(50.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(3f))
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp
                )
            )
            Spacer(modifier = Modifier.weight(5f))
        }
        extraContent()
        if (divider) {
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    Surface {
        AppBar(title = "test")
    }
}

@Preview
@Composable
fun AppBarWithExtra() {
    Surface {
        AppBar(title = "test") {
            Row {
                DropdownSelection(
                    label = "AAAA",
                    items = listOf("Name", "test", "test111", "test222"),
                    current = "Name",
                    onChange = {},
                    exposed = false,
                    onExposeChanged = {},
                    modifier = Modifier.width(110.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                DropdownSelection(
                    label = "Selection",
                    items = listOf("Name", "test", "test111", "test222"),
                    current = "Name",
                    onChange = {},
                    exposed = false,
                    onExposeChanged = {},
                    modifier = Modifier.width(140.dp)
                )
            }
        }
    }
}