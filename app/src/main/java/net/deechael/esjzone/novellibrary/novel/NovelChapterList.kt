package net.deechael.esjzone.novellibrary.novel

import net.deechael.esjzone.novellibrary.component.Item
import net.deechael.esjzone.novellibrary.component.analyseItems
import org.jsoup.nodes.Element

data class NovelChapterList(
    val items: List<Item>
)

fun analyseChapterList(element: Element): NovelChapterList {
    return NovelChapterList(
        analyseItems(element)
    )
}