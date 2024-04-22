package net.deechael.esjzone.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import net.deechael.esjzone.network.features.getFavorites
import net.deechael.esjzone.network.features.getNovelDetail
import net.deechael.esjzone.novellibrary.novel.DetailedNovel
import net.deechael.esjzone.novellibrary.novel.FavoriteNovel
import net.deechael.esjzone.ui.component.AppBar
import net.deechael.esjzone.ui.component.DropdownSelection
import net.deechael.esjzone.ui.component.Loading
import net.deechael.esjzone.ui.component.Novel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

private fun favoriteSortResource(name: String): Int {
    return when (name) {
        "new" -> R.string.favorites_recently_added
        "udate" -> R.string.favorites_recently_update
        else -> throw RuntimeException()
    }
}

object FavoritePage : Screen {

    private fun readResolve(): Any = FavoritePage

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current

        val scope = rememberCoroutineScope()

        val sort = remember {
            mutableStateOf("new")
        }

        val favoritePageModel =
            rememberScreenModel { FavoritePageModel(authorization, scope, sort) }
        val state by favoritePageModel.state.collectAsState()

        val adult by remember {
            GlobalSettings.adult
        }


        Column {
            AppBar(
                title = stringResource(id = R.string.favorites),
                onBack = {
                    navigator.pop()
                }
            ) {
                Row {
                    var exposed by remember { mutableStateOf(false) }
                    DropdownSelection(
                        label = stringResource(id = R.string.novel_list_sort),
                        items = listOf("new", "udate"),
                        current = sort.value,
                        onChange = {
                            sort.value = it
                            favoritePageModel.getRequester()
                        },
                        exposed = exposed,
                        onExposeChanged = { exposed = it },
                        modifier = Modifier.width(140.dp),
                        nameProvider = {
                            stringResource(id = favoriteSortResource(this))
                        }
                    )
                }
            }

            when (state) {
                is FavoritePageModel.State.Loading -> Loading()

                is FavoritePageModel.State.Result -> {
                    val result = (state as FavoritePageModel.State.Result)
                    val requester = result.requester

                    var current by remember {
                        mutableIntStateOf(2)
                    }

                    val max = requester.pages()

                    val items = remember {
                        mutableStateListOf<FavoriteNovel>()
                    }

                    val cache = remember {
                        mutableStateMapOf<String, DetailedNovel>()
                    }

                    items.addAll(result.firstPage)

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items.distinct()) { favoriteNovel ->
                            var detailedNovel: DetailedNovel? by remember {
                                mutableStateOf(cache[favoriteNovel.url])
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
                                            favoriteNovel
                                        )
                                        cache[favoriteNovel.url] = detailedNovel!!
                                    }
                                }
                            } else {
                                if (adult || !detailedNovel!!.isAdult) {
                                    val favorite = rememberSaveable {
                                        mutableStateOf(true)
                                    }

                                    val rememberedFavorite by rememberSaveable {
                                        favorite
                                    }

                                    if (rememberedFavorite && detailedNovel!!.isFavorite) {
                                        Novel(covered = detailedNovel!!) {
                                            navigator.push(
                                                NovelPage(
                                                    favoriteNovel,
                                                    favorite = favorite
                                                )
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
            favoritePageModel.getRequester()
        }
    }

}

class FavoritePageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val sort: MutableState<String>
) : StateScreenModel<FavoritePageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(
            val requester: PageableRequester<FavoriteNovel>,
            val firstPage: List<FavoriteNovel>
        ) : State()
    }

    fun getRequester() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            val (requester, novels) = EsjzoneClient.getFavorites(
                authorization,
                sort.value
            )
            mutableState.value = State.Result(requester, novels)
        }
    }

}
