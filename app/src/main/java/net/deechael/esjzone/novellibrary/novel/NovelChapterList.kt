package net.deechael.esjzone.novellibrary.novel

import net.deechael.esjzone.novellibrary.component.Item
import net.deechael.esjzone.novellibrary.component.analyseItems
import org.jsoup.nodes.Element
import java.io.Serializable

data class NovelChapterList(
    val items: List<Item>
) : Serializable

fun analyseChapterList(element: Element): NovelChapterList {
    return NovelChapterList(
        analyseItems(element)
    )
}