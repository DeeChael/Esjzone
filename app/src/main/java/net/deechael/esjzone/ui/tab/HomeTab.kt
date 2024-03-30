package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getHomeData
import net.deechael.esjzone.novellibrary.data.HomeData
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import net.deechael.esjzone.ui.compose.SubcomposeRow
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.NovelListPage
import net.deechael.esjzone.ui.page.NovelPage

object HomeTab : Tab {

    private fun readResolve(): Any = HomeTab

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = stringResource(id = R.string.screen_main_tab_home),
            icon = rememberVectorPainter(image = Icons.Filled.Home)
        )

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val scope = rememberCoroutineScope()

        var homeData: HomeData? by remember {
            mutableStateOf(null)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 8.0.em,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    bottom = 16.dp
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.clickable {
                        navigator.push(NovelListPage(1, 1, false))
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.tab_home_recentlyupdate_tranlated),
                        fontSize = 6.em,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                if (homeData == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    NovelSets(novels = homeData!!.recentlyUpdateTranslated)
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.clickable {
                        navigator.push(NovelListPage(2, 1, false))
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.tab_home_recentlyupdate_original),
                        fontSize = 6.em,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                if (homeData == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    NovelSets(novels = homeData!!.recentlyUpdateOriginal)
                }

            }

            val adult by remember {
                GlobalSettings.adult
            }

            if (adult) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            navigator.push(NovelListPage(1, 1, true))
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.tab_home_recentlyupdate_tranlated_r18),
                            fontSize = 6.em,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }

                    if (homeData == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        NovelSets(novels = homeData!!.recentlyUpdateTranslatedR18)
                    }

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            navigator.push(NovelListPage(2, 1, true))
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.tab_home_recentlyupdate_original_r18),
                            fontSize = 6.em,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    }

                    if (homeData == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        NovelSets(novels = homeData!!.recentlyUpdateOriginalR18)
                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.tab_home_recommendation),
                        fontSize = 6.em,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }

                if (homeData == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    NovelSets(novels = homeData!!.recommendation)
                }

            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        LaunchedEffect(currentCompositeKeyHash) {
            scope.launch(Dispatchers.IO) {
                homeData = EsjzoneClient.getHomeData(authorization)
            }
        }
    }

}

@Composable
fun NovelSets(novels: List<CoveredNovel>) {
    val configuration = LocalConfiguration.current
    val navigator = LocalBaseNavigator.current

    val adult by remember {
        GlobalSettings.adult
    }

    val finalNovels = novels.filter {
        !(it.isAdult && !adult)
    }.toList()

    SubcomposeRow(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        val length = finalNovels.size
        for (index in 0 until length) {
            val novel = finalNovels[index]
            Card(
                modifier = Modifier
                    .widthIn(max = (configuration.screenWidthDp / 2).dp)
                    .clickable {
                        navigator.push(NovelPage(novel))
                    }
            ) {
                Column {
                    Column {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(novel.coverUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = novel.name,
                            imageLoader = MainActivity.imageLoader,
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(8.dp)
                                .height((configuration.screenHeightDp / 3.5).dp)
                        )
                        Text(
                            text = novel.name,
                            maxLines = 2,
                            minLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 4.dp,
                                    start = 8.dp,
                                    end = 4.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RemoveRedEye,
                                contentDescription = ""
                            )
                            Text(
                                text = "${novel.views}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 3.em,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    bottom = 4.dp,
                                    start = 4.dp,
                                    end = 8.dp
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                contentDescription = ""
                            )
                            Text(
                                text = "${novel.likes}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 3.em,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            if (index != (length - 1)) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun FooterPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(text = "Powered by ")
        Image(
            painter = painterResource(id = R.drawable.jetpack_compose_xxx_high),
            contentScale = ContentScale.Inside,
            contentDescription = "jetpack compose",
            modifier = Modifier.height(20.dp)
        )
        Text(text = "Jetpack Compose", fontWeight = FontWeight.Bold)
    }
}