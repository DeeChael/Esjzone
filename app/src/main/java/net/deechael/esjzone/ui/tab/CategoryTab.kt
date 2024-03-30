package net.deechael.esjzone.ui.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NoAdultContent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.GlobalSettings
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getCategories
import net.deechael.esjzone.novellibrary.novel.Category
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.CategoryPage

@Composable
private fun MirroredIcon(index: Int, modifier: Modifier) {
    when (index) {
        0 -> {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "", modifier)
        }

        1 -> {
            Icon(
                painter = painterResource(id = R.drawable.outline_swords_24),
                contentDescription = "",
                modifier
            )
        }

        2 -> {
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "", modifier)
        }

        3 -> {
            Icon(imageVector = Icons.Filled.Edit, contentDescription = "", modifier)
        }

        4 -> {
            Icon(imageVector = Icons.Filled.NoAdultContent, contentDescription = "", modifier)
        }
    }
}

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
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current

        val scope = rememberCoroutineScope()

        val categoryModel = rememberScreenModel {
            CategoryModel(
                authorization,
                scope
            )
        }
        val state by categoryModel.state.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.categories),
                fontSize = 8.0.em,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 16.dp,
                    bottom = 16.dp
                )
            )

            when (state) {
                is CategoryModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is CategoryModel.State.Result -> {
                    val result = state as CategoryModel.State.Result

                    val adult by remember {
                        GlobalSettings.adult
                    }

                    val filteredList =
                        result.categories.toList().filter { !(it.isAdult && !adult) }.toList()

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(16.dp)
                    ) {

                        for (i in filteredList.indices) {
                            val category = filteredList[i]
                            item {
                                Card(
                                    modifier = Modifier
                                        .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                                        .aspectRatio(1f)
                                        .clickable {
                                            navigator.push(CategoryPage(category))
                                        }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        MirroredIcon(
                                            index = i,
                                            modifier = Modifier
                                                .padding(top = 16.dp)
                                                .size(50.dp)
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = category.name,
                                            modifier = Modifier.padding(16.dp),
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            categoryModel.getCategories()
        }
    }

}


class CategoryModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope
) : StateScreenModel<CategoryModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(
            val categories: List<Category>
        ) : State()
    }

    fun getCategories() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value = State.Result(EsjzoneClient.getCategories(authorization))
        }
    }

}
