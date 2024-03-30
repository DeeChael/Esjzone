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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import net.deechael.esjzone.network.PageableRequester
import net.deechael.esjzone.network.features.novels
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import java.lang.RuntimeException

private fun typeResource(type: Int): Int {
    return when (type) {
        0 -> R.string.novel_list_all
        1 -> R.string.novel_list_japanese
        2 -> R.string.novel_list_original
        3 -> R.string.novel_list_korean
        else -> throw RuntimeException()
    }
}

private fun sortResource(type: Int): Int {
    return when (type) {
        1 -> R.string.novel_filter_recentlyupdate
        2 -> R.string.novel_filter_recentlyupload
        3 -> R.string.novel_filter_highestrating
        4 -> R.string.novel_filter_mostviews
        5 -> R.string.novel_filter_mostchapters
        6 -> R.string.novel_filter_mostcomments
        7 -> R.string.novel_filter_mostfavorites
        8 -> R.string.novel_filter_mostwords
        else -> throw RuntimeException()
    }
}

class NovelListPage(
    private val initializedNovelType: Int,
    private val initializedSortType: Int,
    private val initializedAdultOnly: Boolean
): Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val scope = rememberCoroutineScope()

        val novelType = remember {
            mutableIntStateOf(initializedNovelType)
        }

        val sortType = remember {
            mutableIntStateOf(initializedSortType)
        }

        var adultOnly by remember {
            mutableStateOf(initializedAdultOnly)
        }

        val novelListModel = rememberScreenModel { NovelListPageModel(authorization, scope, novelType, sortType) }
        val state by novelListModel.state.collectAsState()

        val adult by remember {
            GlobalSettings.adult
        }

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
                        text = stringResource(id = R.string.novel_list),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 4.dp
                        )
                    )
                    Spacer(modifier = Modifier.weight(5f))
                }
                Row {
                    var typeExposed by remember { mutableStateOf(false) }
                    var sortExposed by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = typeExposed, onExpandedChange = { typeExposed = it }) {
                        TextField(
                            readOnly = true,
                            value = stringResource(id = typeResource(novelType.intValue)),
                            onValueChange = { },
                            label = { Text(stringResource(id = R.string.novel_list_type)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = typeExposed
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(110.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = typeExposed,
                            onDismissRequest = {
                                typeExposed = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_list_all))
                                },
                                onClick = {
                                    novelType.intValue = 0
                                    typeExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_list_original))
                                },
                                onClick = {
                                    novelType.intValue = 2
                                    typeExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_list_japanese))
                                },
                                onClick = {
                                    novelType.intValue = 1
                                    typeExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_list_korean))
                                },
                                onClick = {
                                    novelType.intValue = 3
                                    typeExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(2f))
                    if (adult) {
                        Text(text = stringResource(id = R.string.novel_list_adultonly), modifier = Modifier.padding(top = 16.dp))
                        Checkbox(checked = adultOnly, onCheckedChange = { adultOnly = it }, modifier = Modifier.padding(top = 4.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ExposedDropdownMenuBox(expanded = sortExposed, onExpandedChange = { sortExposed = it }) {
                        TextField(
                            readOnly = true,
                            value = stringResource(id = sortResource(sortType.intValue)),
                            onValueChange = { },
                            label = { Text(stringResource(id = R.string.novel_list_sort)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = sortExposed
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(140.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = sortExposed,
                            onDismissRequest = {
                                sortExposed = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_recentlyupdate))
                                },
                                onClick = {
                                    sortType.intValue = 1
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_recentlyupload))
                                },
                                onClick = {
                                    sortType.intValue = 2
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_highestrating))
                                },
                                onClick = {
                                    sortType.intValue = 3
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_mostviews))
                                },
                                onClick = {
                                    sortType.intValue = 4
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_mostchapters))
                                },
                                onClick = {
                                    sortType.intValue = 5
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_mostcomments))
                                },
                                onClick = {
                                    sortType.intValue = 6
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_mostfavorites))
                                },
                                onClick = {
                                    sortType.intValue = 7
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.novel_filter_mostwords))
                                },
                                onClick = {
                                    sortType.intValue = 8
                                    sortExposed = false
                                    novelListModel.getRequester()
                                }
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp)
            }

            when (state) {
                is NovelListPageModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is NovelListPageModel.State.Result -> {
                    val result = (state as NovelListPageModel.State.Result)
                    val requester = result.requester

                    var current by remember {
                        mutableIntStateOf(2)
                    }

                    val max = requester.pages()

                    val items = remember {
                        mutableStateListOf<CoveredNovel>()
                    }

                    items.addAll(result.firstPage)

                    val listState = rememberLazyListState()

                    LazyColumn(state = listState) {
                        items(items.toList().filter {
                            if (adult) {
                                if (adultOnly)
                                    return@filter it.isAdult
                                else
                                    return@filter true
                            } else {
                                return@filter !it.isAdult
                            }
                        }.distinct()) { novel ->
                            Card(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, top = 4.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        navigator.push(NovelPage(novel))
                                    }
                            ) {
                                Row {
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
                                        contentScale = ContentScale.FillHeight,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .height((configuration.screenHeightDp / 3.5).dp)
                                            .width((configuration.screenWidthDp / 2.5).dp)
                                    )
                                    Column {
                                        Text(
                                            text = novel.name,
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
                                                text = "${novel.views}",
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
                        }

                        item {
                            if (current <= max && max > 1) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                                LaunchedEffect(currentCompositeKeyHash) {
                                    scope.launch(Dispatchers.IO) {
                                        val newlyLoaded = requester.more(current)
                                        for (item in newlyLoaded) {
                                            if (items.contains(item))
                                                continue
                                            items.add(item)
                                        }
                                        current += 1
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
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
            novelListModel.getRequester()
        }
    }

}

class NovelListPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val novelType: MutableIntState,
    private val sortType: MutableIntState,
) : StateScreenModel<NovelListPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(
            val requester: PageableRequester<CoveredNovel>,
            val firstPage: List<CoveredNovel>
        ) : State()
    }

    fun getRequester() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            val (requester, novels) = EsjzoneClient.novels(authorization, novelType.intValue, sortType.intValue)
            mutableState.value = State.Result(requester, novels)
        }
    }

}
