package net.deechael.esjzone.ui.page

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NoAdultContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.logout
import net.deechael.esjzone.ui.navigation.LocalAppNavigator
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.screen.LoginScreen
import net.deechael.esjzone.ui.theme.catppuccin.CatppuccinThemeType

object SettingsPage : Screen {

    private fun readResolve(): Any = SettingsPage

    @Composable
    override fun Content() {
        val appNavigator = LocalAppNavigator.current
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val scope = rememberCoroutineScope()

        var adult by remember {
            GlobalSettings.adult
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = {
                            navigator.pop()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                            .size(50.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.weight(3f))
                    Text(
                        text = stringResource(id = R.string.settings),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 4.dp
                        )
                    )
                    Spacer(modifier = Modifier.weight(5f))
                }
                HorizontalDivider(thickness = 1.dp)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.settings_category_preference),
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 4.dp),
                    fontSize = 20.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    var theme by remember {
                        GlobalSettings.theme
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ColorLens,
                            contentDescription = "",
                            modifier = Modifier.padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp,
                                end = 16.dp
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.settings_theme),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp
                        )
                    }

                    Text(
                        text = "Frappe",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        for (type in CatppuccinThemeType.frappes()) {
                            Card(
                                modifier = Modifier
                                    .width((configuration.screenWidthDp / 5).dp)
                                    .height(((configuration.screenWidthDp / 5) * 0.65).dp)
                                    .padding(4.dp)
                                    .clickable {
                                        scope.launch(Dispatchers.IO) {
                                            val dao = MainActivity.database.cacheDao()

                                            val themeCache = dao.findByKey("theme")
                                            themeCache.value = type.name

                                            dao.update(themeCache)
                                        }
                                        theme = type
                                    },
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
                                    if (type == theme) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "Latte",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        for (type in CatppuccinThemeType.lattes()) {
                            Card(
                                modifier = Modifier
                                    .width((configuration.screenWidthDp / 5).dp)
                                    .height(((configuration.screenWidthDp / 5) * 0.65).dp)
                                    .padding(4.dp)
                                    .clickable {
                                        scope.launch(Dispatchers.IO) {
                                            val dao = MainActivity.database.cacheDao()

                                            val themeCache = dao.findByKey("theme")
                                            themeCache.value = type.name

                                            dao.update(themeCache)
                                            theme = type
                                        }
                                    },
                                colors = CardColors(
                                    containerColor = type.baseColor,
                                    contentColor = Color.White,
                                    disabledContainerColor = type.baseColor,
                                    disabledContentColor = Color.White
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (type == theme) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "Macchiato",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        for (type in CatppuccinThemeType.macchiatos()) {
                            Card(
                                modifier = Modifier
                                    .width((configuration.screenWidthDp / 5).dp)
                                    .height(((configuration.screenWidthDp / 5) * 0.65).dp)
                                    .padding(4.dp)
                                    .clickable {
                                        scope.launch(Dispatchers.IO) {
                                            val dao = MainActivity.database.cacheDao()

                                            val themeCache = dao.findByKey("theme")
                                            themeCache.value = type.name

                                            dao.update(themeCache)
                                        }
                                        theme = type
                                    },
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
                                    if (type == theme) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "Mocha",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 16.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        for (type in CatppuccinThemeType.mochas()) {
                            Card(
                                modifier = Modifier
                                    .width((configuration.screenWidthDp / 5).dp)
                                    .height(((configuration.screenWidthDp / 5) * 0.65).dp)
                                    .padding(4.dp)
                                    .clickable {
                                        scope.launch(Dispatchers.IO) {
                                            val dao = MainActivity.database.cacheDao()

                                            val themeCache = dao.findByKey("theme")
                                            themeCache.value = type.name

                                            dao.update(themeCache)
                                            theme = type
                                        }
                                    },
                                colors = CardColors(
                                    containerColor = type.baseColor,
                                    contentColor = Color.White,
                                    disabledContainerColor = type.baseColor,
                                    disabledContentColor = Color.White
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (type == theme) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.settings_category_content),
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 4.dp),
                    fontSize = 20.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.NoAdultContent,
                            contentDescription = "",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.settings_showadultcontent),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = adult,
                            onCheckedChange = {
                                adult = it
                                scope.launch(Dispatchers.IO) {
                                    val dao = MainActivity.database.cacheDao()
                                    val showAdult = dao.findByKey("show_adult")
                                    showAdult.value = it.toString()
                                    dao.update(showAdult)
                                }
                            },
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 4.dp, end = 16.dp)
                                .scale(0.9f),
                        )
                    }
                }

                Text(
                    text = stringResource(id = R.string.settings_category_app),
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, bottom = 4.dp),
                    fontSize = 20.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                        .clickable {
                            navigator.push(AboutPage)
                        }
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.about),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                        .clickable {
                            scope.launch(Dispatchers.IO) {
                                EsjzoneClient.logout(authorization)
                                val dao = MainActivity.database.cacheDao()
                                dao.delete(
                                    dao.findByKey("ews_key"),
                                    dao.findByKey("ews_token"),
                                )
                            }
                            appNavigator.replaceAll(LoginScreen)
                        }
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "",
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.button_logout),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

}