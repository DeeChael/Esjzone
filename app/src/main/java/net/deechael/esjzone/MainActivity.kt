package net.deechael.esjzone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import net.deechael.esjzone.database.GeneralDatabase
import net.deechael.esjzone.ui.app.App
import net.deechael.esjzone.ui.theme.catppuccin.latte.CatppuccinLatteYellowTheme

class MainActivity : ComponentActivity() {

    companion object {

        lateinit var database: GeneralDatabase
        lateinit var imageLoader: ImageLoader

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageLoader = ImageLoader.Builder(this)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
            .build()


        enableEdgeToEdge()
        setContent {
            CatppuccinLatteYellowTheme {
                App()
            }
        }
    }

}