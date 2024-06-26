package net.deechael.esjzone.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import net.deechael.esjzone.network.features.search
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import net.deechael.esjzone.ui.component.AppBar
import net.deechael.esjzone.ui.component.Loading
import net.deechael.esjzone.ui.component.Novel
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

class SearchPage(private val keyword: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current

        val scope = rememberCoroutineScope()

        val searchModel = rememberScreenModel { SearchPageModel(authorization, scope, keyword) }
        val state by searchModel.state.collectAsState()

        Column {
            AppBar(
                title = "${stringResource(id = R.string.search_result)}: $keyword",
                onBack = {
                    navigator.pop()
                }
            )

            when (state) {
                is SearchPageModel.State.Loading -> Loading()

                is SearchPageModel.State.Result -> {
                    val result = (state as SearchPageModel.State.Result)
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

                    val adult by remember {
                        GlobalSettings.adult
                    }

                    LazyColumn(state = listState) {
                        items(items.toList().filter {
                            if (!it.isAdult)
                                true
                            else
                                adult
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
            searchModel.getRequester()
        }
    }

}

class SearchPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val keyword: String
) : StateScreenModel<SearchPageModel.State>(State.Loading) {

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
            val (requester, novels) = EsjzoneClient.search(authorization, keyword)
            mutableState.value = State.Result(requester, novels)
        }
    }

}
