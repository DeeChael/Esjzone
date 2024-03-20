package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import net.deechael.esjzone.R

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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}