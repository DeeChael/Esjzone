package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.R
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getUserProfile
import net.deechael.esjzone.novellibrary.user.UserProfile

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
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = data!!.name,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        LaunchedEffect(currentCompositeKeyHash) {
            scope.launch(Dispatchers.IO) {
                data = EsjzoneClient.getUserProfile(authorization)
            }
        }
    }

}