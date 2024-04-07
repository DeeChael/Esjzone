package net.deechael.esjzone.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NoAdultContent
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import net.deechael.esjzone.ui.component.AppBar
import net.deechael.esjzone.ui.component.SettingsButton
import net.deechael.esjzone.ui.component.SettingsColumn
import net.deechael.esjzone.ui.component.SettingsCustom
import net.deechael.esjzone.ui.component.SettingsSwitch
import net.deechael.esjzone.ui.component.SettingsText
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
            AppBar(
                title = stringResource(id = R.string.settings),
                onBack = {
                    navigator.pop()
                }
            )

            SettingsColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SettingsText(text = stringResource(id = R.string.settings_category_preference))
                SettingsCustom(
                    imageVector = Icons.Filled.ColorLens,
                    text = stringResource(id = R.string.settings_theme)
                ) {
                    var theme by remember {
                        GlobalSettings.theme
                    }

                    Text(
                        text = "Frappe",
                        modifier = Modifier.padding(start = 16.dp),
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
                            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
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
                            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
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
                            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
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

                SettingsText(text = stringResource(id = R.string.settings_category_content))
                SettingsSwitch(
                    imageVector = Icons.Filled.NoAdultContent,
                    text = stringResource(id = R.string.settings_showadultcontent),
                    checked = adult
                ) {
                    adult = it
                    scope.launch(Dispatchers.IO) {
                        val dao = MainActivity.database.cacheDao()
                        val showAdult = dao.findByKey("show_adult")
                        showAdult.value = it.toString()
                        dao.update(showAdult)
                    }
                }

                SettingsText(text = stringResource(id = R.string.settings_category_app))
                SettingsButton(
                    imageVector = Icons.Filled.Info,
                    text = stringResource(id = R.string.about)
                ) {
                    navigator.push(AboutPage)
                }
                SettingsButton(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    text = stringResource(id = R.string.button_logout)
                ) {
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
            }
        }
    }

}