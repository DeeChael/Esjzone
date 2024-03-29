package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import net.deechael.esjzone.novellibrary.user.UserProfile
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.SettingsPage

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
        val navigator = LocalBaseNavigator.current

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
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
            }
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                            .clickable {
                                // TODO: push to Favorites page
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(50.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.favorites),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                            .clickable {
                                // TODO: push to History page
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(50.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.history),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                            .clickable {
                                navigator.push(SettingsPage)
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(50.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.settings),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 20.sp
                            )
                        }
                    }
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