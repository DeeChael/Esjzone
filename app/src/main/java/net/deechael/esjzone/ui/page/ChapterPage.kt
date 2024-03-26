package net.deechael.esjzone.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getChapterDetail
import net.deechael.esjzone.novellibrary.component.ImageComponent
import net.deechael.esjzone.novellibrary.component.TextComponent
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.novellibrary.novel.DetailedChapter
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

class ChapterPage(private val chapter: Chapter) : Screen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current
        val configuration = LocalConfiguration.current

        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val density = LocalDensity.current

        val scope = rememberCoroutineScope()

        val screenModel = rememberScreenModel { ChapterPageModel(authorization, scope, chapter) }
        val state by screenModel.state.collectAsState()

        var showTopBar by remember {
            mutableStateOf(false)
        }

        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = showTopBar,
                    enter = slideInVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBackIosNew,
                                        contentDescription = ""
                                    )
                                }
                                Spacer(modifier = Modifier.weight(3f))
                                Text(
                                    text = chapter.name,
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
                    }
                }
            }
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { showTopBar = !showTopBar }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    Text(
                        text = chapter.name,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 32.dp, top = 32.dp)
                    )

                    when (state) {
                        is ChapterPageModel.State.Loading -> Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }

                        is ChapterPageModel.State.Result -> Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val result = state as ChapterPageModel.State.Result
                            for (component in result.detailed.content) {
                                if (component is TextComponent) {
                                    val (str, inlines) = component.toInlineAnnotatedString(
                                        textMeasurer,
                                        textStyle,
                                        density
                                    )
                                    Text(
                                        text = str,
                                        inlineContent = inlines
                                    )
                                    /*
                                                        Text(
                                                            text = component.toAnnotatedString(),
                                                            modifier = Modifier.padding(3.dp)
                                                        )
                                                        */
                                } else if (component is ImageComponent) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(component.url)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "description",
                                        imageLoader = MainActivity.imageLoader,
                                        loading = {
                                            CircularProgressIndicator()
                                        },
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 8.dp,
                                                end = 8.dp,
                                                top = 3.dp,
                                                bottom = 3.dp
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.getDetail()
        }
    }

}

class ChapterPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val chapter: Chapter
) : StateScreenModel<ChapterPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(val detailed: DetailedChapter) : State()
    }

    fun getDetail() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(EsjzoneClient.getChapterDetail(authorization, chapter))
        }
    }

}


