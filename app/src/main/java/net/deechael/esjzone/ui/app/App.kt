package net.deechael.esjzone.ui.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import net.deechael.esjzone.ui.navigation.LocalAppNavigator
import net.deechael.esjzone.ui.screen.LoadingScreen

@Composable
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Navigator(
            LoadingScreen()
        ) { navigator ->
            SlideTransition(navigator = navigator) { screen ->
                CompositionLocalProvider(value = LocalAppNavigator provides navigator) {
                    screen.Content()
                }
            }
        }
    }
}