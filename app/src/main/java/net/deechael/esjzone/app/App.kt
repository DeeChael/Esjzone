package net.deechael.esjzone.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import net.deechael.esjzone.ui.screen.LoadingScreen

@Composable
fun App(modifier: Modifier) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Navigator(
            LoadingScreen()
        ) { navigator ->
            SlideTransition(navigator = navigator)
        }
    }
}