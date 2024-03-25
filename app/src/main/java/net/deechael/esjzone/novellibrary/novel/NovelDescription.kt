package net.deechael.esjzone.novellibrary.novel

import net.deechael.esjzone.novellibrary.component.Component
import net.deechael.esjzone.novellibrary.component.analyseComponents
import org.jsoup.nodes.Element
import java.io.Serializable

data class NovelDescription(
    val components: List<Component>
) : Serializable

fun analyseDescription(element: Element): NovelDescription {
    return NovelDescription(
        analyseComponents(element)
    )
}
