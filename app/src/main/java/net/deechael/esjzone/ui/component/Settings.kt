package net.deechael.esjzone.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Mediation
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.deechael.esjzone.ui.theme.catppuccin.CatppuccinThemeType

@Composable
fun SettingsColumn(
    modifier: Modifier = Modifier,
    content: @Composable SettingsScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        content(SettingsScopeImpl())
    }
}

@Composable
fun SettingsScope.SettingsText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 4.dp, start = 12.dp, bottom = 4.dp),
        fontSize = 16.sp
    )
}

@Composable
fun SettingsScope.SettingsButton(imageVector: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SettingsScope.SettingsSwitch(
    imageVector: ImageVector,
    text: String,
    checked: Boolean,
    onSwitch: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onSwitch,
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp, end = 16.dp)
                .scale(0.9f),
        )
    }
}

@Composable
fun SettingsScope.SettingsCustom(
    imageVector: ImageVector,
    text: String,
    content: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        Spacer(modifier = Modifier.width(40.dp))
        Column {
            content()
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

internal class SettingsScopeImpl : SettingsScope

interface SettingsScope


@Preview
@Composable
fun SettingsColumnPreview() {
    Surface {
        SettingsColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsText(text = "Some Category")
            SettingsButton(imageVector = Icons.Filled.Mediation, text = "Test")
            SettingsButton(imageVector = Icons.Filled.Menu, text = "Another")
            SettingsSwitch(imageVector = Icons.Filled.Category, text = "Switch Test", true) {}
            SettingsSwitch(imageVector = Icons.Filled.AudioFile, text = "Turned off", false) {}
            SettingsText(text = "Some Category")
            SettingsButton(imageVector = Icons.Filled.Mediation, text = "Test")
            SettingsButton(imageVector = Icons.Filled.Menu, text = "Another")
            SettingsCustom(imageVector = Icons.Filled.ColorLens, text = "Colors") {

                Text(
                    text = "Frappe",
                    fontSize = 16.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    for (type in CatppuccinThemeType.frappes()) {
                        Card(
                            modifier = Modifier
                                .width(60.dp)
                                .height(40.dp)
                                .padding(4.dp),
                            colors = CardColors(
                                containerColor = type.baseColor,
                                contentColor = Color.White,
                                disabledContainerColor = type.baseColor,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Frappe",
                    fontSize = 16.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    for (type in CatppuccinThemeType.frappes()) {
                        Card(
                            modifier = Modifier
                                .width(60.dp)
                                .height(40.dp)
                                .padding(4.dp),
                            colors = CardColors(
                                containerColor = type.baseColor,
                                contentColor = Color.White,
                                disabledContainerColor = type.baseColor,
                                disabledContentColor = Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
            }
            SettingsButton(imageVector = Icons.Filled.Mediation, text = "Test")
        }
    }
}