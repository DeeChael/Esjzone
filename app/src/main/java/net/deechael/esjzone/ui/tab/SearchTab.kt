package net.deechael.esjzone.ui.tab

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.database.entity.SearchHistory
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.SearchPage
import net.deechael.esjzone.util.currentDateString
import net.deechael.esjzone.util.formattedDate

object SearchTab : Tab {

    private fun readResolve(): Any = SearchTab

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = stringResource(id = R.string.screen_main_tab_search),
            icon = rememberVectorPainter(image = Icons.Filled.Search)
        )

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val scope = rememberCoroutineScope()

        var loadingHistory by remember {
            mutableStateOf(true)
        }

        val histories = remember {
            mutableStateListOf<SearchHistory>()
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            var keyword by rememberSaveable {
                mutableStateOf("")
            }

            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.search_placeholder))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "")
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        navigator.push(SearchPage(keyword))
                        scope.launch(Dispatchers.IO) {
                            val dao = MainActivity.database.searchHistoryDao()
                            val history =
                                if (dao.exists(keyword)) dao.findByKeyword(keyword) else SearchHistory(
                                    keyword = keyword,
                                    time = currentDateString()
                                )
                            history.time = currentDateString()
                            dao.insertAll(history)
                        }
                    }
                ),
                singleLine = true
            )

            Text(
                text = stringResource(id = R.string.search_history),
                fontSize = 20.sp,
                modifier = Modifier.padding(24.dp)
            )

            if (loadingHistory) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    for (history in histories.toList()
                        .sortedByDescending { it.time.formattedDate() }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigator.push(SearchPage(history.keyword))
                                    scope.launch(Dispatchers.IO) {
                                        history.time = currentDateString()
                                        MainActivity.database
                                            .searchHistoryDao()
                                            .update(history)
                                    }
                                }
                        ) {
                            Text(
                                text = history.keyword,
                                modifier = Modifier.padding(
                                    top = 24.dp,
                                    bottom = 24.dp,
                                    start = 32.dp
                                )
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = {
                                    histories.remove(history)
                                    scope.launch(Dispatchers.IO) {
                                        MainActivity.database.searchHistoryDao().delete(history)
                                    }
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp, bottom = 16.dp, end = 32.dp)
                                    .size(40.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                        )
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            scope.launch(Dispatchers.IO) {
                histories.addAll(MainActivity.database.searchHistoryDao().getAll())
                loadingHistory = false
            }
        }
    }

}