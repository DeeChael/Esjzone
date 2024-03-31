package net.deechael.esjzone.ui.page

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.changeFavorites
import net.deechael.esjzone.network.features.getNovelDetail
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.novellibrary.novel.DetailedNovel
import net.deechael.esjzone.novellibrary.novel.Novel
import net.deechael.esjzone.ui.component.ChapterList
import net.deechael.esjzone.ui.component.Description
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

class NovelPage(
    private val novel: Novel,
    private val history: MutableState<Chapter?> = mutableStateOf(null),
    private val favorite: MutableState<Boolean> = mutableStateOf(false)
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val scope = rememberCoroutineScope()

        val screenModel = rememberScreenModel { NovelPageModel(authorization, scope, novel) }
        val state by screenModel.state.collectAsState()

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
                        text = novel.name,
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
                is NovelPageModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is NovelPageModel.State.Result -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    val result = state as NovelPageModel.State.Result

                    val chapterList = result.detailed.chapterList

                    val history = rememberSaveable {
                        // Saveable, to prevent a situation that you pop from chapter page but the history chapter didn't updated so that if you click continue button it will go to your first read chapter
                        this@NovelPage.history
                    }

                    history.value = chapterList.toRead

                    val hasHistory = rememberSaveable {
                        mutableStateOf(chapterList.hasHistory)
                    }

                    val rememberedHistory by rememberSaveable {
                        history
                    }

                    val rememberedHasHistory by rememberSaveable {
                        hasHistory
                    }

                    var rememberedFavorite by rememberSaveable {
                        favorite
                    }

                    rememberedFavorite = result.detailed.isFavorite

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(result.detailed.cover)
                                .crossfade(true)
                                .build(),
                            contentDescription = novel.name,
                            imageLoader = MainActivity.imageLoader,
                            loading = {
                                CircularProgressIndicator()
                            },
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .padding(8.dp)
                                .height((configuration.screenHeightDp / 4).dp)
                                .width((configuration.screenHeightDp / 5).dp) // TODO: Magic Numbers should be replaced in the future?
                        )
                        Column {
                            Row {
                                Column {
                                    Text(
                                        text = "${stringResource(id = R.string.author)}: ${result.detailed.author}",
                                        modifier = Modifier.padding(8.dp)
                                    )
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
                                            text = "${result.detailed.views}",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 3.em,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
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
                                            imageVector = Icons.Filled.ThumbUp,
                                            contentDescription = ""
                                        )
                                        Text(
                                            text = "${result.detailed.likes}",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 3.em,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
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
                                            imageVector = Icons.Filled.Topic,
                                            contentDescription = ""
                                        )
                                        Text(
                                            text = "${result.detailed.words}",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 3.em,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = {
                                    scope.launch(Dispatchers.IO) {
                                        EsjzoneClient.changeFavorites(authorization, novel)
                                    }
                                    rememberedFavorite = !rememberedFavorite
                                }) {
                                    if (rememberedFavorite) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = ""
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.FavoriteBorder,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    enabled = rememberedHistory != null,
                                    onClick = {
                                        navigator.push(
                                            ChapterPage(
                                                result.detailed.id(),
                                                rememberedHistory!!,
                                                history
                                            )
                                        )
                                    }
                                ) {
                                    val id = if (rememberedHasHistory)
                                        R.string.continue_reading
                                    else
                                        R.string.start_reading
                                    Text(
                                        text = stringResource(id = id),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                    Description(
                        description = result.detailed.description,
                        modifier = Modifier.padding(8.dp)
                    )
                    ChapterList(
                        chapterList = chapterList,
                        history = history,
                        hasHistory = hasHistory
                    )
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getDetail()
        }
    }

}

class NovelPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val novel: Novel
) : StateScreenModel<NovelPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(val detailed: DetailedNovel) : State()
    }

    fun getDetail() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value = State.Result(EsjzoneClient.getNovelDetail(authorization, novel))
        }
    }

}


