package net.deechael.esjzone.novellibrary.novel

import java.io.Serializable

interface Novel : Serializable {
    val name: String
    val url: String
}

data class CategoryNovel(
    override val name: String,
    override val url: String,
    val forumUrl: String
) : Novel

data class CoveredNovel(
    val coverUrl: String,
    override val name: String,
    override val url: String,
    val views: Int,
    val likes: Int,
    val isAdult: Boolean
) : Novel {

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (this === other)
            return true
        if (other !is Novel)
            return false
        return other.url.contentEquals(this.url)
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }

}

private val FORUM_URL_REGEX = "/forum/[0-9]+/([0-9]+)/".toRegex()

data class DetailedNovel(
    val name: String,
    val cover: String,
    val views: Int,
    val likes: Int,
    val words: Int,
    val type: String,
    val author: String,
    val forumUrl: String,
    val tags: List<String>,
    val isAdult: Boolean,
    val description: NovelDescription,
    val chapterList: NovelChapterList
) : Serializable {

    fun id(): String {
        return FORUM_URL_REGEX.find(this.forumUrl)!!.groupValues[1]
    }

}