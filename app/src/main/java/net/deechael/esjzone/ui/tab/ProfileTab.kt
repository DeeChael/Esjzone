package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getUserProfile
import net.deechael.esjzone.network.features.logout
import net.deechael.esjzone.novellibrary.user.UserProfile
import net.deechael.esjzone.ui.navigation.LocalAppNavigator
import net.deechael.esjzone.ui.screen.LoginScreen

object ProfileTab : Tab {

    private fun readResolve(): Any = ProfileTab

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 3u,
            title = stringResource(id = R.string.screen_main_tab_profile),
            icon = rememberVectorPainter(image = Icons.Filled.Person)
        )

    @Composable
    override fun Content() {
        val appNavigator = LocalAppNavigator.current

        val configuration = LocalConfiguration.current
        val authorization = LocalAuthorization.current

        var data: UserProfile? by remember {
            mutableStateOf(null)
        }

        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (data == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.esjzone.me${data!!.avatarUrl}")
                        .crossfade(true)
                        .build(),
                    contentDescription = data!!.name,
                    imageLoader = MainActivity.imageLoader,
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .height((configuration.screenWidthDp / 4).dp)
                        .width((configuration.screenWidthDp / 4).dp)
                )
                Text(
                    text = data!!.name,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            EsjzoneClient.logout(authorization)
                            val dao = MainActivity.database.cacheDao()
                            dao.delete(*dao.getAll().toTypedArray())
                        }
                        appNavigator.replaceAll(LoginScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.button_logout),
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            scope.launch(Dispatchers.IO) {
                data = EsjzoneClient.getUserProfile(authorization)
            }
        }
    }

}