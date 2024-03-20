package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import net.deechael.esjzone.R

object CategoryTab : Tab {

    private fun readResolve(): Any = CategoryTab

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = stringResource(id = R.string.screen_main_tab_category),
            icon = rememberVectorPainter(image = Icons.Filled.Category)
        )

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }

}