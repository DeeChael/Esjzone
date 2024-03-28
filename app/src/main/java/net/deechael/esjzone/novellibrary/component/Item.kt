package net.deechael.esjzone.novellibrary.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val title = analyseParagraph(EsjzoneXPaths.Detail.ChapterListDetails.Title.evaluate(element).elements[0])[0] as TextComponent

    val chapters = mutableListOf<Chapter>()
    for (child in element.children()) {
        if (child.nameIs("a"))
            chapters.add(analyseChapter(child))
    }

    return ChapterListItem(title, chapters.toList())
}

private fun analyseChapter(element: Element): Chapter {
    return Chapter(
        element.attr("data-title"),
        element.attr("href")
    )
}

interface Item {

    @Composable
    fun Render()

}

class TextItem(private val component: TextComponent) : Item {

    @Composable
    override fun Render() {
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

class ChapterItem(private val chapter: Chapter) : Item {

    @Composable
    override fun Render() {
        val navigator = LocalBaseNavigator.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    if (chapter.url.contains("esjzone"))
                        navigator.push(ChapterPage(chapter))
                }
        ) {
            Text(text = chapter.name, modifier = Modifier.padding(16.dp))
        }
    }

}

class ChapterListItem(private val name: TextComponent, private val chapters: List<Chapter>) : Item {

    @Composable
    override fun Render() {
        val navigator = LocalBaseNavigator.current

        val textMeasurer = rememberTextMeasurer()
        val textStyle = LocalTextStyle.current
        val density = LocalDensity.current

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
                        .clickable { expanded = !expanded }
                ) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        for (chapter in chapters) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                        bottom = 8.dp
                                    )
                                    .clickable {
                                        if (chapter.url.contains("esjzone"))
                                            navigator.push(ChapterPage(chapter))
                                    }
                            ) {
                                Text(text = chapter.name, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

}