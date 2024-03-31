package net.deechael.esjzone.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getHistories
import net.deechael.esjzone.network.features.getNovelDetail
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.novellibrary.novel.DetailedNovel
import net.deechael.esjzone.novellibrary.novel.HistoryNovel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

object HistoryPage : Screen {

    private fun readResolve(): Any = HistoryPage

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val scope = rememberCoroutineScope()

        val historyPageModel = rememberScreenModel { HistoryPageModel(authorization, scope) }
        val state by historyPageModel.state.collectAsState()

        Column {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = {
                            navigator.pop()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                            .size(50.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.weight(3f))
                    Text(
                        text = stringResource(id = R.string.history),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 4.dp
                        )
                    )
                    Spacer(modifier = Modifier.weight(5f))
                }
                HorizontalDivider(thickness = 1.dp)
            }

            when (state) {
                is HistoryPageModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is HistoryPageModel.State.Result -> {
                    val result = state as HistoryPageModel.State.Result

                    val novels = result.historyNovels

                    val cache = remember {
                        mutableStateMapOf<String, DetailedNovel>()
                    }

                    val adult by remember {
                        GlobalSettings.adult
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(novels) { historyNovel ->
                            var detailedNovel: DetailedNovel? by remember {
                                mutableStateOf(cache[historyNovel.url])
                            }

                            val historyChapter: MutableState<Chapter?> = rememberSaveable {
                                mutableStateOf(historyNovel.chapter)
                            }

                            val rememberedHistory by rememberSaveable {
                                historyChapter
                            }

                            if (detailedNovel == null) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                }

                                LaunchedEffect(currentCompositeKeyHash) {
                                    scope.launch(Dispatchers.IO) {
                                        detailedNovel = EsjzoneClient.getNovelDetail(
                                            authorization,
                                            historyNovel
                                        )
                                        cache[historyNovel.url] = detailedNovel!!
                                    }
                                }
                            } else {
                                if (adult || !detailedNovel!!.isAdult) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 8.dp, top = 4.dp, end = 8.dp)
                                            .clickable {
                                                navigator.push(
                                                    NovelPage(
                                                        historyNovel,
                                                        historyChapter
                                                    )
                                                )
                                            }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            SubcomposeAsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(detailedNovel!!.cover)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = historyNovel.name,
                                                imageLoader = MainActivity.imageLoader,
                                                loading = {
                                                    CircularProgressIndicator()
                                                },
                                                contentScale = ContentScale.FillHeight,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .height((configuration.screenHeightDp / 10).dp)
                                                    .width((configuration.screenWidthDp / 7).dp)
                                            )
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = detailedNovel!!.name,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                                Text(
                                                    text = historyChapter.value!!.name,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontSize = 10.sp,
                                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                                )
                                            }
                                            TextButton(
                                                onClick = {
                                                    navigator.push(
                                                        ChapterPage(
                                                            detailedNovel!!.id(),
                                                            rememberedHistory!!,
                                                            historyChapter
                                                        )
                                                    )
                                                },
                                                modifier = Modifier.padding(8.dp)
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.PlayArrow,
                                                        contentDescription = ""
                                                    )
                                                    Text(text = stringResource(id = R.string.continue_reading))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = stringResource(id = R.string.the_end),
                                    modifier = Modifier.padding(16.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            historyPageModel.getNovels()
        }
    }

}

class HistoryPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope
) : StateScreenModel<HistoryPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(val historyNovels: List<HistoryNovel>) : State()
    }

    fun getNovels() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value = State.Result(EsjzoneClient.getHistories(authorization))
        }
    }

}