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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
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
import net.deechael.esjzone.R
import net.deechael.esjzone.network.Authorization
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.LocalAuthorization
import net.deechael.esjzone.network.features.getChapterDetail
import net.deechael.esjzone.novellibrary.component.ImageComponent
import net.deechael.esjzone.novellibrary.component.TextComponent
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.novellibrary.novel.DetailedChapter
import net.deechael.esjzone.ui.component.AppBar
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

class ChapterPage(
    private val novelId: String,
    private var chapter: Chapter,
    private val history: MutableState<Chapter?>
) : Screen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current

        val authorization = LocalAuthorization.current

        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val density = LocalDensity.current

        val scope = rememberCoroutineScope()

        val requestedChapter = rememberSaveable {
            mutableStateOf(chapter)
        }

        val chapterPageModel =
            rememberScreenModel { ChapterPageModel(authorization, scope, requestedChapter) }
        val state by chapterPageModel.state.collectAsState()

        var showToolbar by remember {
            mutableStateOf(false)
        }

        val scrollState = rememberScrollState()

        val maxScroll by remember {
            derivedStateOf {
                scrollState.maxValue
            }
        }

        var sliderPosition by remember { mutableFloatStateOf(0f) }

        var chapterName by remember {
            mutableStateOf(chapter.name)
        }

        if (scrollState.isScrollInProgress) {
            sliderPosition = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
        }

        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = showToolbar,
                    enter = slideInVertically(),
                    exit = shrinkVertically()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppBar(
                            title = chapterName,
                            onBack = {
                                navigator.pop()
                            }
                        )
                    }
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = showToolbar,
                    enter = slideInVertically(
                        initialOffsetY = {
                            it / 2
                        },
                    ),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            HorizontalDivider(thickness = 2.dp)
                            Spacer(modifier = Modifier.height(10.dp))

                            if (state is ChapterPageModel.State.Result) {
                                var rememberedHistory by rememberSaveable {
                                    history
                                }

                                val result = state as ChapterPageModel.State.Result
                                val detailed by remember {
                                    result.detailed
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    OutlinedButton(
                                        enabled = detailed.previous != null,
                                        onClick = {
                                            val previous = detailed.previous!!
                                            if (novelId == previous.novelId()) { // README 吐槽#4
                                                rememberedHistory = previous
                                            }
                                            requestedChapter.value = previous
                                            chapterName = previous.name
                                            chapter = previous
                                            chapterPageModel.getDetail()
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.previous_chapter))
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Slider(
                                        value = sliderPosition,
                                        onValueChange = {
                                            sliderPosition = it
                                            val scrollTo = (maxScroll * it).toInt()
                                            scope.launch(Dispatchers.Main) {
                                                scrollState.animateScrollTo(scrollTo)
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    OutlinedButton(
                                        enabled = detailed.next != null,
                                        onClick = {
                                            val next = detailed.next!!
                                            if (novelId == next.novelId()) { // README 吐槽#4
                                                rememberedHistory = next
                                            }
                                            requestedChapter.value = next
                                            chapterName = next.name
                                            chapter = next
                                            chapterPageModel.getDetail()
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.next_chapter))
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    OutlinedButton(
                                        enabled = false,
                                        onClick = {}
                                    ) {
                                        Text(text = stringResource(id = R.string.previous_chapter))
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Slider(
                                        value = 0f,
                                        onValueChange = {},
                                        enabled = false,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    OutlinedButton(
                                        enabled = false,
                                        onClick = {}
                                    ) {
                                        Text(text = stringResource(id = R.string.next_chapter))
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
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
                    ) { showToolbar = !showToolbar }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(
                            scrollState
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
                            for (component in result.detailed.value.content) {
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
            chapterPageModel.getDetail()
        }
    }

}

class ChapterPageModel(
    private val authorization: Authorization,
    private val scope: CoroutineScope,
    private val chapter: MutableState<Chapter>
) : StateScreenModel<ChapterPageModel.State>(State.Loading) {

    sealed class State {
        data object Loading : State()
        data class Result(val detailed: MutableState<DetailedChapter>) : State()
    }

    fun getDetail() {
        scope.launch(Dispatchers.IO) {
            mutableState.value = State.Loading
            mutableState.value =
                State.Result(
                    mutableStateOf(
                        EsjzoneClient.getChapterDetail(
                            authorization,
                            chapter.value
                        )
                    )
                )
        }
    }

}


