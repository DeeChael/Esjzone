package net.deechael.esjzone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.deechael.esjzone.app.App
import net.deechael.esjzone.database.GeneralDatabase
import net.deechael.esjzone.ui.theme.catppuccin.latte.CatppuccinLatteYellowTheme

class MainActivity : ComponentActivity() {

    companion object {

        lateinit var database: GeneralDatabase

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CatppuccinLatteYellowTheme {
                App()
            }
        }
    }

}