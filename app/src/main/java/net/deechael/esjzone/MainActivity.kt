package net.deechael.esjzone

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.database.GeneralDatabase
import net.deechael.esjzone.database.entity.Cache
import net.deechael.esjzone.ui.app.App
import net.deechael.esjzone.ui.theme.catppuccin.CatppuccinDynamicTheme
import net.deechael.esjzone.ui.theme.catppuccin.CatppuccinThemeType

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

        lifecycleScope.launch(Dispatchers.IO) {
            database = Room.databaseBuilder(
                this@MainActivity,
                GeneralDatabase::class.java, "general"
            ).build()

            val dao = database.cacheDao()

            if (!dao.exists("theme")) {
                dao.insertNotExists(
                    Cache(
                        key = "theme",
                        value = GlobalSettings.theme.value.name
                    )
                )
            }

            GlobalSettings.theme.value = CatppuccinThemeType.valueOf(dao.findByKey("theme").value)

            this.launch(Dispatchers.Main) {
                setContent {
                    CatppuccinDynamicTheme {
                        App()
                    }
                }
            }
        }
    }

}