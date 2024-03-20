package net.deechael.esjzone.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.ui.tab.CategoryTab
import net.deechael.esjzone.ui.tab.HomeTab
import net.deechael.esjzone.ui.tab.ProfileTab
import net.deechael.esjzone.ui.tab.SearchTab

class MainScreen(val authorization: Authorization) : Screen {

    @Composable
    override fun Content() {
        TabNavigator(tab = HomeTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(tab = HomeTab)
                        TabNavigationItem(tab = CategoryTab)
                        TabNavigationItem(tab = SearchTab)
                        TabNavigationItem(tab = ProfileTab)
                    }
                }
            ) {
                CompositionLocalProvider(value = LocalAuthorization provides authorization) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }

}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            if (tab.options.icon != null)
                Icon(painter = tab.options.icon!!, contentDescription = tab.options.title)
        }
    )
}