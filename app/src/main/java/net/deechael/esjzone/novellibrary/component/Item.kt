package net.deechael.esjzone.novellibrary.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    val title = EsjzoneXPaths.Detail.ChapterListDetails.Title.evaluate(element).get()

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    // go to the chapter page
                }
        ) {
            Text(text = chapter.name, modifier = Modifier.padding(16.dp))
        }
    }

}

class ChapterListItem(private val name: String, private val chapters: List<Chapter>) : Item {

    @Composable
    override fun Render() {
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
                    Text(text = name, modifier = Modifier.padding(16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "",
                        modifier = Modifier.padding(12.dp)
                    )
                }
                AnimatedVisibility(expanded) {
                    Column {
                        for (chapter in chapters) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 8.dp,
                                        bottom = 8.dp
                                    )
                                    .clickable {
                                        // go to the chapter page
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