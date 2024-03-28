package net.deechael.esjzone.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.database.GeneralDatabase
import net.deechael.esjzone.database.entity.Cache
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.features.isAuthorized

class LoadingScreen : Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        LaunchedEffect(currentCompositeKeyHash) {
            launch(Dispatchers.IO) {
                MainActivity.database = Room.databaseBuilder(
                    context,
                    GeneralDatabase::class.java, "general"
                ).build()

                val dao = MainActivity.database.cacheDao()

                if (!dao.exists("ews_key")) {
                    dao.insertNotExists(
                        Cache(
                            key = "ews_key",
                            value = "null"
                        )
                    )
                }

                if (!dao.exists("ews_token")) {
                    dao.insertNotExists(
                        Cache(
                            key = "ews_token",
                            value = "null"
                        )
                    )
                }

                if (!dao.exists("show_adult")) {
                    dao.insertNotExists(
                        Cache(
                            key = "show_adult",
                            value = "false"
                        )
                    )
                }

                val ewsKey = dao.findByKey("ews_key")
                val ewsToken = dao.findByKey("ews_token")

                GlobalSettings.adult.value = dao.findByKey("show_adult").value.toBooleanStrictOrNull() ?: false

                val authorization = Authorization(ewsKey.value, ewsToken.value)

                if (EsjzoneClient.isAuthorized(authorization = authorization)) {
                    navigator.replace(MainScreen(authorization = authorization))
                } else {
                    navigator.replace(LoginScreen)
                }
            }
        }
    }

}