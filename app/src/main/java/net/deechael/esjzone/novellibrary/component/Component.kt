package net.deechael.esjzone.novellibrary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.deechael.esjzone.novellibrary.novel.NovelDescription
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode


fun analyseComponents(element: Element): List<Component> {
    val components = mutableListOf<Component>()

    for (paragraph in element.children()) {
        components.addAll(analyseParagraph(paragraph))
    }

    return components.toList()
}


private fun analyseParagraph(paragraph: Element): List<Component> {
    val components = mutableListOf<TextComponent>()

    for (child in paragraph.childNodes()) {
        if (child is TextNode) {
            components.add(TextComponent(child.text()))
        } else if (child is Element) {
            if (child.tagName() == "img") {
                return listOf(ImageComponent(child.attr("src")))
            } else {
                components.addAll(analyseText(child, listOf()))
            }
        }
    }

    return components.toSingle()
}

private val STYLE_BACKGROUND_COLOR_REGEX =
    "background-color:rgb\\(([0-9]+),([0-9]+),([0-9]+)\\)".toRegex()
private val STYLE_COLOR_REGEX =
    "(?<!background-)color:rgb\\(([0-9]+),([0-9]+),([0-9]+)\\)".toRegex()
private val STYLE_FONT_SIZE_REGEX = "font-size:([0-9]+)px".toRegex()

private fun analyseText(node: Node, styles: List<TextStyle>): List<TextComponent> {
    if (node is TextNode) {
        var component = TextComponent(node.text())
        for (style in styles)
            component = component.style(style)
        return listOf(component)
    } else if (node is Element) {
        val list = mutableListOf<TextComponent>()

        if (node.nameIs("ruby")) {
            val rts = node.getElementsByTag("rt")
            if (rts.isNotEmpty()) {
                val furiganaComponents = mutableListOf<TextComponent>()

                furiganaComponents.addAll(analyseText(rts[0], listOf()))

                if (furiganaComponents.isNotEmpty()) {
                    val newStyles = mutableListOf<TextStyle>()
                    newStyles.addAll(styles)

                    val single = furiganaComponents.toSingle()[0]

                    newStyles.add(FuriganaTextStyle(single))

                    for (childNode in node.childNodes()) {
                        if (childNode is TextNode)
                            list.addAll(analyseText(childNode, newStyles))
                        else if (childNode is Element) {
                            if (!childNode.nameIs("rp") && !childNode.nameIs("rt")) {
                                list.addAll(analyseText(childNode, newStyles))
                            }
                        }
                    }
                    return list
                }
            }
            for (childNode in node.childNodes()) {
                if (childNode is TextNode)
                    list.addAll(analyseText(childNode, styles))
                else if (childNode is Element) {
                    if (!childNode.nameIs("rp") && !childNode.nameIs("rt")) {
                        list.addAll(analyseText(childNode, styles))
                    }
                }
            }
            return list
        } else if (node.nameIs("br")) {
            return listOf(NewLineComponent())
        } else if (node.nameIs("rp")) {
            return listOf()
        }

        val newStyles = mutableListOf<TextStyle>()
        newStyles.addAll(styles)
        if (node.nameIs("strong")) {
            newStyles.add(BoldTextStyle)
        } else if (node.nameIs("u")) {
            newStyles.add(UnderlineTextStyle)
        } else if (node.nameIs("!strong")) {
            newStyles.remove(BoldTextStyle)
        } else if (node.nameIs("!u")) {
            newStyles.remove(UnderlineTextStyle)
        } else if (node.nameIs("span")) {
            newStyles.addAll(analyseStyles(node.attr("style").replace(" ", "")))
        }

        node.childNodes().map { analyseText(it, newStyles) }.forEach { list.addAll(it) }
        return list
    }
    return listOf()
}

private fun analyseStyles(styleString: String): List<TextStyle> {
    val list = mutableListOf<TextStyle>()

    for (result in STYLE_COLOR_REGEX.findAll(styleString)) {
        list.add(
            ColorTextStyle(
                Color(
                    result.groupValues[1].toInt(),
                    result.groupValues[2].toInt(),
                    result.groupValues[3].toInt()
                )
            )
        )
    }

    for (result in STYLE_BACKGROUND_COLOR_REGEX.findAll(styleString)) {
        list.add(
            BackgroundColorTextStyle(
                Color(
                    result.groupValues[1].toInt(),
                    result.groupValues[2].toInt(),
                    result.groupValues[3].toInt()
                )
            )
        )
    }

    for (result in STYLE_FONT_SIZE_REGEX.findAll(styleString)) {
        list.add(FontSizeTextStyle(result.groupValues[1].toInt()))
    }

    return list.toList()
}

interface Component

open class TextComponent(val text: String) : Component {

    private val extras: MutableList<TextComponent> = mutableListOf()
    private val styles: MutableList<TextStyle> = mutableListOf()
    private var inline: FuriganaTextStyle? = null

    fun append(component: TextComponent): TextComponent {
        this.extras.add(component)
        return this
    }

    open fun style(style: TextStyle): TextComponent {
        if (style is FuriganaTextStyle)
            inline = style
        this.styles.add(style)
        return this
    }

    fun getExtras(): List<TextComponent> {
        return this.extras.toList()
    }

    fun getStyles(): List<TextStyle> {
        return this.styles.toList()
    }

    fun toAnnotatedString(): AnnotatedString {
        return buildAnnotatedString {
            var basicSpanStyle = SpanStyle()
            this@TextComponent.getStyles().forEach {
                basicSpanStyle = it.apply(basicSpanStyle)
            }


            withStyle(basicSpanStyle) {
                append(this@TextComponent.text)
            }

            for (extra in this@TextComponent.getExtras()) {
                append(extra.toAnnotatedString())
            }
        }
    }

    fun toInlineAnnotatedString(
        textMeasurer: TextMeasurer,
        localTextStyle: androidx.compose.ui.text.TextStyle,
        localDensity: Density
    ): Pair<AnnotatedString, Map<String, InlineTextContent>> {
        val inlineContent = mutableMapOf<String, InlineTextContent>()
        // val width = (text.length.toDouble() + ((text.length - 1) * 0.05) + 2).em

        return buildAnnotatedString {
            var basicSpanStyle = SpanStyle()

            this@TextComponent.getStyles().forEach {
                basicSpanStyle = it.apply(basicSpanStyle)
            }

            if (inline != null) {
                withStyle(basicSpanStyle) {
                    appendInlineContent(
                        this@TextComponent.text,
                        this@TextComponent.text
                    )
                }

                val size =
                    textMeasurer.measure(text, localTextStyle.copy().merge(basicSpanStyle)).size
                val width = with(localDensity) { size.width.toDp().toSp() }
                val height = with(localDensity) { size.height.toDp().toSp() }

                inlineContent[this@TextComponent.text] = inline!!.inline(
                    width,
                    height,
                    basicSpanStyle,
                    this@TextComponent.text,
                    localDensity
                )
            } else {
                withStyle(basicSpanStyle) {
                    append(this@TextComponent.text)
                }
            }

            for (extra in this@TextComponent.getExtras()) {
                val (str, inlines) = extra.toInlineAnnotatedString(
                    textMeasurer,
                    localTextStyle,
                    localDensity
                )
                append(str)
                inlineContent.putAll(inlines)
            }
        } to inlineContent.toMap()
    }

}

class NewLineComponent : TextComponent("\n") {

    override fun style(style: TextStyle): TextComponent {
        return this
    }

}

class ImageComponent(val url: String) : Component

interface TextStyle {

    fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle
    }

}

object BoldTextStyle : TextStyle {

    override fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle.merge(
            SpanStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }

}

object UnderlineTextStyle : TextStyle {

    override fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle.merge(
            SpanStyle(
                textDecoration = TextDecoration.Underline
            )
        )
    }

}

class ColorTextStyle(private val color: Color) : TextStyle {

    override fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle.merge(
            SpanStyle(
                color = this.color
            )
        )
    }

}

class BackgroundColorTextStyle(private val color: Color) : TextStyle {

    override fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle.merge(
            SpanStyle(
                background = this.color
            )
        )
    }

}

class FontSizeTextStyle(private val size: Int) : TextStyle {

    override fun apply(spanStyle: SpanStyle): SpanStyle {
        return spanStyle.merge(
            SpanStyle(
                fontSize = this.size.sp
            )
        )
    }

}

// Code from: https://github.com/mainrs/android-compose-furigana/blob/main/library/src/main/java/net/zerotask/libraries/android/compose/furigana/TextWithReading.kt
// License: MIT License
class FuriganaTextStyle(text: TextComponent) : TextStyle {

    private val text: TextComponent

    init {
        this.text = safer(text)
    }

    private fun safer(text: TextComponent): TextComponent {
        val newOne = TextComponent(text.text)
        for (style in text.getStyles())
            if (style !is FontSizeTextStyle)
                newOne.style(style)

        for (extra in text.getExtras())
            newOne.append(safer(extra))
        return newOne
    }

    /**
     * TODO: fix magic number, though it not a big problem
     */
    fun inline(
        width: TextUnit,
        height: TextUnit,
        style: SpanStyle,
        bottom: String,
        localDensity: Density
    ): InlineTextContent {
        return InlineTextContent(
            placeholder = Placeholder(
                width = width,
                height = (height.value + 18).sp,
                // height = 2.12.em,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom,
            ),
            children = {
                val readingFontSize = 11.sp / 1.4
                val boxHeight = with(localDensity) { (readingFontSize).toDp() }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Box(
                        modifier = Modifier.requiredHeight(boxHeight + 6.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            modifier = Modifier
                                .wrapContentWidth(unbounded = true)
                                .padding(top = 3.dp),
                            text = text.toAnnotatedString(),
                            style = androidx.compose.ui.text.TextStyle.Default.copy(fontSize = readingFontSize)
                        )
                    }
                    Text(text = buildAnnotatedString {
                        withStyle(style) {
                            append(bottom)
                        }
                    })
                }
            }
        )
    }

}

private fun List<TextComponent>.toSingle(): List<TextComponent> {
    if (this.isEmpty())
        return listOf()

    val first = this[0]

    for (i in 1 until this.size)
        first.append(this[i])

    return listOf(first)
}