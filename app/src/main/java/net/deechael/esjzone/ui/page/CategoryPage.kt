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
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getNovelDetail
import net.deechael.esjzone.network.features.listNovels
import net.deechael.esjzone.novellibrary.novel.Category
import net.deechael.esjzone.novellibrary.novel.CategoryNovel
import net.deechael.esjzone.novellibrary.novel.DetailedNovel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

class CategoryPage(private val category: Category) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val scope = rememberCoroutineScope()

        val categoryPageModel =
            rememberScreenModel { CategoryPageModel(authorization, scope, category) }
        val state by categoryPageModel.state.collectAsState()

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
                        text = category.name,
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
                is CategoryPageModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is CategoryPageModel.State.Result -> {
                    val result = state as CategoryPageModel.State.Result

                    val novels = result.categoryNovels

                    val cache = remember {
                        mutableStateMapOf<String, DetailedNovel>()
                    }

                    val adult by remember {
                        GlobalSettings.adult
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(novels) { categoryNovel ->
                            var detailedNovel: DetailedNovel? by remember {
                                mutableStateOf(cache[categoryNovel.url])
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
                                            categoryNovel
                                        )
                                        cache[categoryNovel.url] = detailedNovel!!
                                    }
                                }
                            } else {
                                if (adult || !detailedNovel!!.isAdult) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 8.dp, top = 4.dp, end = 8.dp)
                                            .clickable {
                                                navigator.push(NovelPage(categoryNovel))
                                            }
                                    ) {
                                        Row {
                                            SubcomposeAsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(detailedNovel!!.cover)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = categoryNovel.name,
                                                imageLoader = MainActivity.imageLoader,
                                                loading = {
                                                    CircularProgressIndicator()
                                                },
                                                contentScale = ContentScale.FillHeight,
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .height((configuration.screenHeightDp / 3.5).dp)
                                                    .width((configuration.screenWidthDp / 2.5).dp)
                                            )
                                            Column {
                                                Text(
                                                    text = detailedNovel!!.name,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
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
                                                        text = "${detailedNovel!!.views}",
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
                                                        text = "${detailedNovel!!.likes}",
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontSize = 3.em,
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
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
            categoryPageModel.getNovels()
        }
    }

}

class CategoryPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val category: Category
) : StateScreenModel<CategoryPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(val categoryNovels: List<CategoryNovel>) : State()
    }

    fun getNovels() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value = State.Result(EsjzoneClient.listNovels(authorization, category))
        }
    }

}