package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import net.deechael.esjzone.R
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.SearchPage

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
                    }
                ),
                singleLine = true
            )
        }
    }
}