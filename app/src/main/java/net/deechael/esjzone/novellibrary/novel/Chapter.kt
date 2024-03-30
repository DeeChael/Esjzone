package net.deechael.esjzone.novellibrary.novel

import net.deechael.esjzone.novellibrary.component.Component
import java.io.Serializable

private val URL_REGEX = "https://www\\.esjzone\\.me/forum/([0-9]+)/[0-9]+\\.html".toRegex()

data class Chapter(
    val name: String,
    val url: String,
    val isHistory: Boolean
) : Serializable {

    fun novelId(): String {
        return URL_REGEX.find(this.url)!!.groupValues[1]
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (this === other)
            return true
        if (other !is Chapter)
            return false
        return this.url == other.url || this.url === other.url || this.url.contentEquals(other.url)
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

}

data class DetailedChapter(
    val name: String,
    val content: List<Component>,
    val previous: Chapter?,
    val next: Chapter?
) : Serializable