package net.deechael.esjzone.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.PageableRequester
import net.deechael.esjzone.network.features.novels
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import net.deechael.esjzone.ui.component.AppBar
import net.deechael.esjzone.ui.component.DropdownSelection
import net.deechael.esjzone.ui.component.Loading
import net.deechael.esjzone.ui.component.Novel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

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
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current

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

        val novelListModel =
            rememberScreenModel { NovelListPageModel(authorization, scope, novelType, sortType) }
        val state by novelListModel.state.collectAsState()

        val adult by remember {
            GlobalSettings.adult
        }

        Column {
            AppBar(
                title = stringResource(id = R.string.novel_list),
                onBack = {
                    navigator.pop()
                }
            ) {
                Row {
                    var typeExposed by remember { mutableStateOf(false) }
                    var sortExposed by remember { mutableStateOf(false) }
                    DropdownSelection(
                        label = stringResource(id = R.string.novel_list_type),
                        items = listOf(0, 2, 1, 3),
                        current = novelType.intValue,
                        onChange = {
                            novelType.intValue = it
                            novelListModel.getRequester()
                        },
                        exposed = typeExposed,
                        onExposeChanged = {
                            typeExposed = it
                        },
                        modifier = Modifier.width(110.dp),
                        nameProvider = { stringResource(id = typeResource(this)) }
                    )
                    Spacer(modifier = Modifier.weight(2f))
                    if (adult) {
                        Text(
                            text = stringResource(id = R.string.novel_list_adultonly),
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Checkbox(
                            checked = adultOnly,
                            onCheckedChange = { adultOnly = it },
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    DropdownSelection(
                        label = stringResource(id = R.string.novel_list_sort),
                        items = listOf(1, 2, 3, 4, 5, 6, 7, 8),
                        current = sortType.intValue,
                        onChange = {
                            sortType.intValue = it
                            novelListModel.getRequester()
                        },
                        exposed = sortExposed,
                        onExposeChanged = {
                            sortExposed = it
                        },
                        modifier = Modifier.width(140.dp),
                        nameProvider = { stringResource(id = sortResource(this)) }
                    )
                }
            }

            when (state) {
                is NovelListPageModel.State.Loading -> Loading()

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

                    LazyColumn {
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
                            Novel(covered = novel)
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
            val (requester, novels) = EsjzoneClient.novels(
                authorization,
                novelType.intValue,
                sortType.intValue
            )
            mutableState.value = State.Result(requester, novels)
        }
    }

}
