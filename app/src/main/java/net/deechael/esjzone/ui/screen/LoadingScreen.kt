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

                if (!MainActivity.database.cacheDao().exists("ews_key")) {
                    MainActivity.database.cacheDao().insertNotExists(
                        Cache(
                            key = "ews_key",
                            value = "null"
                        )
                    )
                }

                if (!MainActivity.database.cacheDao().exists("ews_token")) {
                    MainActivity.database.cacheDao().insertNotExists(
                        Cache(
                            key = "ews_token",
                            value = "null"
                        )
                    )
                }

                val ewsKey = MainActivity.database.cacheDao().findByKey("ews_key")
                val ewsToken = MainActivity.database.cacheDao().findByKey("ews_token")

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