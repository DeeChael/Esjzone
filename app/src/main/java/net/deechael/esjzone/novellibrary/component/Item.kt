package net.deechael.esjzone.novellibrary.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import net.deechael.esjzone.network.EsjzoneXPaths
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.ChapterPage
import org.jsoup.nodes.Element

fun analyseItems(element: Element): List<Item> {
    val items = mutableListOf<Item>()

    for (child in element.children()) {
        if (child.nameIs("p")) {
            items.add(TextItem(analyseParagraph(child)[0] as TextComponent))
        } else if (child.nameIs("a")) {
            items.add(ChapterItem(analyseChapter(child)))
        } else if (child.nameIs("details")) {
            items.add(analyseChapterList(child))
        }
    }

    return items.toList()
}

private fun analyseChapterList(element: Element): ChapterListItem {
    val title =
        analyseParagraph(EsjzoneXPaths.Detail.ChapterListDetails.Title.evaluate(element).elements[0])[0] as TextComponent

    val chapters = mutableListOf<Chapter>()
    for (child in element.children()) {
        if (child.nameIs("a"))
            chapters.add(analyseChapter(child))
    }

    return ChapterListItem(title, chapters.toList())
}

private fun analyseChapter(element: Element): Chapter {
    val children = element.children()
    val isHistory = if (children.isNotEmpty()) {
        val first = children[0]
        if (first.hasAttr("class")) {
            first.attr("class").contains("active")
        } else {
            false
        }
    } else {
        false
    }

    return Chapter(
        element.attr("data-title"),
        element.attr("href"),
        isHistory
    )
}

interface Item {

    @Composable
    fun Render(novelId: String, history: MutableState<Chapter?>, hasHistory: MutableState<Boolean>)

}

class TextItem(private val component: TextComponent) : Item {

    @Composable
    override fun Render(
        novelId: String,
        history: MutableState<Chapter?>,
        hasHistory: MutableState<Boolean>
    ) {
        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val density = LocalDensity.current

        val (str, inlines) = component.toInlineAnnotatedString(
            textMeasurer,
            textStyle,
            density
        )
        Text(
            text = str,
            inlineContent = inlines,
            modifier = Modifier.padding(8.dp)
        )
    }

}

class ChapterItem(val chapter: Chapter) : Item {

    @Composable
    override fun Render(
        novelId: String,
        history: MutableState<Chapter?>,
        hasHistory: MutableState<Boolean>
    ) {
        val navigator = LocalBaseNavigator.current

        var historied by rememberSaveable {
            hasHistory
        }

        var rememberedHistory by rememberSaveable {
            history
        }

        if (historied && chapter == rememberedHistory) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        if (chapter.url.contains("esjzone"))
                            navigator.push(ChapterPage(novelId, chapter, history))
                    }
            ) {
                Text(
                    text = chapter.name,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    )
                )
            }
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        if (chapter.url.contains("esjzone")) {
                            historied = true
                            rememberedHistory = this.chapter
                            navigator.push(ChapterPage(novelId, chapter, history))
                        }
                    }
            ) {
                Text(
                    text = chapter.name,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    )
                )
            }
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }

}

class ChapterListItem(private val name: TextComponent, val chapters: List<Chapter>) : Item {

    @Composable
    override fun Render(
        novelId: String,
        history: MutableState<Chapter?>,
        hasHistory: MutableState<Boolean>
    ) {
        val navigator = LocalBaseNavigator.current

        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val density = LocalDensity.current

        var historied by rememberSaveable {
            hasHistory
        }

        var rememberedHistory by rememberSaveable {
            history
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            var expanded by remember {
                mutableStateOf(false)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val vector = if (expanded)
                        Icons.Filled.ExpandLess
                    else
                        Icons.Filled.ExpandMore
                    Icon(
                        imageVector = vector,
                        contentDescription = "",
                        modifier = Modifier.padding(12.dp)
                    )
                    val (str, inlines) = name.toInlineAnnotatedString(
                        textMeasurer,
                        textStyle,
                        density
                    )
                    Text(
                        text = str,
                        inlineContent = inlines,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
                AnimatedVisibility(expanded) {
                    Column {
                        for (chapter in chapters) {
                            if (chapter == rememberedHistory) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 8.dp,
                                            end = 8.dp
                                        )
                                        .clickable {
                                            if (chapter.url.contains("esjzone"))
                                                navigator.push(
                                                    ChapterPage(
                                                        novelId,
                                                        chapter,
                                                        history
                                                    )
                                                )
                                        }
                                ) {
                                    Text(
                                        text = chapter.name,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 8.dp,
                                            end = 8.dp
                                        )
                                        .clickable {
                                            if (chapter.url.contains("esjzone")) {
                                                historied = true
                                                rememberedHistory = chapter
                                                navigator.push(
                                                    ChapterPage(
                                                        novelId,
                                                        chapter,
                                                        history
                                                    )
                                                )
                                            }
                                        }
                                ) {
                                    Text(
                                        text = chapter.name,
                                        modifier = Modifier.padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 12.dp,
                                            bottom = 12.dp
                                        )
                                    )
                                }
                                HorizontalDivider(
                                    thickness = 2.dp,
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}